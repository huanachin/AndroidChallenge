package com.example.androidchallenge.ui.pages.register

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.androidchallenge.R
import com.example.androidchallenge.ui.custom.ErrorPasswordTextField
import com.example.androidchallenge.ui.custom.LoadingComponent
import com.example.androidchallenge.ui.custom.ValidTextField
import com.example.androidchallenge.ui.navigation.Screen
import com.example.androidchallenge.ui.theme.AndroidChallengeTheme

@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun RegisterPreview() {
    AndroidChallengeTheme {
        RegisterPage(rememberNavController())
    }
}

@Composable
fun RegisterPage(navController: NavController, viewModel: RegisterViewModel = viewModel()) {

    val state = viewModel.eventsFlow.collectAsState(initial = null)
    val event = state.value

    val onRegister: (() -> Unit) = {
        viewModel.register()
    }

    LaunchedEffect(event) {
        when (event) {
            is RegisterEvent.NavigateHome -> {
                navController.navigate(Screen.HomeScreen.route) {
                    popUpTo(Screen.RegisterScreen.route) { inclusive = true }
                }
            }
            is RegisterEvent.ShowError -> {

            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.create_user),
            style = MaterialTheme.typography.body1.copy(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(32.dp))
        ValidTextField(
            isValid = viewModel.isUsernameValid.value,
            text = viewModel.username.value,
            placeholder = stringResource(R.string.username),
            onTextChanged = { viewModel.onUsernameChanged(it) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        ErrorPasswordTextField(
            text = viewModel.password.value,
            placeholder = stringResource(R.string.password),
            onTextChanged = { viewModel.onPasswordChanged(it) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        ErrorPasswordTextField(
            hasError = !viewModel.isConfirmedPasswordValid.value,
            text = viewModel.confirmedPassword.value,
            placeholder = stringResource(R.string.confirm_password),
            onTextChanged = { viewModel.onConfirmedPasswordChanged(it) }
        )
        Spacer(modifier = Modifier.height(32.dp))
        RegisterButton(enabled = viewModel.isEnabled.value, onRegister = onRegister)
    }
    LoadingComponent(viewModel.isLoading.value)
}

@Composable
fun RegisterButton(enabled: Boolean = false, onRegister: () -> Unit) {
    Button(
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Blue,
            disabledBackgroundColor = Color.LightGray
        ),
        contentPadding = PaddingValues(16.dp),
        onClick = onRegister
    ) {
        Text(
            stringResource(R.string.create),
            style = MaterialTheme.typography.body1.copy(
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        )
    }
}