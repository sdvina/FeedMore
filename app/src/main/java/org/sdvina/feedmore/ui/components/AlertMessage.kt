package org.sdvina.feedmore.ui.components

import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.sdvina.feedmore.ui.theme.AppTheme

@Composable
fun AlertMessage(
    modifier: Modifier = Modifier,
    message: String
) {
    Snackbar(
        modifier = modifier
    ) {
        Text(text = message)
    }
}

@Composable
fun AlertMessageWithConfirm(
    modifier: Modifier = Modifier,
    message: String
) {
    Snackbar(
        modifier = modifier
    ) {
        Text(text = message)
    }
}

@Preview
@Composable
fun AlertMessagePreview(){
    AppTheme() {
        AlertMessage(message = "This is a message for you")
    }
}