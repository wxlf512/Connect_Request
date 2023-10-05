package dev.wxlf.connectrequest.request_ui

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import dev.wxlf.connectrequest.core.ui.theme.ConnectRequestTheme
import dev.wxlf.connectrequest.request_ui.ErrorOn.IDLE
import dev.wxlf.connectrequest.request_ui.ErrorOn.LoadHouses
import dev.wxlf.connectrequest.request_ui.ErrorOn.LoadStreets
import dev.wxlf.connectrequest.request_ui.elements.LowerTextField

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
                LoadHouses -> viewModel.selectStreet(it)
            }
        },
        searchStreets = viewModel::searchStreets,
        selectStreet = viewModel::selectStreet
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RequestScreenContent(
    uiState: RequestUiState,
    focusRequester: FocusRequester,
    retry: (String) -> Unit,
    searchStreets: (String) -> Unit,
    selectStreet: (String) -> Unit
) {
    if (uiState.isError)
        Dialog(onDismissRequest = { retry("") }) {
            Card {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(uiState.errorMsg, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(2.dp))
                    Button(onClick = { retry("") }) {
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
        val chooseHouse = stringResource(R.string.choose_house)
        var street by rememberSaveable { mutableStateOf("") }
        var streetsMenu by rememberSaveable { mutableStateOf(false) }
        var streetChosenId by rememberSaveable { mutableStateOf("") }
        var houseChosenId by rememberSaveable { mutableStateOf("") }
        var houseChosen by rememberSaveable {
            mutableStateOf(chooseHouse)
        }
        var houseNum by rememberSaveable { mutableStateOf("") }
        var houseBuildNum by rememberSaveable { mutableStateOf("") }
        var flatNum by rememberSaveable { mutableStateOf("") }


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
                    Box {
                        LowerTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            value = street,
                            onValueChange = {
                                street = it
                                searchStreets(it)
                                streetChosenId = ""
                                houseChosen = chooseHouse
                                houseChosenId = ""
                                if (it.length >= 3)
                                    streetsMenu = true
                            },
                            placeholder = { Text(stringResource(R.string.choose_street)) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(onNext = {
                                focusManager.moveFocus(FocusDirection.Next)
                            })
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
                                        selectStreet(it.streetId)
                                        streetChosenId = it.streetId
                                    },
                                    contentPadding = PaddingValues(horizontal = 8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (streetChosenId.isNotEmpty()) {
                        var houseMenu by rememberSaveable { mutableStateOf(false) }
                        LowerTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    houseMenu = !houseMenu
                                },
                            value = houseChosen,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = stringResource(R.string.house_chooser)
                                )
                            },
                            enabled = false,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                            ),
                            singleLine = true
                        )

                        DropdownMenu(
                            modifier = Modifier.background(Color.White),
                            expanded = houseMenu,
                            onDismissRequest = { houseMenu = false },
                            properties = PopupProperties()
                        ) {
                            uiState.houses.forEach {
                                DropdownMenuItem(text = { Text(it.house) }, onClick = {
                                    houseChosen = it.house
                                    houseChosenId = it.houseId
                                    houseMenu = false
                                })
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (houseChosenId.isEmpty()) {
                            OutlinedTextField(
                                value = houseNum,
                                onValueChange = { houseNum = it },
                                placeholder = {
                                    Text(
                                        text = stringResource(R.string.house),
                                        fontSize = 14.sp
                                    )
                                },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                textStyle = TextStyle(fontSize = 14.sp),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next,
                                    keyboardType = KeyboardType.Number
                                ),
                                keyboardActions = KeyboardActions(onNext = {
                                    focusManager.moveFocus(FocusDirection.Next)
                                })
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            OutlinedTextField(
                                value = houseBuildNum,
                                onValueChange = { houseBuildNum = it },
                                placeholder = {
                                    Text(
                                        text = stringResource(R.string.house_build),
                                        fontSize = 14.sp
                                    )
                                },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                textStyle = TextStyle(fontSize = 14.sp),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next,
                                    keyboardType = KeyboardType.Number
                                ),
                                keyboardActions = KeyboardActions(onNext = {
                                    focusManager.moveFocus(FocusDirection.Next)
                                })
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                        }

                        OutlinedTextField(
                            value = flatNum,
                            onValueChange = { flatNum = it },
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.flat),
                                    fontSize = 14.sp
                                )
                            },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            textStyle = TextStyle(fontSize = 14.sp),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Number
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                focusManager.clearFocus()
                            })
                        )
                    }
                }
            }
            val context = LocalContext.current
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val msg =
                        if (streetChosenId.isNotEmpty())
                            if (houseChosenId.isNotEmpty())
                                "ID улицы: $streetChosenId, ID дома: $houseChosenId, Квартира: $flatNum"
                            else
                                "ID улицы: $streetChosenId, Дом: $houseNum, Корпус: $houseBuildNum, Квартира: $flatNum"
                        else
                            "Улица: $street, Дом: $houseNum, Корпус: $houseBuildNum, Квартира: $flatNum"

                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                },
                enabled = street.isNotEmpty() && (houseChosen.isNotEmpty() || (houseNum.isNotEmpty() && houseBuildNum.isNotEmpty())) && flatNum.isNotEmpty(),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color(0xFFA5A5AA),
                    disabledContentColor = Color(0xFFF5FAFA)
                )
            ) {
                Text(stringResource(R.string.send_button), fontSize = 18.sp)
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
                searchStreets = {},
                selectStreet = {}
            )
        }
    }
}