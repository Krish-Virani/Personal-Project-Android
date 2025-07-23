package com.example.personalproject.presentation.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.personalproject.data.DataStoreManager
import com.example.personalproject.domain.model.User
import com.example.personalproject.presentation.UiState
import com.example.personalproject.presentation.screens.dealer.DealerTopAppBar
import com.example.personalproject.presentation.viewmodels.AuthViewModel
import com.example.personalproject.presentation.viewmodels.CustomerViewModel
import com.example.personalproject.presentation.viewmodels.DealerViewModel
import kotlinx.coroutines.flow.firstOrNull

@Composable
fun CustomerHomeScreen(navController: NavController, customerViewModel: CustomerViewModel) {
    val authViewModel: AuthViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            CustomerTopAppBar(
                onProfileClick = {
                    navController.navigate("dealer_details")
                }
            )
        },
        content = { padding->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFDFCFB),
                                Color(0xFFE2EBF0),
                                Color(0xFFE2EBF0)
                            )
                        )
                    ),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 24.dp)
            ){

            }
        }
    )
    Button(
        onClick = {
            navController.navigate("signup1") {
                popUpTo(0) // removes entire backstack
            }
            authViewModel.logout()
        }
    ) { }
}


@Composable
fun CustomerTopAppBar(
    onProfileClick: () -> Unit
) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, start = 24.dp, end = 24.dp, bottom = 16.dp)
            .background(Color(0xFFFDFCFB))
    ){
        Text(
            text = "Welcome to app!..",
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Placeholder Shop Image",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .clickable(
                        onClick = { onProfileClick() }
                    )
                    .padding(end = 4.dp)
            )


    }

}