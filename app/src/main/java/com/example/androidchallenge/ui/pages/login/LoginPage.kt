package com.example.androidchallenge.ui.pages.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.androidchallenge.R
import com.example.androidchallenge.ui.custom.ErrorPasswordTextField
import com.example.androidchallenge.ui.custom.ErrorTextField
import com.example.androidchallenge.ui.custom.LoadingComponent
import com.example.androidchallenge.ui.navigation.Screen
import com.example.androidchallenge.ui.theme.AndroidChallengeTheme


@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun LoginPreview() {
    AndroidChallengeTheme {
        LoginPage(rememberNavController())
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun LoadingPreview() {
    AndroidChallengeTheme {
        LoadingComponent(true)
    }
}

@Composable
fun LoginPage(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val state = viewModel.eventsFlow.collectAsState(initial = null)
    val event = state.value

    val onLogin: (() -> Unit) = {
        viewModel.authenticate()
    }

    val onRegister: (() -> Unit) = {
        navController.navigate(Screen.RegisterScreen.route)
    }

    LaunchedEffect(event) {
        when (event) {
            is LoginEvent.NavigateHome -> {
                navController.navigate("${Screen.HomeScreen.route}/${event.userId}") {
                    popUpTo(Screen.LoginScreen.route) { inclusive = true }
                }
            }
            is LoginEvent.ShowError -> {

            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.welcome),
            style = MaterialTheme.typography.body1.copy(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(Modifier.height(32.dp))
        ErrorTextField(
            hasError = viewModel.hasError.value,
            text = viewModel.username.value,
            placeholder = stringResource(R.string.username),
            onTextChanged = { viewModel.onUsernameChanged(it) }
        )
        Spacer(Modifier.height(16.dp))
        ErrorPasswordTextField(
            hasError = viewModel.hasError.value,
            text = viewModel.password.value,
            placeholder = stringResource(R.string.password),
            onTextChanged = { viewModel.onPasswordChanged(it) }
        )
        Spacer(Modifier.height(32.dp))
        EnterButton(enabled = viewModel.isEnabled.value, onEnter = onLogin)
        if (viewModel.hasError.value) {
            Text(
                modifier = Modifier.padding(vertical = 16.dp),
                text = stringResource(R.string.username_password_invalid),
                style = MaterialTheme.typography.body1.copy(color = Color.Red)
            )
        }
        Spacer(modifier = Modifier.height(48.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                modifier = Modifier.alignByBaseline(),
                text = stringResource(R.string.dont_have_user_yet)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                modifier = Modifier
                    .alignByBaseline()
                    .clickable {
                        onRegister()
                    },
                text = stringResource(R.string.register_me),
                style = MaterialTheme.typography.body1.copy(color = Color.Blue)
            )
        }
    }
    LoadingComponent(viewModel.isLoading.value)
}

@Composable
fun EnterButton(enabled: Boolean = false, onEnter: () -> Unit) {
    Button(
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Blue,
            disabledBackgroundColor = Color.LightGray
        ),
        contentPadding = PaddingValues(16.dp),
        onClick = onEnter
    ) {
        Text(
            stringResource(R.string.enter),
            style = MaterialTheme.typography.body1.copy(
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        )
    }
}