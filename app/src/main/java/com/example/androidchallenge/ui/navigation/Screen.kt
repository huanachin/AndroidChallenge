package com.example.androidchallenge.ui.navigation

sealed class Screen(val route: String) {

    object DecisionScreen : Screen("decision_screen")
    object LoginScreen : Screen("login_screen")
    object RegisterScreen : Screen("register_screen")
    object HomeScreen : Screen("home_screen")
    object TaskDialog : Screen("task_dialog")

}