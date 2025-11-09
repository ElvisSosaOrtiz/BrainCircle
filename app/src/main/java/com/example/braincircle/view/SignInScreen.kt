package com.example.braincircle.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.braincircle.view.common.PasswordField

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onResetPasswordClick: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

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
            nextIsPassword = true,
            onValueChange = { email = it }
        )
        Spacer(Modifier.padding(vertical = 8.dp))
        PasswordField(
            value = password,
            nextIsPasswordRepeat = false,
            label = R.string.password,
            onValueChange = { password = it }
        )
        Row(Modifier.padding(top = 8.dp)) {
            Text(
                text = stringResource(R.string.forgot_password).uppercase(),
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                modifier = Modifier.clickable(onClick = onResetPasswordClick),
                text = stringResource(R.string.reset_here).uppercase(),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(Modifier.padding(vertical = 16.dp))
        FilledTonalButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            colors = ButtonDefaults.filledTonalButtonColors(MaterialTheme.colorScheme.onPrimaryContainer),
            elevation = ButtonDefaults.filledTonalButtonElevation(8.dp),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            onClick = onSignInClick
        ) {
            Text(
                text = stringResource(R.string.sign_in),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer),
            onClick = onSignUpClick
        ) {
            Text(
                text = stringResource(R.string.sign_up),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(Modifier.padding(vertical = 16.dp))
        Text(
            text = stringResource(R.string.or_connect_with).uppercase(),
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.padding(vertical = 16.dp))
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
                    text = stringResource(R.string.google),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}