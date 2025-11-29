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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.braincircle.viewmodel.group_details.GroupDetailsUiState
import com.example.braincircle.viewmodel.group_details.GroupDetailsViewModel
import java.util.Date

@Composable
fun GroupDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: GroupDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    GroupDetailsScreenStateless(
        modifier = modifier,
        uiState = uiState
    )
}

@Composable
private fun GroupDetailsScreenStateless(
    modifier: Modifier = Modifier,
    uiState: GroupDetailsUiState
) {
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
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7FBF1)
@Composable
fun GroupDetailsScreenLightPreview() {
    BrainCircleTheme {
        GroupDetailsScreenStateless(
            uiState = GroupDetailsUiState(
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                courseCode = "CODE-123",
                courseTitle = "Course Title",
                courseDept = "Course Department",
                locationName = "Location of meeting or class",
                meetingDate = Date()
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
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                courseCode = "CODE-123",
                courseTitle = "Course Title",
                courseDept = "Course Department",
                locationName = "Location of meeting or class",
                meetingDate = Date()
            )
        )
    }
}