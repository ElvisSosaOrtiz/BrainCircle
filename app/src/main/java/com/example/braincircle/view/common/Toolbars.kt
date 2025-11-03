@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.braincircle.view.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.braincircle.R

@Composable
fun BasicToolbar(@StringRes title: Int) {
    TopAppBar(
        title = { Text(stringResource(title)) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    )
}

@Composable
fun ActionToolbar(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    @DrawableRes endActionIcon: Int,
    endAction: () -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(title)) },
//        colors = TopAppBarDefaults.topAppBarColors(containerColor = toolbarColor()),
        actions = {
            Box(modifier) {
                IconButton(onClick = endAction) {
                    Icon(
                        painter = painterResource(endActionIcon),
                        contentDescription = stringResource(R.string.action)
                    )
                }
            }
        }
    )
}

//@Composable
//private fun toolbarColor(): Color {
//    return if (isSystemInDarkTheme()) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
//}
