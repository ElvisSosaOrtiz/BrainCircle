package com.example.braincircle.view

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.braincircle.model.data.StudyGroup
import com.example.braincircle.ui.theme.BrainCircleTheme
import com.example.braincircle.viewmodel.find_groups.FindGroupsViewModel

@Composable
fun FindGroupsScreen(
    modifier: Modifier = Modifier,
    viewModel: FindGroupsViewModel = hiltViewModel(),
    onGroupClick: (String, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isLoading) {
            item { CircularProgressIndicator() }
        } else if (uiState.groups.isEmpty()) {
            item { Text(text = "There are no groups in this moment") }
        } else {
            items(uiState.groups) { group ->
                GroupCard(
                    modifier = Modifier.padding(8.dp),
                    group = group,
                    onGroupClick = onGroupClick
                )
            }
        }
    }
}

@Composable
fun GroupCard(
    modifier: Modifier = Modifier,
    group: StudyGroup,
    onGroupClick: (String, String) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = { onGroupClick(group.groupId, group.name) }),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = group.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.inversePrimary
                )
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = group.courseCode,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            Spacer(Modifier.padding(vertical = 8.dp))
            Text(
                text = group.courseTitle,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = group.courseDept,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FindGroupsScreenLightPreview() {
    BrainCircleTheme {
        GroupCard(
            group = StudyGroup(
                name = "Group name",
                courseCode = "Course code",
                courseTitle = "Course title",
                courseDept = "Course department"
            ),
            onGroupClick = { _, _ -> }
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun FindGroupsScreenDarkPreview() {
    BrainCircleTheme {
        GroupCard(
            group = StudyGroup(
                name = "Group name",
                courseCode = "Course code",
                courseTitle = "Course title",
                courseDept = "Course department"
            ),
            onGroupClick = { _, _ -> }
        )
    }
}