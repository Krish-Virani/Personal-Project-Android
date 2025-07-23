package com.example.personalproject.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.personalproject.data.DataStoreManager
import com.example.personalproject.presentation.screens.LoginScreen
import com.example.personalproject.presentation.screens.SignupScreen1
import com.example.personalproject.presentation.screens.customer.CustomerHomeScreen
import com.example.personalproject.presentation.screens.dealer.AlloyDetailsScreen
import com.example.personalproject.presentation.screens.dealer.DealerHomeScreen
import com.example.personalproject.presentation.screens.dealer.DealerProfileScreen
import com.example.personalproject.presentation.screens.dealer.UploadAlloyImageScreen
import com.example.personalproject.presentation.viewmodels.AuthViewModel
import com.example.personalproject.presentation.viewmodels.CustomerViewModel
import com.example.personalproject.presentation.viewmodels.DealerViewModel


sealed class Screen(val route: String) {
    object Signup1 : Screen("signup1")
    object Login : Screen("login")

    object CustomerGraph : Screen("customer_graph")
    object CustomerHome : Screen("customer_home")

    object DealerGraph : Screen("dealer_graph") // ✅ parent graph for dealer
    object DealerHome : Screen("dealer_home")
    object AlloyDetails : Screen("alloy_details")

    object DealerDetails : Screen("dealer_details")

    object UploadAlloyImage: Screen("upload_alloy_image")
}



@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun AppNavGraph(
    role: String?,
    navController: NavHostController,
    dataStoreManager: DataStoreManager
) {
    val authViewModel : AuthViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = when (role) {
            "CUSTOMER" -> Screen.CustomerGraph.route
            "DEALER" -> Screen.DealerGraph.route // 🔁 use parent graph here
            else -> Screen.Signup1.route
        }
    ) {

        // Customer and Auth screens (not shared ViewModel)
        composable(Screen.Signup1.route) {
            SignupScreen1(navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        navigation(
            startDestination = Screen.CustomerHome.route,
            route = Screen.CustomerGraph.route
        ) {
            composable(Screen.CustomerHome.route) { backStackEntry ->
                val parentEntry = remember { navController.getBackStackEntry(Screen.CustomerGraph.route) }
                val customerViewModel: CustomerViewModel = hiltViewModel(parentEntry)

                CustomerHomeScreen(navController, customerViewModel)
            }

        }

        // ✅ Dealer graph to share DealerViewModel
        navigation(
            startDestination = Screen.DealerHome.route,
            route = Screen.DealerGraph.route
        ) {
            composable(Screen.DealerHome.route) { backStackEntry ->
                val parentEntry = remember { navController.getBackStackEntry(Screen.DealerGraph.route) }
                val dealerViewModel: DealerViewModel = hiltViewModel(parentEntry)

                DealerHomeScreen(navController, dealerViewModel)
            }

            composable(Screen.AlloyDetails.route) { backStackEntry ->
                val parentEntry = remember { navController.getBackStackEntry(Screen.DealerGraph.route) }
                val dealerViewModel: DealerViewModel = hiltViewModel(parentEntry)

                AlloyDetailsScreen(navController, dealerViewModel)
            }

            composable(Screen.DealerDetails.route) { backStackEntry ->
                val parentEntry = remember { navController.getBackStackEntry(Screen.DealerGraph.route) }
                val dealerViewModel: DealerViewModel = hiltViewModel(parentEntry)

                DealerProfileScreen(
                    navController,
                    dealerViewModel
                ) {
                    authViewModel.logout()
                    navController.navigate("signup1"){
                        popUpTo(0)
                    }
                }

            }

            composable(Screen.UploadAlloyImage.route) { backStackEntry ->
                val parentEntry = remember { navController.getBackStackEntry(Screen.DealerGraph.route) }
                val dealerViewModel: DealerViewModel = hiltViewModel(parentEntry)

                UploadAlloyImageScreen(dealerViewModel, dealerViewModel.selectedAlloy?.id ?: " ")
            }
        }
    }
}





