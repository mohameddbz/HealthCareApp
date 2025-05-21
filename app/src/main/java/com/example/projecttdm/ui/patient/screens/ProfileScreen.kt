package com.example.projecttdm.ui.patient.screens

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.projecttdm.ui.common.components.DeconnectionButton
import com.example.projecttdm.ui.common.components.ProfileImage
import com.example.projecttdm.ui.common.components.UserProfileImage
import com.example.projecttdm.viewmodel.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onNavigateToSettings: () -> Unit = {}
) {
    val profile = viewModel.profileState
    val isEditing = viewModel.isEditing
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
    val selectedImageUri = viewModel.selectedImageUri
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Date picker
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    // Image picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.updateProfileImage(context,it.toString())
        }
    }

    // Show date picker if needed
    if (showDatePicker && isEditing) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val date = Date(it)
                        val formattedDate = viewModel.formatDateForApi(date)
                        viewModel.updateBirthDate(formattedDate)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Show error message if needed
    errorMessage?.let {
        LaunchedEffect(it) {
            // Show snackbar or toast with error message
            println("Error: $it")
        }
    }

    // Show loading indicator if needed
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // If profile is not loaded, show a message
    if (profile == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Profile not found...")
        }
        return
    }

    // Main content
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Profile",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = onNavigateToSettings) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings"
                    )
                }
            }

            // Profile card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Image
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .padding(8.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        // Profile image or placeholder
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                .clickable(enabled = isEditing) {
                                    if (isEditing) {
                                        imagePickerLauncher.launch("image/*")
                                    }
                                }
                        ) {
                            // Show selected image if available, otherwise show profile image or placeholder
                            when {
                                selectedImageUri != null -> {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(selectedImageUri)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Selected profile photo",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                profile.image != null -> {
//                                    AsyncImage(
//                                        model = ImageRequest.Builder(context)
//                                            .data(profile.image)
//                                            .crossfade(true)
//                                            .build(),
//                                        contentDescription = "Profile photo",
//                                        contentScale = ContentScale.Crop,
//                                        modifier = Modifier.fillMaxSize()
//                                    )
                                    ProfileImage(
                                        imageBlob = profile.image, // or your image object
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                    )
                                }
                                else -> {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Profile icon",
                                        modifier = Modifier
                                            .size(60.dp)
                                            .align(Alignment.Center),
                                        tint = Color.Gray
                                    )
                                }
                            }
                        }

                        // Edit image button
                        if (isEditing) {
                            FloatingActionButton(
                                onClick = { imagePickerLauncher.launch("image/*") },
                                modifier = Modifier
                                    .size(36.dp)
                                    .align(Alignment.BottomEnd),
                                containerColor = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Change photo",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Display name when not in edit mode
                    if (!isEditing) {
                        Text(
                            text = "${profile.first_name} ${profile.last_name}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Text(
                            text = profile.email,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Section title
                    Text(
                        text = "Personal Information",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 8.dp)
                    )

                    Divider(modifier = Modifier.padding(bottom = 16.dp))

                    // Profile Fields
                    EditableProfileField(
                        icon = Icons.Default.Person,
                        value = profile.first_name,
                        onValueChange = { viewModel.updateFirstName(it) },
                        placeholder = "First name",
                        isEditing = isEditing
                    )

                    EditableProfileField(
                        icon = Icons.Default.Person,
                        value = profile.last_name,
                        onValueChange = { viewModel.updateLastName(it) },
                        placeholder = "Last name",
                        isEditing = isEditing
                    )

                    EditableProfileField(
                        icon = Icons.Default.Phone,
                        value = profile.phone,
                        onValueChange = { viewModel.updatePhone(it) },
                        placeholder = "Phone number",
                        isEditing = isEditing
                    )

                    EditableProfileField(
                        icon = Icons.Default.Email,
                        value = profile.email,
                        onValueChange = { viewModel.updateEmail(it) },
                        placeholder = "Email address",
                        isEditing = isEditing
                    )

                    // Date of birth with picker
                    val birthDateText = viewModel.formatDateForDisplay(profile.PATIENT.date_birthday)

                    ProfileField(
                        icon = Icons.Default.DateRange,
                        label = "Date of Birth",
                        value = birthDateText,
                        onClick = { if (isEditing) showDatePicker = true },
                        isClickable = isEditing,
                        showArrow = isEditing
                    )

                    // Gender field with dropdown
                    var expanded by remember { mutableStateOf(false) }
                    val genderDisplay = if (profile.PATIENT.sexe == "male") "Male" else "Female"

                    ProfileField(
                        icon = if (profile.PATIENT.sexe == "male") Icons.Default.Male else Icons.Default.Female,
                        label = "Gender",
                        value = genderDisplay,
                        onClick = { if (isEditing) expanded = true },
                        isClickable = isEditing,
                        showArrow = isEditing
                    )

                    // Dropdown menu for gender selection
                    DropdownMenu(
                        expanded = expanded && isEditing,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.width(200.dp)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Male") },
                            onClick = {
                                viewModel.updateSexe("male")
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Female") },
                            onClick = {
                                viewModel.updateSexe("female")
                                expanded = false
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isEditing) Arrangement.SpaceBetween else Arrangement.Center
                    ) {
                        if (isEditing) {
                            // Cancel button when editing
                            OutlinedButton(
                                onClick = { viewModel.toggleEditMode() },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Cancel")
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            // Save button when editing
                            Button(
                                onClick = { viewModel.saveProfile(context) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Save Changes")
                            }
                        } else {
                            // Edit button when not editing
                            Button(
                                onClick = { viewModel.toggleEditMode() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Edit Profile")
                            }
                        }
                    }

                    // Logout button (only shown when not editing)
                    if (!isEditing) {
                        Spacer(modifier = Modifier.height(16.dp))
                        DeconnectionButton()
//                        OutlinedButton(
//                            onClick = { viewModel.logout() },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(50.dp),
//                            colors = ButtonDefaults.outlinedButtonColors(
//                                contentColor = Color.Red
//                            ),
//                            border = BorderStroke(1.dp, Color.Red),
//                            shape = RoundedCornerShape(8.dp)
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Logout,
//                                contentDescription = "Logout",
//                                tint = Color.Red,
//                                modifier = Modifier.size(20.dp)
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(
//                                text = "Log out",
//                                fontSize = 16.sp,
//                                color = Color.Red
//                            )
//                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditableProfileField(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isEditing: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = placeholder,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))

        if (isEditing) {
            BasicTextField(
                value = value,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                ),
                onValueChange = onValueChange,
                singleLine = true,
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )
        } else {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = placeholder,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = if (value.isNotEmpty()) value else "Not specified",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun ProfileField(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit,
    isClickable: Boolean,
    showArrow: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(enabled = isClickable) { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                color = Color.Gray,
                fontSize = 14.sp
            )
            Text(
                text = value.ifEmpty { "Not specified" },
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp
            )
        }

        if (showArrow) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Select",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}