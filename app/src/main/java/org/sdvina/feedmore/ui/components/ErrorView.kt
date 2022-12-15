package org.sdvina.feedmore.ui.components

import android.content.res.Configuration
import android.webkit.WebViewClient.ERROR_HOST_LOOKUP
import android.webkit.WebViewClient.ERROR_TIMEOUT
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sdvina.feedmore.R
import org.sdvina.feedmore.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorView(
    modifier: Modifier = Modifier,
    errorCode: Int,
    description: String?,
    onRetry: () -> Unit = {},
) {
    Scaffold{ innerPadding ->
        Column(
            modifier= modifier.padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                modifier = Modifier
                    .padding(start = 32.dp, end = 32.dp)
                    .fillMaxWidth(),
                painter = painterResource(
                    id = R.drawable.ic_undraw_location_search_modified
                ),
                contentDescription = stringResource(R.string.cd_error_webpage),
            )
            Spacer(Modifier.height(32.dp))
            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                text = when (errorCode) {
                    ERROR_HOST_LOOKUP -> stringResource(R.string.error_host_lookup)
                    ERROR_TIMEOUT -> stringResource(R.string.error_timeout)
                    else -> description ?: stringResource(R.string.error_else)
                },
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
            )
            Button(
                modifier = Modifier.padding(top = 32.dp),
                onClick = { onRetry() }
            ) {
                Text(text = stringResource(R.string.try_again))
            }
        }
    }
}

@Preview
@Composable
fun ErrorViewPreview() {
    AppTheme {
        ErrorView(
            errorCode = ERROR_HOST_LOOKUP,
            description = "Webpage not available"
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ErrorViewPreviewDark() {
    AppTheme {
        ErrorView(
            errorCode = ERROR_HOST_LOOKUP,
            description = "Webpage not available"
        )
    }
}
