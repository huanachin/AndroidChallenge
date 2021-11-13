package com.example.androidchallenge.ui.pages.task

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.androidchallenge.R
import com.example.androidchallenge.ui.custom.LoadingComponent
import com.example.androidchallenge.ui.custom.NormalTextField
import com.example.androidchallenge.ui.theme.AndroidChallengeTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@Preview(showBackground = true, widthDp = 400)
@Composable
fun TaskPreview() {
    AndroidChallengeTheme {
        TaskPage(navController = rememberNavController())
    }
}

@ExperimentalCoroutinesApi
@Composable
fun TaskPage(
    navController: NavController,
    viewModel: TaskViewModel = hiltViewModel()
) {

    val state = viewModel.eventsFlow.collectAsState(initial = null)
    val event = state.value

    LaunchedEffect(event) {
        when (event) {
            is TaskEvent.NavigateBack -> {
                navController.navigateUp()
            }
            is TaskEvent.ShowError -> {

            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()

    ) {
        it?.let { uri ->
            viewModel.addImage(TaskImage.TaskImageLocal(uri))
        }
    }

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
                        navController.navigateUp()
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
            Box(Modifier.height(300.dp)) {
                ImagesBox(
                    images = viewModel.images,
                    onAdd = { launcher.launch("image/*") },
                    onDelete = { viewModel.removeImage(it) }
                )
            }
            Spacer(Modifier.height(16.dp))
            SaveButton(
                enabled = viewModel.isEnabled.value,
                onSave = { viewModel.onSave() })
        }
    }
    LoadingComponent(viewModel.isLoading.value)
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
fun ImagesBox(images: List<TaskImage>, onAdd: () -> Unit, onDelete: (Int) -> Unit) {
    if (images.isNotEmpty()) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            itemsIndexed(images) { index, task ->
                Box(
                    Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                ) {

                    val painter = when (task) {
                        is TaskImage.TaskImageLocal -> rememberImagePainter(data = task.uri)
                        is TaskImage.TaskImageNetwork -> rememberImagePainter(data = task.url)
                    }

                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                    Icon(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .clickable {
                                onDelete(index)
                            },
                        imageVector = Icons.Filled.Cancel,
                        contentDescription = null,
                        tint = Color.Black
                    )

                }
            }
            item {
                Spacer(Modifier.height(8.dp))
                AddImageBox(height = 100.dp, onAdd = onAdd)
            }
        }
    } else {
        AddImageBox(height = 300.dp, onAdd = onAdd)
    }

}

@Composable
fun AddImageBox(height: Dp = 200.dp, onAdd: () -> Unit) {

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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable {
                onAdd()
            }
        ) {
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