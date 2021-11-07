package com.example.androidchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.androidchallenge.ui.navigation.Navigation
import com.example.androidchallenge.ui.theme.AndroidChallengeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidChallengeTheme {
                Navigation()
            }
        }
    }
}