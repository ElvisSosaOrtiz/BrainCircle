package com.example.braincircle.view

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.GroupOff
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.braincircle.model.utils.DateUtils
import com.example.braincircle.ui.theme.BrainCircleTheme
import com.example.braincircle.view.common.ConfirmationModal
import com.example.braincircle.viewmodel.group_details.GroupDetailsUiState
import com.example.braincircle.viewmodel.group_details.GroupDetailsViewModel
import java.util.Date

@Composable
fun GroupDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: GroupDetailsViewModel = hiltViewModel(),
    navToMyGroups: () -> Unit,
    navToManageGroup: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    GroupDetailsScreenStateless(
        modifier = modifier,
        uiState = uiState,
        navToMyGroups = navToMyGroups,
        navToManageGroup = navToManageGroup,
        joinGroup = viewModel::joinGroup,
        leaveGroup = viewModel::leaveGroup,
        removeGroup = viewModel::removeGroup
    )
}

@Composable
private fun GroupDetailsScreenStateless(
    modifier: Modifier = Modifier,
    uiState: GroupDetailsUiState,
    navToMyGroups: () -> Unit = {},
    navToManageGroup: () -> Unit = {},
    joinGroup: (() -> Unit) -> Unit = {},
    leaveGroup: (() -> Unit) -> Unit = {},
    removeGroup: (() -> Unit) -> Unit = {}
) {
    var showConfirmationModal by remember { mutableStateOf(false) }
    var modalTitle by remember { mutableStateOf("") }
    var modalMessage by remember { mutableStateOf("") }
    var modalConfirmEvent by remember { mutableStateOf<(() -> Unit) -> Unit>({}) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Row(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.Info, contentDescription = null)
                Spacer(Modifier.padding(horizontal = 4.dp))
                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
            Text(
                modifier = Modifier.padding(16.dp),
                text = uiState.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Row(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Meeting details",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Row(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val annotatedString = buildAnnotatedString {
                    if (uiState.locationLink == Uri.EMPTY) {
                        append(uiState.locationName)
                    } else {
                        withLink(
                            LinkAnnotation.Url(
                                url = uiState.locationLink.toString(),
                                styles = TextLinkStyles(
                                    style = SpanStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        textDecoration = TextDecoration.Underline
                                    )
                                )
                            )
                        ) {
                            append(uiState.locationName)
                        }
                    }
                }
                Icon(imageVector = Icons.Filled.LocationOn, contentDescription = null)
                Spacer(Modifier.padding(horizontal = 4.dp))
                Text(
                    text = annotatedString,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.CalendarMonth, contentDescription = null)
                Spacer(Modifier.padding(horizontal = 4.dp))
                Text(
                    text = DateUtils.formatMeetingDate(uiState.meetingDate),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary)
        ) {
            Text(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                text = "Other details",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = uiState.courseTitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = uiState.courseCode,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Text(
                modifier = Modifier.padding(bottom = 16.dp, start = 16.dp),
                text = uiState.courseDept,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        if (uiState.isAdmin) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                FilledIconButton(
                    onClick = {
                        modalTitle = "Remove group?"
                        modalMessage = "Are you sure you want to remove this group?"
                        modalConfirmEvent = removeGroup
                        showConfirmationModal = true
                    },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
                }
                Spacer(Modifier.padding(horizontal = 8.dp))
                FilledIconButton(
                    onClick = navToManageGroup,
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                }
            }
        } else {
            if (uiState.isInGroup) {
                FilledIconButton(
                    onClick = {
                        modalTitle = "Leave group?"
                        modalMessage = "Are you sure you want to leave this group?"
                        modalConfirmEvent = leaveGroup
                        showConfirmationModal = true
                    },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(imageVector = Icons.Filled.GroupOff, contentDescription = null)
                }
            } else {
                FilledIconButton(
                    onClick = { joinGroup(navToMyGroups) },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(imageVector = Icons.Filled.Group, contentDescription = null)
                }
            }
        }

        if (showConfirmationModal) {
            ConfirmationModal(
                title = modalTitle,
                message = modalMessage,
                onDismiss = { showConfirmationModal = false },
                onConfirm = { modalConfirmEvent(navToMyGroups) }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7FBF1)
@Composable
fun GroupDetailsScreenLightPreview() {
    BrainCircleTheme {
        GroupDetailsScreenStateless(
            uiState = GroupDetailsUiState(
                courseCode = "CODE-123",
                courseTitle = "Course Title",
                courseDept = "Course Department",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                locationName = "Location of meeting or class",
                meetingDate = Date(),
                isAdmin = true,
                isInGroup = false
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF10140F)
@Composable
fun GroupDetailsScreenDarkPreview() {
    BrainCircleTheme(darkTheme = true) {
        GroupDetailsScreenStateless(
            uiState = GroupDetailsUiState(
                courseCode = "CODE-123",
                courseTitle = "Course Title",
                courseDept = "Course Department",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                locationName = "Location of meeting or class",
                meetingDate = Date(),
                isAdmin = true,
                isInGroup = false
            )
        )
    }
}