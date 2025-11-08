package com.example.braincircle.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.braincircle.R
import com.example.braincircle.view.common.EmailField
import com.example.braincircle.view.common.PasswordField

@Composable
fun SignUpScreen(modifier: Modifier = Modifier) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var repeatPassword by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        EmailField(
            value = email,
            onValueChange = { email = it }
        )
        Spacer(Modifier.padding(vertical = 8.dp))
        PasswordField(
            value = password,
            nextIsPasswordRepeat = true,
            label = R.string.password,
            onValueChange = { password = it }
        )
        Spacer(Modifier.padding(vertical = 8.dp))
        PasswordField(
            value = repeatPassword,
            nextIsPasswordRepeat = false,
            label = R.string.repeat_password,
            onValueChange = { repeatPassword = it }
        )
        Spacer(Modifier.padding(vertical = 16.dp))
        FilledTonalButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            colors = ButtonDefaults.filledTonalButtonColors(MaterialTheme.colorScheme.onPrimaryContainer),
            elevation = ButtonDefaults.filledTonalButtonElevation(8.dp),
            onClick = {}
        ) {
            Text(
                text = stringResource(R.string.sign_up),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}