package dev.wxlf.connectrequest.request_ui

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import dev.wxlf.connectrequest.core.ui.theme.ConnectRequestTheme
import dev.wxlf.connectrequest.request_ui.ErrorOn.IDLE
import dev.wxlf.connectrequest.request_ui.ErrorOn.LoadStreets
import dev.wxlf.connectrequest.request_ui.elements.StreetTextField

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RequestScreen(viewModel: RequestViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var flag by rememberSaveable { mutableStateOf(false) }
    if (!flag) {
        LaunchedEffect(Unit) {
            viewModel.loadStreets()
            focusRequester.requestFocus()
            keyboardController?.show()
        }
        flag = true
    }

    RequestScreenContent(
        uiState = uiState,
        focusRequester = focusRequester,
        retry = {
            when (uiState.errorOn) {
                IDLE -> {}
                LoadStreets -> viewModel.loadStreets()
            }
        },
        searchStreets = viewModel::searchStreets
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RequestScreenContent(
    uiState: RequestUiState,
    focusRequester: FocusRequester,
    retry: () -> Unit,
    searchStreets: (String) -> Unit
) {
    if (uiState.isError)
        Dialog(onDismissRequest = retry) {
            Card {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(uiState.errorMsg, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(2.dp))
                    Button(onClick = retry) {
                        Text(stringResource(R.string.retry))
                    }
                }
            }
        }

    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = { focusManager.clearFocus() }
            )
        },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.request_title)) },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        var street by remember { mutableStateOf("") }
        var streetsMenu by remember { mutableStateOf(false) }
        Column(
            Modifier
                .padding(paddingValues)
                .padding(vertical = 8.dp, horizontal = 16.dp),
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(2.dp, Color(0xFFEDEDED))
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    StreetTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        value = street,
                        onValueChange = {
                            street = it
                            searchStreets(it)
                            if (it.length >= 3)
                                streetsMenu = true
                        },
                        placeholder = { Text(stringResource(R.string.choose_street)) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                        ),
                        singleLine = true
                    )

                    DropdownMenu(
                        modifier = Modifier.background(Color.White),
                        expanded = streetsMenu,
                        onDismissRequest = { streetsMenu = false },
                        properties = PopupProperties()
                    ) {
                        uiState.streets.forEach {
                            DropdownMenuItem(
                                text = { Text(it.street) },
                                onClick = {
                                    street = it.street
                                    focusManager.moveFocus(FocusDirection.Next)
                                    streetsMenu = false
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp)
                            )
                        }
                    }

                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { /*TODO*/ },
                enabled = false,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color(0xFFA5A5AA),
                    disabledContentColor = Color(0xFFF5FAFA)
                )
            ) {
                Text(stringResource(R.string.send_button))
            }

        }
    }
}

@Preview(
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
    device = "spec:width=392.7dp,height=850.9dp,dpi=440",
)
@Composable
private fun RequestPreview() {
    ConnectRequestTheme {
        Surface {
            RequestScreenContent(
                uiState = RequestUiState(),
                focusRequester = FocusRequester(),
                retry = {},
                searchStreets = {}
            )
        }
    }
}