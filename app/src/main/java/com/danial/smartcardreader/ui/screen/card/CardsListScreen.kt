package com.danial.smartcardreader.ui.screen.card

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.danial.smartcardreader.R
import com.danial.smartcardreader.model.CardItemModel
import com.danial.smartcardreader.model.MessageModel
import com.danial.smartcardreader.ui.customView.CustomAppBar
import com.danial.smartcardreader.ui.screen.card.widgets.AddCardItemDialog
import com.danial.smartcardreader.ui.screen.card.widgets.CardItem
import com.danial.smartcardreader.ui.screen.card.widgets.DeleteCardItemDialog
import com.danial.smartcardreader.ui.theme.SmartCardReaderTheme
import com.danial.smartcardreader.ui.utils.FilePath

@Composable
@Preview
fun CardsListScreenPreview() {
    SmartCardReaderTheme(darkTheme = true) {
        val cardsList = listOf(
            CardItemModel(
                label = "Pasargad",
                number = "50222912365455588",
                sheba = "121212121212"
            )
        )
        ContentView(
            isLoading = false,
            cardsList = /*cardsList*/emptyList(),
            addNewItem = {},
            deleteItem = {}
        )
    }
}

@Composable
fun CardsListScreen(viewModel: CardListViewModel = hiltViewModel()) {

    val activity = (LocalContext.current as Activity)

    val uiState by viewModel.uiState.collectAsState()

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            if (uriList.isNotEmpty()) {
                val file = FilePath.getFile(activity, uriList[0])
                viewModel.parseImage(file)
            }
        }

    LaunchedEffect(key1 = uiState) {
        if (uiState.message != null) {
            val message = when (uiState.message) {
                is MessageModel.Message -> {
                    (uiState.message as MessageModel.Message).message
                }

                is MessageModel.ServerError -> {
                    (uiState.message as MessageModel.ServerError).message
                }

                is MessageModel.ConnectionError -> {
                    (uiState.message as MessageModel.ConnectionError).message
                }

                is MessageModel.UnknownError -> {
                    (uiState.message as MessageModel.UnknownError).message
                }

                null -> null
            }

            message?.let {
                Toast.makeText(
                    activity, it, Toast.LENGTH_LONG
                ).show()
            }

        }
    }

    uiState.addCardResult?.let { cardItem ->
        AddCardItemDialog(
            onConfirm = {
                viewModel.addItem(cardItem.copy(label = it))
            },
            onDismissRequest = {
                viewModel.dismissAddCardDialog()
            })
    }

    uiState.itemForDelete?.let {
        DeleteCardItemDialog(
            onConfirm = {
                viewModel.deleteItem(it)
            }, onDismissRequest = {
                viewModel.dismissDeleteItem()
            })
    }

    ContentView(
        isLoading = uiState.showLoading,
        cardsList = uiState.cardsList,
        addNewItem = {
            galleryLauncher.launch("image/*")
        },
        deleteItem = {
            viewModel.confirmDeleteItem(it)
        })


}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun ContentView(
    isLoading: Boolean,
    cardsList: List<CardItemModel>?,
    addNewItem: () -> Unit,
    deleteItem: (CardItemModel) -> Unit
) {

    Scaffold(
        topBar = {
            CustomAppBar(
                title = "Cards list",
            )
        }, content = {
            Box(
                Modifier
                    .padding(it)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                ) {
                    cardsList?.forEach { cardItem ->
                        CardItem(
                            cardItem = cardItem,
                            onDeleteItemClicked = {
                                deleteItem(cardItem)
                            })
                    }
                }

                if (cardsList.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(R.string.no_cards_added_until_now),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                FloatingActionButton(
                    onClick = addNewItem,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_add),
                        contentDescription = stringResource(R.string.add_new_card)
                    )
                }

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(alignment = Alignment.Center),
                    )
                }

            }
        })
}