@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.braincircle.view

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.braincircle.R

enum class BrainCircleScreen(@param:StringRes val title: Int) {
    Start(title = R.string.app_name),
    SignIn(title = R.string.sign_in_title),
    SignUp(title = R.string.sign_up_title)
}

@Composable
fun BrainCircleAppBar(
    currentScreen: BrainCircleScreen,
    canNavigateUp: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            if (currentScreen != BrainCircleScreen.Start) {
                Text(stringResource(currentScreen.title))
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateUp) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun BrainCircleApp(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = BrainCircleScreen.valueOf(
        backStackEntry?.destination?.route ?: BrainCircleScreen.Start.name
    )

    Scaffold(
        topBar = {
            BrainCircleAppBar(
                currentScreen = currentScreen,
                canNavigateUp = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = BrainCircleScreen.Start.name,
            modifier = modifier
        ) {
            composable(route = BrainCircleScreen.Start.name) {
                StartScreen(
                    onSignInButtonClicked = { navController.navigate(BrainCircleScreen.SignIn.name) },
                    onSignUpButtonClicked = { navController.navigate(BrainCircleScreen.SignUp.name) }
                )
            }
            composable(route = BrainCircleScreen.SignIn.name) {
                SignInScreen()
            }
            composable(route = BrainCircleScreen.SignUp.name) {
                SignUpScreen()
            }
        }
    }
}