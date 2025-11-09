package com.example.braincircle.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.braincircle.R
import com.example.braincircle.view.common.EmailField

@Composable
fun ResetPasswordScreen(
    modifier: Modifier = Modifier,
    onResetPasswordClick: () -> Unit,
    onBackToSignInClick: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.primaryContainer),
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
        EmailField(
            value = email,
            nextIsPassword = false,
            onValueChange = { email = it }
        )
        Spacer(Modifier.padding(vertical = 16.dp))
        FilledTonalButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            colors = ButtonDefaults.filledTonalButtonColors(MaterialTheme.colorScheme.onPrimaryContainer),
            elevation = ButtonDefaults.filledTonalButtonElevation(8.dp),
            onClick = onResetPasswordClick
        ) {
            Text(
                text = stringResource(R.string.reset_password),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer),
            onClick = onBackToSignInClick
        ) {
            Text(
                text = stringResource(R.string.back_to_sign_in),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}