package com.example.projecttdm.ui.auth.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.projecttdm.composant.NavBarTop
import com.example.projecttdm.data.endpoint.ApiClient
import com.example.projecttdm.data.model.auth.AuthResponse
import com.example.projecttdm.state.UiState
import com.example.projecttdm.ui.common.components.showError
import com.example.projecttdm.ui.patient.PatientActivity
import com.example.projecttdm.viewmodel.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun FillProfileScreen(authViewModel: AuthViewModel) {
    val email by authViewModel.email.collectAsState()
    val password by authViewModel.password.collectAsState()
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val authState by authViewModel.authState.collectAsState()



    val scope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    val firstNameFocusRequester = remember { FocusRequester() }
    val lastNameFocusRequester = remember { FocusRequester() }
    val phoneFocusRequester = remember { FocusRequester() }

    val imagePicker = rememberImagePicker { uri ->
        imageUri = uri
    }

    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        ShowDatePickerDialog(
            datePickerState = datePickerState,
            onDismiss = { showDatePicker = false },
            onDateSelected = { date ->
                birthDate = date
                showDatePicker = false
            }
        )
    }

    Column {
        NavBarTop("Fill Your Profile")

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                ProfileImageSection(
                    imageUri = imageUri,
                    onEditClick = { imagePicker.launch("image/*") }
                )
            }

            item {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    placeholder = { Text("First Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(firstNameFocusRequester),
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                            lastNameFocusRequester.requestFocus()
                        }
                    ),
                    colors = customTextFieldColors()
                )
            }

            item {
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    placeholder = { Text("Last Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(lastNameFocusRequester),
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                            phoneFocusRequester.requestFocus()
                        }
                    ),
                    colors = customTextFieldColors()
                )
            }

            item {
                OutlinedTextField(
                    value = birthDate,
                    onValueChange = {},
                    placeholder = { Text("Date of Birth") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true },
                    shape = RoundedCornerShape(16.dp),
                    readOnly = true,
                    enabled = false,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                Icons.Default.CalendarToday,
                                contentDescription = "Calendar",
                                tint = Color.Gray
                            )
                        }
                    },
                    colors = customTextFieldColors()
                )
            }

            item {
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = { Text("Phone") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(phoneFocusRequester),
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    ),
                    trailingIcon = {
                        Icon(Icons.Default.Phone, contentDescription = "Phone", tint = Color.Gray)
                    },
                    colors = customTextFieldColors()
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = gender,
                        onValueChange = {},
                        placeholder = { Text("Gender") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        readOnly = true,
                        enabled = false,
                        trailingIcon = {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Select Gender",
                                    tint = Color.Gray
                                )
                            }
                        },
                        colors = customTextFieldColors()
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        listOf("Male", "Female", "Other").forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    gender = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))

                when (authState) {
                    is UiState.Init -> {}
                    is UiState.Loading -> CircularProgressIndicator()
                    is UiState.Error -> showError((authState as UiState.Error).message)
                    is UiState.Success -> LaunchedEffect(Unit) {
                        withContext(Dispatchers.IO) {
                            val succesSignup = authState as UiState.Success<AuthResponse>
                            val sharedPreferences =
                                context.getSharedPreferences("doctor_prefs", Context.MODE_PRIVATE)
                            with(sharedPreferences.edit()) {
                                putBoolean("user_connected", true)
                                putString("auth_token", succesSignup.data.token)
                                putString("role", succesSignup.data.role)
                                apply()
                            }
                            ApiClient.setTokenProvider { succesSignup.data.token }
                        }
                        val intent = Intent(context, PatientActivity::class.java)
                        context.startActivity(intent)
                        val activity = context as? Activity
                        activity?.finish()
                    }


                }

                Button(
                    onClick = {
                        keyboardController?.hide()
                        // Handle continue logic
                        scope.launch {
                            authViewModel.registerUser(firstName,lastName,email,password,phone,"patient",imageUri,context)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Continue",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }

    LaunchedEffect(Unit) {
        firstNameFocusRequester.requestFocus()
    }
}


@Composable
fun ProfileImageSection(imageUri: Uri?, onEditClick: () -> Unit) {
    Box(contentAlignment = Alignment.BottomEnd) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(Color(0xFFF4F4F6)),
            contentAlignment = Alignment.Center
        ) {
            if (imageUri == null) {
                // Show person icon when no image is selected
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Person",
                    modifier = Modifier.size(80.dp),
                    tint = Color.Gray
                )
            } else {
                // Display the selected image using Coil
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }

        FloatingActionButton(
            onClick = onEditClick,
            modifier = Modifier.size(40.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ) {
            Icon(
                Icons.Default.Edit,
                contentDescription = "Edit",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun rememberImagePicker(onImagePicked: (Uri?) -> Unit) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
) { uri: Uri? ->
    onImagePicked(uri)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDatePickerDialog(
    datePickerState: DatePickerState,
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { millis ->
                    val calendar = Calendar.getInstance().apply { timeInMillis = millis }
                    val formattedDate =
                        "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
                    onDateSelected(formattedDate)
                }
            }) {
                Text("OK", color = Color(0xFF4169E1))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        },
        colors = DatePickerDefaults.colors(
            selectedDayContainerColor = Color(0xFF4169E1),
            selectedDayContentColor = Color.White,
            todayContentColor = Color(0xFF4169E1),
            todayDateBorderColor = Color(0xFF4169E1)
        )
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun customTextFieldColors() = TextFieldDefaults.outlinedTextFieldColors(
    unfocusedBorderColor = Color(0xFFEEEEEE),
    focusedBorderColor = Color(0xFF4169E1),
    cursorColor = Color(0xFF4169E1),
    disabledBorderColor = Color(0xFFEEEEEE),
    disabledTextColor = Color.Black,
    disabledPlaceholderColor = Color.Gray,
    focusedTextColor = Color.Black,
    unfocusedPlaceholderColor = Color.Gray,
    unfocusedLabelColor = Color.Gray,
    containerColor = Color(0xFFF5F5F5)
)