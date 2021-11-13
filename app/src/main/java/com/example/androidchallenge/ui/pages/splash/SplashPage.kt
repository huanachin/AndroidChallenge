package com.example.androidchallenge.ui.pages.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.androidchallenge.ui.navigation.Screen

@Composable
fun SplashPage(navController: NavController, viewModel: SplashViewModel = hiltViewModel()) {

    val state = viewModel.eventsFlow.collectAsState(initial = null)
    val event = state.value

    LaunchedEffect(event) {
        when (event) {
            is SplashEvent.NavigateHome -> {
                navController.navigate("${Screen.HomeScreen.route}/${event.userId}") {
                    popUpTo(Screen.DecisionScreen.route) { inclusive = true }
                }
            }
            is SplashEvent.NavigateLogin -> {
                navController.navigate(Screen.LoginScreen.route) {
                    popUpTo(Screen.DecisionScreen.route) { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}