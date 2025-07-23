package com.example.personalproject.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.personalproject.R
import com.example.personalproject.States
import com.example.personalproject.data.DataStoreManager
import com.example.personalproject.data.remotedata.auth.LoginResponse
import com.example.personalproject.data.remotedata.auth.SignupRequest
import com.example.personalproject.domain.model.DealerProfile
import com.example.personalproject.presentation.UiState
import com.example.personalproject.presentation.viewmodels.AuthViewModel


@Composable
fun SignupScreen1(navController: NavController ){

    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsState()

    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf<String?>(null) }
    var shopName by remember { mutableStateOf<String>("") }
    var shopCity by remember { mutableStateOf<String>("") }
    var shopState by remember { mutableStateOf<String>("") }
    var selectedState by remember { mutableStateOf("") }

    val isNameValid = name.trim().length >= 1
    val isPhoneValid = phoneNumber.matches(Regex("^\\d{10}\$"))
    val isFormValid = if(selectedRole=="DEALER") {
        isPhoneValid && isPhoneValid && shopName!="" && shopCity!="" && selectedState!=""
    } else if(selectedRole=="CUSTOMER") {
        isPhoneValid && isNameValid
    } else{
        false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Image(
            painter = painterResource(id = R.drawable.signup_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = 0.99f }
                .blur(16.dp)
        )
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Sign Up",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(Modifier.padding(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            ) {
                Text(
                    text = "Enter name",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.LightGray
                )
            }

            // Name TextField with Icon
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.White) },
                singleLine = true,
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color(0xFF90CAF9),
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.padding(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            ) {
                Text(
                    text = "Enter phoneNumber",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.LightGray
                )
            }

            // Phone TextField with Icon
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Color.White) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color(0xFF90CAF9),
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.padding(12.dp))

            Text(
                text = "Select Your Role",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.LightGray
            )

            Spacer(Modifier.padding(6.dp))

            // Role Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                RoleButton(
                    text = "Customer",
                    isSelected = selectedRole == "CUSTOMER",
                    onClick = {
                        selectedRole = "CUSTOMER"
                    }
                )

                RoleButton(
                    text = "Dealer",
                    isSelected = selectedRole == "DEALER",
                    onClick = {
                        selectedRole = "DEALER"
                    }
                )
            }

            Spacer(Modifier.padding(12.dp))


            if(selectedRole == "DEALER")
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                ) {
                    Text(
                        text = "Enter Shop Name",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.LightGray
                    )
                }
                OutlinedTextField(
                    value = shopName,
                    onValueChange = { shopName = it },
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = Color.White) },
                    singleLine = true,
                    shape = RoundedCornerShape(15.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color(0xFF90CAF9),
                        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.padding(6.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Column(
                        modifier = Modifier.weight(1f)
                    ){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp)
                        ) {
                            Text(
                                text = "Select Shop State",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.LightGray
                            )
                        }

                        Column() {
                            StateDropdownField(
                                selectedState = selectedState,
                                onStateSelected = { selectedState = it }
                            )
                        }
                    }
                    Spacer(Modifier.padding(4.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp)
                        ) {
                            Text(
                                text = "Select Shop City",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.LightGray
                            )
                        }

                        OutlinedTextField(
                            value = shopCity,
                            onValueChange = { shopCity = it },
                            leadingIcon = { Icon(Icons.Default.Place, contentDescription = null, tint = Color.White) },
                            singleLine = true,
                            shape = RoundedCornerShape(15.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Black,
                                unfocusedBorderColor = Color(0xFF90CAF9),
                                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                }
            }

            Spacer(Modifier.padding(20.dp))


            Row(
            ) {
                Text(
                    text = "Already have an account. ",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.LightGray
                )
                Text(
                    text = "LogIn!",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.clickable(
                        onClick = { navController.navigate("login"){
                            popUpTo("signup1") { inclusive = true }
                        }
                        }
                    )
                )
            }

            Spacer(Modifier.padding(20.dp))

            Button(
                onClick = {

                    if(selectedRole == "CUSTOMER") {
                        val request = SignupRequest(name, phoneNumber, selectedRole)
                        authViewModel.signup(request)
                    } else{
                        val dealerProfile = DealerProfile(shopName, shopCity, selectedState)
                        val request = SignupRequest(name, phoneNumber, selectedRole, dealerProfile)
                        authViewModel.signup(request)
                    }
                          },
                enabled = isFormValid,
                modifier = Modifier
                    .padding(16.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(Color.White),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next",
                    tint = Color.Black
                )
            }

        }

        when (authState) {
            is UiState.Loading -> {
                // Show a progress indicator or loading dialog
                CircularProgressIndicator()
            }

            is UiState.Success -> {
                val response = (authState as UiState.Success<LoginResponse>)
                val  data = response.data
                val role = data.role
                LaunchedEffect(Unit) {
                    if (role == "CUSTOMER") {
                        navController.navigate("customer_home") {
                            popUpTo("signup1") { inclusive = true }
                        }
                    } else if (role == "DEALER") {
                        navController.navigate("dealer_home") {
                            popUpTo("signup1") { inclusive = true }
                        }
                    }
                }
            }

            is UiState.Error -> {
                val message = (authState as UiState.Error).message ?: "Something went wrong"
                Text(
                    text = message,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            else-> {
                // Nothing to do here
            }
        }
    }

}

@Composable
fun RoleButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val background = if (isSelected) Color.Green else Color.DarkGray

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(background)
            .clickable { onClick() }
            .padding(horizontal = 28.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = if (!isSelected) Color.White else Color.Black,
            fontWeight = FontWeight.Medium
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateDropdownField(
    selectedState: String,
    onStateSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedState,
            onValueChange = {}, // Disable manual editing
            readOnly = true,
            leadingIcon = { Icon(Icons.Default.Place, contentDescription = null, tint = Color.White)},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            shape = RoundedCornerShape(15.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Black,
                                unfocusedBorderColor = Color(0xFF90CAF9),
                                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.White
                            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            States.forEach { state ->
                DropdownMenuItem(
                    text = { Text(state) },
                    onClick = {
                        onStateSelected(state)
                        expanded = false
                    }
                )
            }
        }
    }
}
