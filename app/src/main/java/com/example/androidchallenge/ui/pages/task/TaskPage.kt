package com.example.androidchallenge.ui.pages.task

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.androidchallenge.R
import com.example.androidchallenge.data.model.TaskModel
import com.example.androidchallenge.ui.custom.NormalTextField
import com.example.androidchallenge.ui.theme.AndroidChallengeTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@Preview(showBackground = true, widthDp = 400)
@Composable
fun TaskPreview() {
    AndroidChallengeTheme {
        TaskPage(onCloseDialog = {})
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun ImageBoxPreview() {
    AndroidChallengeTheme {
        ImagesBox((1..2).map { "https://lorempixel.com/400/400/people/$it/" })
    }
}

@ExperimentalCoroutinesApi
@Composable
fun TaskPage(
    viewModel: TaskViewModel = viewModel(),
    task: TaskModel? = null,
    onCloseDialog: () -> Unit
) {


    Box(Modifier.background(color = Color.White)) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable {
                        onCloseDialog()
                    },
                imageVector = Icons.Filled.Cancel,
                contentDescription = null,
                tint = Color.Gray
            )
            Spacer(Modifier.height(8.dp))
            NormalTextField(
                placeholder = "${stringResource(R.string.task_name)}*",
                text = viewModel.name.value,
                onTextChanged = { viewModel.onNameChanged(it) }
            )
            Spacer(Modifier.height(8.dp))
            NormalTextField(
                placeholder = "${stringResource(R.string.description)}*",
                text = viewModel.description.value,
                onTextChanged = { viewModel.onDescriptionChanged(it) }
            )
            Spacer(Modifier.height(16.dp))
            ImagesBox(emptyList())
            Spacer(Modifier.height(16.dp))
            SaveButton(
                enabled = viewModel.isEnabled.value,
                onSave = {
                    if (task == null) {
                        viewModel.addTask(
                            onAddTaskError = onCloseDialog,
                            onAddTaskSuccess = onCloseDialog
                        )
                    } else {
                        viewModel.updateTask(
                            task = task,
                            onEditTaskError = onCloseDialog,
                            onEditTaskSuccess = onCloseDialog
                        )
                    }
                })
        }
    }
}

@Composable
fun SaveButton(enabled: Boolean = false, onSave: () -> Unit) {
    Button(
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Blue,
            disabledBackgroundColor = Color.LightGray
        ),
        contentPadding = PaddingValues(16.dp),
        onClick = onSave
    ) {
        Text(
            stringResource(R.string.save),
            style = MaterialTheme.typography.body1.copy(
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun ImagesBox(images: List<String>) {
    if (images.isNotEmpty()) {
        Column {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(images) {
                    Box(
                        Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                    ) {
                        Icon(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp),
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = rememberImagePainter(it),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            AddImageBox(height = 100.dp)
        }
    } else {
        AddImageBox()
    }
}

@Composable
fun AddImageBox(height: Dp = 200.dp) {
    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height), contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(color = Color.LightGray, style = stroke)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                modifier = Modifier.size(40.dp),
                imageVector = Icons.Default.Image,
                contentDescription = null,
                tint = Color.LightGray,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.add_images),
                style = MaterialTheme.typography.body1.copy(color = Color.LightGray)
            )
        }
    }
}