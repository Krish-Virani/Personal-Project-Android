package com.example.personalproject

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.personalproject.data.DataStoreManager
import com.example.personalproject.presentation.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

// 1212121212
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //WindowCompat.setDecorFitsSystemWindows(window, false)
        val splash = installSplashScreen()
        var keepSplash = true
        splash.setKeepOnScreenCondition { keepSplash }

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val userRole = remember { mutableStateOf<String?>(null) }

            val done = remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                val role = dataStoreManager.roleFlow.firstOrNull()
                userRole.value = role
                Log.d("SplashDebug", "Role = $role")
                done.value = true
                keepSplash = false
            }

            // Only load navigation once userRole is ready
            if(done.value) {
                AppNavGraph(
                    userRole.value,
                    navController = navController,
                    dataStoreManager = dataStoreManager,

                )
            }
        }
    }


}

