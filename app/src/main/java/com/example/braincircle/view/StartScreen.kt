package com.example.braincircle.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.braincircle.R
import com.example.braincircle.ui.theme.BrainCircleTheme

@Composable
fun StartScreen(
    onSignInButtonClicked: () -> Unit,
    onSignUpButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(150.dp),
            painter = painterResource(R.drawable.brain_circle_app_icon),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Column {
            FilledTonalButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                colors = ButtonDefaults.filledTonalButtonColors(MaterialTheme.colorScheme.onPrimaryContainer),
                elevation = ButtonDefaults.filledTonalButtonElevation(8.dp),
                onClick = onSignInButtonClicked
            ) {
                Text(
                    text = stringResource(R.string.sign_in),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            FilledTonalButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                colors = ButtonDefaults.filledTonalButtonColors(MaterialTheme.colorScheme.onPrimaryContainer),
                elevation = ButtonDefaults.filledTonalButtonElevation(8.dp),
                onClick = onSignUpButtonClicked
            ) {
                Text(
                    text = stringResource(R.string.sign_up),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            FilledTonalButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                colors = ButtonDefaults.filledTonalButtonColors(MaterialTheme.colorScheme.onPrimaryContainer),
                elevation = ButtonDefaults.filledTonalButtonElevation(8.dp),
                onClick = { }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.google__g__logo),
                        contentDescription = null
                    )
                    Spacer(Modifier.padding(horizontal = 4.dp))
                    Text(
                        text = stringResource(R.string.sign_in_with_google),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    BrainCircleTheme {
        StartScreen(
            onSignInButtonClicked = {},
            onSignUpButtonClicked = {}
        )
    }
}