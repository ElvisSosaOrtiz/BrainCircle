package com.example.braincircle.view.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.braincircle.R

@Composable
fun EmailField(
    modifier: Modifier = Modifier,
    value: String,
    nextIsPassword: Boolean,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
        shape = RoundedCornerShape(50.dp),
        singleLine = true,
        enabled = enabled,
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(stringResource(R.string.email)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = stringResource(R.string.email)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = if (nextIsPassword) ImeAction.Next else ImeAction.Done
        )
    )
}

@Composable
fun PasswordField(
    modifier: Modifier = Modifier,
    value: String,
    nextIsPasswordRepeat: Boolean,
    @StringRes label: Int,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true
) {
    var isVisible by rememberSaveable { mutableStateOf(false) }

    val icon =
        if (isVisible) painterResource(R.drawable.ic_visibility_on)
        else painterResource(R.drawable.ic_visibility_off)

    val visualTransformation =
        if (isVisible) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
        shape = RoundedCornerShape(50.dp),
        enabled = enabled,
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(text = stringResource(label)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = stringResource(R.string.lock)) },
        trailingIcon = {
            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(painter = icon, contentDescription = stringResource(R.string.visibility))
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = if (nextIsPasswordRepeat) ImeAction.Next else ImeAction.Done
        ),
        visualTransformation = visualTransformation
    )
}