import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.projecttdm.viewmodel.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel()
) {
    val profile = viewModel.profileState
    val isEditing = viewModel.isEditing
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
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
            viewModel.updateProfileImage(it.toString())
        }
    }

    // Afficher le sélecteur de date si nécessaire
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

    // Afficher un message d'erreur si nécessaire
    errorMessage?.let {
        LaunchedEffect(it) {
            // Vous pourriez afficher un Snackbar ou une alerte ici
        }
    }

    // Afficher un indicateur de chargement si nécessaire
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // Si le profil n'est pas chargé, afficher un message
    if (profile == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Update profile...")
        }
        return
    }

    // Box englobant tout l'écran
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header
                    Text(
                        text = "My profile",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp),
                    )

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
                                .size(140.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                                .align(Alignment.Center)
                                .clickable(enabled = isEditing) {
                                    if (isEditing) {
                                        imagePickerLauncher.launch("image/*")
                                    }
                                }
                        ) {
                            if (profile.image != null) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(profile.image)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Photo de profil",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Icône de profil",
                                    modifier = Modifier
                                        .size(65.dp)
                                        .align(Alignment.Center),
                                    tint = Color.Gray
                                )
                            }
                        }

                        // Camera icon for image upload - toujours visible
                        if (isEditing){
                            IconButton(
                                onClick = { imagePickerLauncher.launch("image/*") },
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                                    .align(Alignment.BottomEnd)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Changer la photo",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        }


                    Spacer(modifier = Modifier.height(8.dp))
                    if (!isEditing) {
                        Text(
                            text = profile.first_name+" "+profile.last_name,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Profile Fields - Tous les champs sont modifiables en mode édition
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
                        placeholder = "last name",
                        isEditing = isEditing
                    )

                    EditableProfileField(
                        icon = Icons.Default.Phone,
                        value = profile.phone,
                        onValueChange = { viewModel.updatePhone(it) },
                        placeholder = "phone",
                        isEditing = isEditing
                    )

                    EditableProfileField(
                        icon = Icons.Default.Email,
                        value = profile.email,
                        onValueChange = { viewModel.updateEmail(it) },
                        placeholder = "Email",
                        isEditing = isEditing
                    )

                    // Date de naissance avec picker
                    val birthDateText = viewModel.formatDateForDisplay(profile.PATIENT.date_birthday)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                if (isEditing) {
                                    showDatePicker = true
                                }
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Date de naissance",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = birthDateText,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Sélectionner",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Sexe field
                    var expanded by remember { mutableStateOf(false) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                if(isEditing){
                                    expanded = true
                                }
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (profile.PATIENT.sexe == "male") Icons.Default.Male else Icons.Default.Female,
                            contentDescription = "Sexe",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))

                        if (isEditing) {
                            val options = listOf("male", "female")

                            Box {
                                Text(
                                    text = if (profile.PATIENT.sexe == "male") "Male" else "Female",
                                    modifier = Modifier.fillMaxWidth()
                                                        .clickable { expanded = true }
                                )

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    options.forEach { option ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(if (option == "male") "Male" else "Female")
                                            },
                                            onClick = {
                                                viewModel.updateSexe(option)
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = if (profile.PATIENT.sexe == "male") "Male" else "Female"
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Sélectionner",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Buttons
                    Button(
                        onClick = {
                            if (isEditing) {
                                viewModel.saveProfile()
                            } else {
                                viewModel.toggleEditMode()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isEditing) "Update" else "Edit Profile",
                            fontSize = 16.sp,
                            color = Color.White // Pour que le texte ressorte sur le fond primaire
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    if (!isEditing){
                        OutlinedButton(
                            onClick = { viewModel.logout() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Red
                            ),
                            border = BorderStroke(1.dp, Color.Red),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = "Déconnexion",
                                tint = Color.Red,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Log out",
                                fontSize = 16.sp,
                                color = Color.Red
                            )
                        }
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
    isEditing: Boolean,
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
                ),
                onValueChange = onValueChange,
                singleLine = true,
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color =  MaterialTheme.colorScheme.onBackground
                            )
                        }
                        innerTextField()
                    }
                }
            )
        } else {
            Text(
                text = value.ifEmpty { placeholder },
                color =  MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
        }

        if (showArrow) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Sélectionner",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}