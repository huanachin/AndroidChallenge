package com.example.androidchallenge.ui.pages.decision

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.androidchallenge.ui.navigation.Screen
import com.example.androidchallenge.ui.util.Constants.PARAMS
import com.example.androidchallenge.ui.util.encode
import com.example.androidchallenge.ui.util.parseModel

@Composable
fun DecisionPage(
    navController: NavController,
    params: String? = null,
    viewModel: DecisionViewModel = hiltViewModel()
) {

    val state = viewModel.eventsFlow.collectAsState(initial = null)
    val event = state.value

    LaunchedEffect(event) {
        when (event) {
            is DecisionEvent.NavigateHome -> {
                val route = if (params == null) "${Screen.HomeScreen.route}/${event.userId}"
                else "${Screen.HomeScreen.route}/${event.userId}?$PARAMS=${params.encode()}"
                navController.navigate(route) {
                    popUpTo(Screen.DecisionScreen.route) { inclusive = true }
                }
            }
            is DecisionEvent.NavigateLogin -> {
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