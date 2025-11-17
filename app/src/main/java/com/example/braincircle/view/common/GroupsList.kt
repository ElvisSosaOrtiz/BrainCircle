package com.example.braincircle.view.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.braincircle.ui.theme.BrainCircleTheme

@Composable
fun GroupsList(modifier: Modifier = Modifier) {

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        items(5) {
            GroupElement()
        }
    }
}

@Composable
fun GroupElement(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun GroupElementPreview() {
    BrainCircleTheme {
        GroupElement()
    }
}