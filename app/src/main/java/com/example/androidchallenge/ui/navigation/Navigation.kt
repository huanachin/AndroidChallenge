package com.example.androidchallenge.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.androidchallenge.ui.navigation.types.DeepLinkTaskParamType
import com.example.androidchallenge.ui.navigation.types.TaskParamType
import com.example.androidchallenge.ui.pages.decision.DecisionPage
import com.example.androidchallenge.ui.pages.home.HomePage
import com.example.androidchallenge.ui.pages.login.LoginPage
import com.example.androidchallenge.ui.pages.register.RegisterPage
import com.example.androidchallenge.ui.pages.task.TaskPage
import com.example.androidchallenge.ui.util.Constants.PARAMS
import com.example.androidchallenge.ui.util.Constants.TASK
import com.example.androidchallenge.ui.util.Constants.USER_ID
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun Navigation(params: String?) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.DecisionScreen.route
    ) {
        composable(
            route = Screen.DecisionScreen.route
        ) {
            DecisionPage(
                navController = navController,
                params = params
            )
        }
        composable(route = Screen.RegisterScreen.route) { RegisterPage(navController = navController) }
        composable(
            route = "${Screen.HomeScreen.route}/{$USER_ID}?$PARAMS={$PARAMS}",
            arguments = listOf(
                navArgument(USER_ID) { type = NavType.StringType },
                navArgument(PARAMS) {
                    type = DeepLinkTaskParamType()
                    defaultValue = null
                    nullable = true
                })
        ) {
            it.arguments?.getString(USER_ID)?.let { userId ->
                HomePage(navController = navController, userId = userId)
            }
        }
        composable(route = Screen.LoginScreen.route) { LoginPage(navController = navController) }
        dialog(
            route = "${Screen.TaskDialog.route}/{$USER_ID}?$TASK={$TASK}",
            arguments = listOf(
                navArgument(USER_ID) { type = NavType.StringType },
                navArgument(TASK) {
                    type = TaskParamType()
                    defaultValue = null
                    nullable = true
                }
            )
        ) {
            TaskPage(
                navController = navController
            )
        }
    }
}