package com.example.braincircle.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.braincircle.ui.theme.BrainCircleTheme
import com.example.braincircle.viewmodel.create_group.CreateGroupUiState
import com.example.braincircle.viewmodel.create_group.CreateGroupViewModel

@Composable
fun CreateGroupScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateGroupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CreateGroupScreenStateless(
        modifier = modifier,
        uiState = uiState
    )
}

@Composable
fun CreateGroupScreenStateless(
    modifier: Modifier = Modifier,
    uiState: CreateGroupUiState
) {

}

@Preview(showBackground = true, backgroundColor = 0xFFF7FBF1)
@Composable
fun CreateGroupScreenLightPreview() {
    BrainCircleTheme {
        CreateGroupScreenStateless(uiState = CreateGroupUiState())
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF10140F)
@Composable
fun CreateGroupScreenDarkPreview() {
    BrainCircleTheme(darkTheme = true) {
        CreateGroupScreenStateless(uiState = CreateGroupUiState())
    }
}