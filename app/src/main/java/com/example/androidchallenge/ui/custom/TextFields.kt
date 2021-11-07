package com.example.androidchallenge.ui.custom

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidchallenge.ui.theme.AndroidChallengeTheme


@Preview(showBackground = true)
@Composable
fun ErrorTextFieldPreview() {
    AndroidChallengeTheme {
        ErrorTextField(hasError = true, placeholder = "Usuario", text = "") {}
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorPasswordTextFieldPreview() {
    AndroidChallengeTheme {
        ErrorPasswordTextField(hasError = true, placeholder = "Contraseña", text = "") {}
    }
}

@Preview(showBackground = true)
@Composable
fun ValidTextFieldPreview() {
    AndroidChallengeTheme {
        ValidTextField(isValid = true, placeholder = "Usuario", text = "") {}
    }
}

@Preview(showBackground = true)
@Composable
fun NormalTextFieldPreview() {
    AndroidChallengeTheme {
        NormalTextField(placeholder = "Nombre de la tarea*", text = "") {}
    }
}

@Composable
fun ErrorTextField(
    hasError: Boolean = false,
    placeholder: String = "",
    text: String,
    onTextChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        label = null,
        isError = hasError,
        onValueChange = onTextChanged,
        placeholder = {
            Text(
                placeholder,
                style = MaterialTheme.typography.body1.copy(
                    color = Color.LightGray,
                    fontSize = 14.sp,
                )
            )
        },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.LightGray,
            unfocusedIndicatorColor = Color.LightGray,
            errorIndicatorColor = Color.Red,
            backgroundColor = Color.White
        ),
        trailingIcon = {
            if (hasError) Icon(
                imageVector = Icons.Filled.ErrorOutline,
                contentDescription = null,
                tint = Color.Red
            )
        }
    )
}

@Composable
fun ErrorPasswordTextField(
    hasError: Boolean = false,
    placeholder: String = "",
    text: String,
    onTextChanged: (String) -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = text,
        label = null,
        isError = hasError,
        onValueChange = onTextChanged,
        placeholder = {
            Text(
                placeholder,
                style = MaterialTheme.typography.body1.copy(
                    color = Color.LightGray,
                    fontSize = 14.sp,
                )
            )
        },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.LightGray,
            unfocusedIndicatorColor = Color.LightGray,
            errorIndicatorColor = Color.Red,
            backgroundColor = Color.White
        ),
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            if (hasError) Icon(
                imageVector = Icons.Filled.ErrorOutline,
                contentDescription = null,
                tint = Color.Red
            ) else {
                val image = if (visible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                IconButton(onClick = {
                    visible = !visible
                }) {
                    Icon(imageVector = image, null)
                }
            }
        }
    )
}

@Composable
fun ValidTextField(
    isValid: Boolean = false,
    placeholder: String = "",
    text: String,
    onTextChanged: (String) -> Unit
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isValid) Color.Green else Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            ),
        value = text,
        label = null,
        onValueChange = onTextChanged,
        placeholder = {
            Text(
                placeholder,
                style = MaterialTheme.typography.body1.copy(
                    color = Color.LightGray,
                    fontSize = 14.sp,
                )
            )
        },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = Color.White
        ),
        trailingIcon = {
            if (isValid) Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = Color.Green
            )
        }
    )
}

@Composable
fun NormalTextField(
    placeholder: String = "",
    text: String,
    padding: PaddingValues = PaddingValues(vertical = 16.dp),
    onTextChanged: (String) -> Unit
) {
    BasicTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = text,
        onValueChange = onTextChanged,
        singleLine = true,
        decorationBox = { innerTextField ->
            if (text.isEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(padding),
                    text = placeholder,
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic
                )
            }
            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(Modifier.padding(padding)) {
                    innerTextField()
                }
                Divider(
                    Modifier
                        .height(1.dp)
                        .fillMaxWidth(), color = Color.LightGray
                )
            }
        })
}