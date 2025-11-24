@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.braincircle.view

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.braincircle.R
import com.example.braincircle.ui.theme.BrainCircleTheme
import com.example.braincircle.viewmodel.brain_circle.BrainCircleUiState
import com.example.braincircle.viewmodel.brain_circle.BrainCircleViewModel
import kotlinx.coroutines.launch

enum class BrainCircleScreen(@param:StringRes val title: Int) {
    SignIn(title = R.string.sign_in_title),
    SignUp(title = R.string.sign_up_title),
    ResetPassword(title = R.string.reset_password_title),
    FindGroups(title = R.string.app_name),
    GroupDetails(title = R.string.group_details)
}

@Composable
fun BrainCircleAppBar(
    modifier: Modifier = Modifier,
    currentScreen: BrainCircleScreen,
    canNavigateUp: Boolean,
    navigateUp: () -> Unit,
    groupName: String = "",
    onNavDrawerClick: () -> Unit = {},
    isInGroupsListScreen: Boolean = false
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
    if (isSearch) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .windowInsetsPadding(TopAppBarDefaults.windowInsets),
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
        CenterAlignedTopAppBar(
            modifier = modifier,
            title = {
                Text(stringResource(currentScreen.title, groupName))
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
                if (isInGroupsListScreen) {
                    IconButton(onClick = onNavDrawerClick) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(R.string.navigation_drawer)
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
        )
    }
}

@Composable
fun NavigationDrawerContent(
    modifier: Modifier = Modifier,
    uiState: BrainCircleUiState,
    onSignOutNavDrawerItemClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    val username = uiState.username ?: ""
    val photoUrl = uiState.photoUrl ?: ""
    var showSignOutDialog by remember { mutableStateOf(false) }

    ModalDrawerSheet(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (photoUrl.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        val usernameSplit = username.split(" ")
                        val initials = if (usernameSplit.size > 1) {
                            usernameSplit
                                .mapNotNull { it.firstOrNull()?.toString() }
                                .reduce { acc, s -> acc + s }
                        } else {
                            "${username.getOrNull(0)}${username.getOrNull(1)}"
                        }

                        Text(
                            text = initials,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                } else {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(photoUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = stringResource(R.string.user_profile_picture),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(Modifier.padding(horizontal = 4.dp))
                Text(text = username)
            }
            Spacer(Modifier.padding(top = 12.dp))
            HorizontalDivider()
            NavigationDrawerItem(
                label = { Text(text = stringResource(R.string.sign_out)) },
                selected = false,
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.Logout,
                        contentDescription = stringResource(R.string.sign_out)
                    )
                },
                onClick = {
                    onSignOutNavDrawerItemClick()
                    showSignOutDialog = true
                }
            )
        }
    }

    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                FilledTonalButton(
                    onClick = {
                        onSignOutClick()
                        showSignOutDialog = false
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )

                ) { Text(text = stringResource(R.string.sign_out)) }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showSignOutDialog = false },
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) { Text(text = stringResource(R.string.cancel)) }
            },
            title = { Text(text = stringResource(R.string.sign_out)) },
            text = { Text(text = stringResource(R.string.sign_out_confirmation_question)) },
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BrainCircleApp(
    modifier: Modifier = Modifier,
    viewModel: BrainCircleViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentScreen = BrainCircleScreen.valueOf(
        backStackEntry?.destination?.route ?: BrainCircleScreen.SignIn.name
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = if (uiState.isUserSignedIn) BrainCircleScreen.FindGroups.name else BrainCircleScreen.SignIn.name
    ) {
        val navigateToFindGroups = {
            navController.navigate(BrainCircleScreen.FindGroups.name) {
                popUpTo(BrainCircleScreen.SignIn.name) { inclusive = true }
                launchSingleTop = true
            }
        }
        composable(route = BrainCircleScreen.SignIn.name) {
            SignInScreen(
                onSignInClick = navigateToFindGroups,
                onSignInWithGoogleClick = navigateToFindGroups,
                onSignUpClick = { navController.navigate(BrainCircleScreen.SignUp.name) },
                onResetPasswordClick = { navController.navigate(BrainCircleScreen.ResetPassword.name) },
                topBar = {
                    BrainCircleAppBar(
                        currentScreen = currentScreen,
                        canNavigateUp = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() },
                        isInGroupsListScreen = currentScreen.name == BrainCircleScreen.FindGroups.name
                    )
                }
            )
        }
        composable(route = BrainCircleScreen.SignUp.name) {
            SignUpScreen(
                onCreateAccountClick = {
                    viewModel.reloadUser()
                    navigateToFindGroups()
                },
                onBackToSignInClick = {
                    navController.navigate(BrainCircleScreen.SignIn.name) {
                        popUpTo(BrainCircleScreen.SignUp.name) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                topBar = {
                    BrainCircleAppBar(
                        currentScreen = currentScreen,
                        canNavigateUp = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() },
                        isInGroupsListScreen = currentScreen.name == BrainCircleScreen.FindGroups.name
                    )
                }
            )
        }
        composable(route = BrainCircleScreen.ResetPassword.name) {
            ResetPasswordScreen(
                onBackToSignInClick = {
                    navController.navigate(BrainCircleScreen.SignIn.name) {
                        popUpTo(BrainCircleScreen.ResetPassword.name) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                topBar = {
                    BrainCircleAppBar(
                        currentScreen = currentScreen,
                        canNavigateUp = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() },
                        isInGroupsListScreen = currentScreen.name == BrainCircleScreen.FindGroups.name
                    )
                }
            )
        }
        composable(route = BrainCircleScreen.FindGroups.name) {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    NavigationDrawerContent(
                        uiState = uiState,
                        onSignOutNavDrawerItemClick = { scope.launch { drawerState.close() } },
                        onSignOutClick = {
                            viewModel.signOut()
                            navController.navigate(BrainCircleScreen.SignIn.name) {
                                popUpTo(BrainCircleScreen.FindGroups.name) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            ) {
                Scaffold(
                    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        BrainCircleAppBar(
                            currentScreen = currentScreen,
                            canNavigateUp = navController.previousBackStackEntry != null,
                            navigateUp = { navController.navigateUp() },
                            isInGroupsListScreen = currentScreen.name == BrainCircleScreen.FindGroups.name,
                            onNavDrawerClick = {
                                scope.launch {
                                    drawerState.apply { if (isClosed) open() else close() }
                                }
                            }
                        )
                    }
                ) {
                    FindGroupsScreen(
                        onGroupClick = { groupId, groupName ->
                            navController.navigate("${BrainCircleScreen.GroupDetails.name}/$groupId/$groupName")
                        }
                    )
                }
            }
        }
        composable(
            route = "${BrainCircleScreen.GroupDetails.name}/{groupId}/{groupName}",
            arguments = listOf(
                navArgument("groupId") { type = NavType.StringType },
                navArgument("groupName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
            val groupName = backStackEntry.arguments?.getString("groupName") ?: ""

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    NavigationDrawerContent(
                        uiState = uiState,
                        onSignOutNavDrawerItemClick = { scope.launch { drawerState.close() } },
                        onSignOutClick = {
                            viewModel.signOut()
                            navController.navigate(BrainCircleScreen.SignIn.name) {
                                popUpTo(BrainCircleScreen.FindGroups.name) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            ) {
                Scaffold(
                    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        BrainCircleAppBar(
                            currentScreen = currentScreen,
                            canNavigateUp = navController.previousBackStackEntry != null,
                            navigateUp = { navController.navigateUp() },
                            groupName = groupName,
                            isInGroupsListScreen = currentScreen.name == BrainCircleScreen.FindGroups.name,
                            onNavDrawerClick = {
                                scope.launch {
                                    drawerState.apply { if (isClosed) open() else close() }
                                }
                            }
                        )
                    }
                ) {
                    GroupDetailsScreen(groupId = groupId)
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BrainCircleAppPreview() {
    BrainCircleTheme {
        NavigationDrawerContent(
            uiState = BrainCircleUiState(username = "Elvis Sosa"),
            onSignOutNavDrawerItemClick = { },
            onSignOutClick = { }
        )
    }
}