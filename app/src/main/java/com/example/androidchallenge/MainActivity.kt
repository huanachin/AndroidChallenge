package com.example.androidchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.androidchallenge.ui.navigation.Navigation
import com.example.androidchallenge.ui.theme.AndroidChallengeTheme
import com.example.androidchallenge.ui.util.Constants.TASK_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val taskId = intent.data?.getQueryParameter(TASK_ID)
            AndroidChallengeTheme {
                Navigation(taskId)
            }
        }
    }
}