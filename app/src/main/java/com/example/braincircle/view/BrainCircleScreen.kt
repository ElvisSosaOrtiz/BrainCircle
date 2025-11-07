@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.braincircle.view

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.braincircle.R
import kotlin.math.roundToInt

enum class BrainCircleScreen(@param:StringRes val title: Int) {
    Start(title = R.string.app_name),
    SignIn(title = R.string.sign_in_title),
    SignUp(title = R.string.sign_up_title),
    FindGroups(title = R.string.find_groups_title)
}

@Composable
fun BrainCircleAppBar(
    currentScreen: BrainCircleScreen,
    canNavigateUp: Boolean,
    navigateUp: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    isInGroupsListScreen: Boolean,
    modifier: Modifier = Modifier
) {
    var isSearch by remember { mutableStateOf(false) }
    var value by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(isSearch) {
        if (isSearch) {
            focusRequester.requestFocus()
            keyboardController?.show()
        } else {
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    }

    Crossfade(
        modifier = Modifier.animateContentSize(),
        targetState = isSearch,
        label = "Search"
    ) { target ->
        if (target) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .windowInsetsPadding(TopAppBarDefaults.windowInsets)
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        val height =
                            placeable.height * (1 - scrollBehavior.state.collapsedFraction)
                        layout(placeable.width, height.roundToInt()) {
                            placeable.place(0, 0)
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(size = 50.dp),
                    value = value,
                    placeholder = { Text(text = "Search a group") },
                    onValueChange = { value = it },
                    leadingIcon = {
                        IconButton(onClick = { isSearch = !isSearch }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    trailingIcon = {
                        if (value.isNotBlank()) {
                            IconButton(onClick = { value = "" }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            isSearch = !isSearch
                        }
                    )
                )
            }
        } else {
            TopAppBar(
                modifier = modifier,
                title = {
                    if (currentScreen != BrainCircleScreen.Start) {
                        Text(stringResource(currentScreen.title))
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                navigationIcon = {
                    if (canNavigateUp) {
                        IconButton(onClick = navigateUp) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back_button)
                            )
                        }
                    }
                },
                actions = {
                    if (isInGroupsListScreen) {
                        IconButton(onClick = { isSearch = !isSearch }) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    }
}

@Composable
fun BrainCircleApp(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = BrainCircleScreen.valueOf(
        backStackEntry?.destination?.route ?: BrainCircleScreen.Start.name
    )

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            BrainCircleAppBar(
                currentScreen = currentScreen,
                canNavigateUp = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                scrollBehavior = scrollBehavior,
                isInGroupsListScreen = currentScreen.name == BrainCircleScreen.FindGroups.name
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = BrainCircleScreen.Start.name
        ) {
            composable(route = BrainCircleScreen.Start.name) {
                StartScreen(
                    onSignInButtonClick = { navController.navigate(BrainCircleScreen.SignIn.name) },
                    onSignUpButtonClick = { navController.navigate(BrainCircleScreen.SignUp.name) }
                )
            }
            composable(route = BrainCircleScreen.SignIn.name) {
                SignInScreen(
                    onSignInClick = {
                        navController.navigate(BrainCircleScreen.FindGroups.name) {
                            popUpTo(BrainCircleScreen.Start.name) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(route = BrainCircleScreen.SignUp.name) {
                SignUpScreen()
            }
            composable(route = BrainCircleScreen.FindGroups.name) {
                FindGroupsScreen()
            }
        }
    }
}