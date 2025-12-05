package com.example.braincircle.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.braincircle.model.utils.DateUtils
import com.example.braincircle.ui.theme.BrainCircleTheme
import com.example.braincircle.view.common.ConfirmationModal
import com.example.braincircle.view.common.SequentialDateTimePicker
import com.example.braincircle.viewmodel.manage_group.ManageGroupUiState
import com.example.braincircle.viewmodel.manage_group.ManageGroupViewModel
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun ManageGroupScreen(
    modifier: Modifier = Modifier,
    viewModel: ManageGroupViewModel = hiltViewModel(),
    topBar: @Composable () -> Unit,
    navToGroupDetails: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = topBar
    ) { paddingValues ->
        ManageGroupScreenStateless(
            modifier = modifier.padding(paddingValues),
            uiState = uiState,
            onNameChange = viewModel::onNameChange,
            onCourseCodeChange = viewModel::onCourseCodeChange,
            onCourseTitleChange = viewModel::onCourseTitleChange,
            onCourseDeptChange = viewModel::onCourseDeptChange,
            onDescriptionChange = viewModel::onDescriptionChange,
            onLocationNameChange = viewModel::onLocationNameChange,
            onLocationLinkChange = viewModel::onLocationLinkChange,
            onMeetingDateChange = viewModel::onMeetingDateChange,
            onEditGroupClick = { viewModel.updateGroup(navToGroupDetails) },
            onRemoveGroupClick = { viewModel.removeGroup(navToGroupDetails) }
        )
        LaunchedEffect(uiState.errorMessage) {
            if (uiState.errorMessage.isNotEmpty()) {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = uiState.errorMessage,
                        actionLabel = "Dismiss",
                        duration = SnackbarDuration.Short
                    )
                }
                viewModel.clearMessages()
            }
        }
    }
}

@Composable
fun ManageGroupScreenStateless(
    modifier: Modifier = Modifier,
    uiState: ManageGroupUiState,
    onNameChange: (String) -> Unit = {},
    onCourseCodeChange: (String) -> Unit = {},
    onCourseTitleChange: (String) -> Unit = {},
    onCourseDeptChange: (String) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onLocationNameChange: (String) -> Unit = {},
    onLocationLinkChange: (String) -> Unit = {},
    onMeetingDateChange: (Date) -> Unit = {},
    onEditGroupClick: () -> Unit = {},
    onRemoveGroupClick: () -> Unit = {}
) {
    var showDateTimePicker by remember { mutableStateOf(false) }
    var showConfirmationModal by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 8.dp),
            value = uiState.name,
            onValueChange = { onNameChange(it) },
            label = { Text(text = "Group name") },
            singleLine = true,
            enabled = !uiState.isLoading,
            shape = RoundedCornerShape(50.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 8.dp),
            value = uiState.courseTitle,
            onValueChange = { onCourseTitleChange(it) },
            label = { Text(text = "Course title") },
            singleLine = true,
            enabled = !uiState.isLoading,
            shape = RoundedCornerShape(50.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 8.dp),
            value = uiState.courseCode,
            onValueChange = { onCourseCodeChange(it) },
            label = { Text(text = "Course code") },
            singleLine = true,
            enabled = !uiState.isLoading,
            shape = RoundedCornerShape(50.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 8.dp),
            value = uiState.courseDept,
            onValueChange = { onCourseDeptChange(it) },
            label = { Text(text = "Course department") },
            singleLine = true,
            enabled = !uiState.isLoading,
            shape = RoundedCornerShape(50.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 8.dp),
            value = uiState.description,
            onValueChange = { onDescriptionChange(it) },
            label = { Text(text = "Description") },
            singleLine = false,
            enabled = !uiState.isLoading,
            shape = RoundedCornerShape(50.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 8.dp),
            value = uiState.locationName,
            onValueChange = { onLocationNameChange(it) },
            label = { Text(text = "Location name") },
            singleLine = true,
            enabled = !uiState.isLoading,
            shape = RoundedCornerShape(50.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 8.dp),
            value = uiState.locationLink.toString(),
            onValueChange = { onLocationLinkChange(it) },
            label = { Text(text = "Location link") },
            singleLine = true,
            enabled = !uiState.isLoading,
            shape = RoundedCornerShape(50.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Uri
            )
        )
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            onClick = { showDateTimePicker = true },
            enabled = !uiState.isLoading
        ) {
            Text(
                text = DateUtils.formatMeetingDate(uiState.meetingDate)
                    .ifBlank { "Schedule meeting" })
        }
        FilledTonalButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .padding(horizontal = 24.dp),
            onClick = onEditGroupClick,
            enabled = uiState.name.isNotBlank() && !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(text = "Edit group")
            }
        }
        FilledTonalButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            onClick = {},
            enabled = uiState.name.isNotBlank() && !uiState.isLoading,
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text(text = "Remove group")
        }

        SequentialDateTimePicker(
            show = showDateTimePicker,
            onDismiss = { showDateTimePicker = false },
            onConfirm = { date ->
                onMeetingDateChange(date)
                showDateTimePicker = false
            }
        )

        if (showConfirmationModal) {
            ConfirmationModal(
                title = "Remove group?",
                message = "Are you sure you want to remove this group?",
                onDismiss = { showConfirmationModal = false },
                onConfirm = onRemoveGroupClick
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7FBF1)
@Composable
fun ManageGroupScreenLightPreview() {
    BrainCircleTheme {
        ManageGroupScreenStateless(uiState = ManageGroupUiState())
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF10140F)
@Composable
fun ManageGroupScreenDarkPreview() {
    BrainCircleTheme(darkTheme = true) {
        ManageGroupScreenStateless(uiState = ManageGroupUiState())
    }
}