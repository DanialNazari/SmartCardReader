package com.danial.smartcardreader.ui.screen.card.list

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
    SmartCardReaderTheme(darkTheme = false) {
        val cardsList = arrayListOf(
            CardItemModel(
                label = "Pasargad",
                number = "50222912365455588",
                sheba = "121212121212"
            )
        )
        ContentView(
            isLoading = false,
            cardsList = cardsList,
            onItemClicked = {},
            addNewItem = {},
            deleteItem = {}
        )
    }
}

@Composable
fun CardsListScreen(
    viewModel: CardListViewModel = hiltViewModel(),
    onNavigateToCardItemScreen: (cardItemModel: CardItemModel) -> Unit
) {

    val activity = (LocalContext.current as Activity)
    val uiState = viewModel.uiState.collectAsState()

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            if (uriList.isNotEmpty()) {
                val file = FilePath.getFile(activity, uriList[0])
                viewModel.parseImage(file)
            }
        }

    LaunchedEffect(key1 = viewModel.uiState) {
        if (viewModel.uiState.value.message != null) {
            val message = when (viewModel.uiState.value.message) {
                is MessageModel.Message -> {
                    (viewModel.uiState.value.message as MessageModel.Message).message
                }

                is MessageModel.ServerError -> {
                    (viewModel.uiState.value.message as MessageModel.ServerError).message
                }

                is MessageModel.ConnectionError -> {
                    (viewModel.uiState.value.message as MessageModel.ConnectionError).message
                }

                is MessageModel.UnknownError -> {
                    (viewModel.uiState.value.message as MessageModel.UnknownError).message
                }

                null -> {
                    null
                }
            }
            message?.let {
                Toast.makeText(
                    activity, it, Toast.LENGTH_LONG
                ).show()
            }

        }


    }

    uiState.value.itemForAdd?.let { cardItem ->
        AddCardItemDialog(
            onConfirm = {
                viewModel.addItem(cardItem.copy(label = it))
            },
            onDismissRequest = {
                viewModel.dismissAddCardItem()
            })
    }

    uiState.value.itemForDelete?.let { cardItem ->
        DeleteCardItemDialog(
            onConfirm = {
                viewModel.deleteItem(cardItem)
            }, onDismissRequest = {
                viewModel.dismissDeleteItem()
            })
    }


    ContentView(
        isLoading = uiState.value.showLoading,
        cardsList = uiState.value.cardsList,
        onItemClicked = onNavigateToCardItemScreen,
        addNewItem = {
            galleryLauncher.launch("image/*")
        },
        deleteItem = {
            viewModel.deleteItemConfirm(it)
        })


}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun ContentView(
    isLoading: Boolean,
    cardsList: ArrayList<CardItemModel>?,
    onItemClicked: (cardItemModel: CardItemModel) -> Unit,
    addNewItem: () -> Unit,
    deleteItem: (CardItemModel) -> Unit
) {


    Scaffold(
        topBar = {
            CustomAppBar(
                title = stringResource(R.string.cards_list),
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
                            onItemClicked = {
                                onItemClicked(cardItem)
                            },
                            onDeleteItemClicked = {
                                deleteItem(cardItem)
                            })
                    }
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
                if (cardsList.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier.align(alignment = Alignment.Center),
                        text = stringResource(R.string.no_card_added_yet),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(alignment = Alignment.Center))
                }
            }
        })
}