package com.danial.smartcardreader.ui.screen.card.item

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.danial.smartcardreader.R
import com.danial.smartcardreader.model.CardItemModel
import com.danial.smartcardreader.ui.customView.CustomAppBar
import com.danial.smartcardreader.ui.theme.SmartCardReaderTheme

@Composable
@Preview
fun CardsListScreenPreview() {
    SmartCardReaderTheme(darkTheme = false) {
        ContentView(
            cardDetails = CardItemModel(
                label = "Test",
                sheba = "1234543224243432340000",
                number = "3244432334543422"
            )
        )
    }
}

@Composable
fun CardsItemScreen(
    cardItem: CardItemModel?,
    cardItemViewModel: CardItemViewModel = hiltViewModel(),
) {
    var initCardItemViewModel by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = initCardItemViewModel) {
        if (!initCardItemViewModel) {
            cardItemViewModel.setItem(cardItem)
            initCardItemViewModel = true
        }
    }

    ContentView(cardItemViewModel.uiState.collectAsState().value.cardItem)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun ContentView(cardDetails: CardItemModel?) {


    Scaffold(
        topBar = {
            CustomAppBar(
                title = stringResource(R.string.card_details),
            )
        }, content = {
            Box(
                Modifier
                    .padding(it)
                    .background(MaterialTheme.colorScheme.background)
            ) {

                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            text = stringResource(R.string.label)
                        )
                        Text(text = cardDetails?.label ?: " --- ")
                    }
                }

            }
        })
}