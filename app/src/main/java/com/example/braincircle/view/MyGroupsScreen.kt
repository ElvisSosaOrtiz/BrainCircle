package com.example.braincircle.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.braincircle.model.data.StudyGroup
import com.example.braincircle.ui.theme.BrainCircleTheme
import com.example.braincircle.viewmodel.my_groups.MyGroupsUiState
import com.example.braincircle.viewmodel.my_groups.MyGroupsViewModel

@Composable
fun MyGroupsScreen(
    modifier: Modifier = Modifier,
    viewModel: MyGroupsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MyGroupsStateless(
        modifier = modifier,
        uiState = uiState
    )
}

@Composable
fun MyGroupsStateless(
    modifier: Modifier = Modifier,
    uiState: MyGroupsUiState
) {
    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (uiState.myGroups.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "You have no saved groups",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            items(uiState.myGroups) { group ->
                MyGroupElement(
                    modifier = Modifier.padding(4.dp),
                    group = group
                )
            }
        }
    }
}

@Composable
fun MyGroupElement(
    modifier: Modifier = Modifier,
    group: StudyGroup
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = group.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "00:00",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.outline
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.size(width = 300.dp, height = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sender Name: Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = CircleShape
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    text = "1",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7FBF1)
@Composable
fun MyGroupsScreenLightPreview() {
    BrainCircleTheme {
//        MyGroupElement(group = StudyGroup())
        MyGroupsStateless(
            uiState = MyGroupsUiState(
                myGroups = listOf(
                    StudyGroup(name = "Group name"),
                    StudyGroup(name = "Group name"),
                    StudyGroup(name = "Group name"),
                    StudyGroup(name = "Group name"),
                    StudyGroup(name = "Group name")
                )
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF10140F)
@Composable
fun MyGroupsScreenDarkPreview() {
    BrainCircleTheme(darkTheme = true) {
//        MyGroupElement(group = StudyGroup())
        MyGroupsStateless(
            uiState = MyGroupsUiState(
                myGroups = listOf(
                    StudyGroup(name = "Group name"),
                    StudyGroup(name = "Group name"),
                    StudyGroup(name = "Group name"),
                    StudyGroup(name = "Group name"),
                    StudyGroup(name = "Group name")
                )
            )
        )
    }
}