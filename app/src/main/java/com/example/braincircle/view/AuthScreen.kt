package com.example.braincircle.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.braincircle.ui.theme.BrainCircleTheme

@Composable
fun SignInScreen(modifier: Modifier = Modifier) {

}

@Composable
fun SignUpScreen(modifier: Modifier = Modifier) {

}

@Preview(showBackground = true, /*uiMode = Configuration.UI_MODE_NIGHT_YES*/)
@Composable
fun SignInPreview() {
    BrainCircleTheme {
        SignInScreen()
    }
}

//@Preview
//@Composable
//fun SignUpPreview() {
//    SignUpScreen()
//}