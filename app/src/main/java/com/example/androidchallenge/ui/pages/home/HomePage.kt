package com.example.androidchallenge.ui.pages.home

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.androidchallenge.R
import com.example.androidchallenge.data.model.TaskModel
import com.example.androidchallenge.ui.custom.LoadingComponent
import com.example.androidchallenge.ui.navigation.Screen
import com.example.androidchallenge.ui.theme.AndroidChallengeTheme
import com.example.androidchallenge.ui.util.Constants.TASK
import com.example.androidchallenge.ui.util.parseModel
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.net.URLEncoder

@ExperimentalCoroutinesApi
@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun HomePreview() {
    AndroidChallengeTheme {
        HomePage(navController = rememberNavController(), userId = "")
    }
}

@Preview(showBackground = true)
@Composable
fun DropDownPreview() {
    AndroidChallengeTheme {
        TaskMenuItem(text = stringResource(R.string.share), imageVector = Icons.Default.Share) {

        }
    }
}


@ExperimentalCoroutinesApi
@Composable
fun HomePage(
    navController: NavController,
    userId: String,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state = viewModel.eventsFlow.collectAsState(initial = null)
    val event = state.value

    LaunchedEffect(event) {
        when (event) {
            HomeEvent.ShowDeleteTaskError -> {

            }
            HomeEvent.ShowDeleteTaskSuccess -> {

            }
        }
    }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.currentBackStackEntry?.arguments?.putParcelable(TASK, null)
                    navController.navigate("${Screen.TaskDialog.route}/$userId")
                },
                backgroundColor = Color.Gray
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {

            Spacer(Modifier.height(16.dp))
            Row {
                Text(
                    text = stringResource(R.string.my_tasks),
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                IconButton(onClick = {
                    viewModel.logout()
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(Screen.HomeScreen.route) { inclusive = true }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
            Spacer(Modifier.height(32.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(viewModel.tasks) {
                    TaskItem(task = it,
                        onShare = {

                        }, onDelete = {
                            it.id?.let { id ->
                                viewModel.deleteTask(id)
                            }
                        }, onEdit = {
                            val taskEncoded = it.parseModel()
                            navController.navigate("${Screen.TaskDialog.route}/$userId?$TASK=$taskEncoded")
                        })
                }
            }
        }
        LoadingComponent(viewModel.isLoading.value)
    }
}

@Composable
fun TaskItem(
    task: TaskModel,
    onShare: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Box(
        Modifier.background(
            color = Color.LightGray,
            shape = RoundedCornerShape(4.dp)
        )
    ) {
        Row(
            Modifier.padding(start = 16.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = task.name.orEmpty(),
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
            )
            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    tint = Color.DarkGray
                )
                DropdownMenu(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    TaskMenuItem(
                        text = stringResource(R.string.share),
                        imageVector = Icons.Default.Share,
                        onClick = {
                            expanded = false
                            onShare()
                        }
                    )
                    TaskMenuItem(
                        text = stringResource(R.string.edit),
                        imageVector = Icons.Default.Edit,
                        onClick = {
                            expanded = false
                            onEdit()
                        }
                    )
                    TaskMenuItem(
                        text = stringResource(R.string.delete),
                        imageVector = Icons.Default.Delete,
                        onClick = {
                            expanded = false
                            onDelete()
                        }
                    )
                }
            }

        }
    }
}

@Composable
fun TaskMenuItem(text: String, imageVector: ImageVector, onClick: () -> Unit) {
    DropdownMenuItem(onClick = onClick) {
        Row {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = Color.DarkGray
            )
            Spacer(Modifier.width(8.dp))
            Text(text = text)
        }
    }
}