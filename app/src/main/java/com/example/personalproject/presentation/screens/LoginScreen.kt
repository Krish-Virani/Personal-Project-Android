package com.example.personalproject.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import com.example.personalproject.data.remotedata.auth.LoginRequest
import com.example.personalproject.data.remotedata.auth.LoginResponse
import com.example.personalproject.data.remotedata.auth.SignupRequest
import com.example.personalproject.domain.model.DealerProfile
import com.example.personalproject.presentation.UiState
import com.example.personalproject.presentation.viewmodels.AuthViewModel

@Composable
fun LoginScreen(navController: NavController){

    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsState()

    var phoneNumber by remember { mutableStateOf("") }

    val isPhoneValid = phoneNumber.matches(Regex("^\\d{10}\$"))
    val isFormValid = isPhoneValid

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
                text = "Log In",
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

            Spacer(Modifier.padding(20.dp))


            Row(
            ) {
                Text(
                    text = "Don't have an account. ",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.LightGray
                )
                Text(
                    text = "SignUp!",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.clickable(
                        onClick = { navController.navigate("signup1"){
                            popUpTo("login") { inclusive = true }
                        }
                        }
                    )
                )
            }

            Spacer(Modifier.padding(20.dp))

            Button(
                onClick = {
                    val request = LoginRequest(phoneNumber)
                    authViewModel.login(request)
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
                            popUpTo("login") { inclusive = true }
                        }
                    } else if (role == "DEALER") {
                        navController.navigate("dealer_home") {
                            popUpTo("login") { inclusive = true }
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