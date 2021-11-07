package com.example.androidchallenge.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidchallenge.ui.pages.home.HomePage
import com.example.androidchallenge.ui.pages.login.LoginPage
import com.example.androidchallenge.ui.pages.register.RegisterPage
import com.example.androidchallenge.ui.pages.splash.SplashPage

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
        composable(route = Screen.SplashScreen.route) { SplashPage(navController = navController) }
        composable(route = Screen.RegisterScreen.route) { RegisterPage(navController = navController) }
        composable(route = Screen.HomeScreen.route) { HomePage(navController = navController) }
        composable(route = Screen.LoginScreen.route) { LoginPage(navController = navController) }
    }
}