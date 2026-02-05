package ru.vsu.roadmap.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vsu.roadmap.R
import ru.vsu.roadmap.ui.viewmodel.EditProfileViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel? = null,
    onSaveClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    // Handling saving success
    val context = LocalContext.current
    if (viewModel?.isSaved == true) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Изменения сохранены", Toast.LENGTH_SHORT).show()
            onSaveClick()
        }
    }

    // Local state for unsupported fields (Surname, DOB) if needed or binding to VM for supported ones
    // Note: Since VM initializes, we should use its state directly if available
    
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                        viewModel?.dob = formatter.format(Date(millis))
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

    if (viewModel?.isLoading == true && viewModel.name.isEmpty()) {
         Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
             CircularProgressIndicator(color = Color.Black)
         }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.edit_profile_title),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            // Email (Read Only)
            OutlinedTextField(
                value = viewModel?.email ?: "",
                onValueChange = {},
                label = { Text(stringResource(R.string.email_label)) },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Email, null) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.LightGray.copy(alpha = 0.1f),
                    unfocusedContainerColor = Color.LightGray.copy(alpha = 0.1f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Name
            OutlinedTextField(
                value = viewModel?.name ?: "",
                onValueChange = { viewModel?.name = it },
                label = { Text(stringResource(R.string.name_hint)) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Person, null) }
            )

            Spacer(modifier = Modifier.height(16.dp))
            
            // Surname (Local state in VM)
            OutlinedTextField(
                value = viewModel?.surname ?: "",
                onValueChange = { viewModel?.surname = it },
                label = { Text(stringResource(R.string.surname_hint)) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Person, null) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // DOB (Local state in VM)
            OutlinedTextField(
                value = viewModel?.dob ?: "",
                onValueChange = { viewModel?.dob = it },
                label = { Text(stringResource(R.string.dob_hint)) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { 
                    Icon(
                        Icons.Default.DateRange, 
                        null,
                        modifier = Modifier.clickable { showDatePicker = true }
                    ) 
                },
                readOnly = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Position (Mapped to 'about')
            OutlinedTextField(
                value = viewModel?.position ?: "",
                onValueChange = { viewModel?.position = it },
                label = { Text(stringResource(R.string.position_hint)) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Settings, null) }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Save Button
            Button(
                onClick = {
                    viewModel?.saveProfile()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(25.dp),
                enabled = viewModel?.isLoading == false
            ) {
                if (viewModel?.isLoading == true) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text(stringResource(R.string.save_changes))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logout Button
            Button(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(stringResource(R.string.logout_button))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen()
}
