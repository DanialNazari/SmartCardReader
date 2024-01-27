package com.danial.smartcardreader.ui.screen.card

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.danial.smartcardreader.ui.utils.FilePath
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.danial.smartcardreader.R
import com.danial.smartcardreader.model.CardItemModel
import com.danial.smartcardreader.ui.customView.CustomAppBar
import com.danial.smartcardreader.ui.screen.card.components.CardItem
import com.danial.smartcardreader.ui.theme.SmartCardReaderTheme

@Composable
@Preview
fun CardsListScreenPreview() {
    SmartCardReaderTheme {
        val cardsList = listOf(
            CardItemModel(
                number = "50222912365455588",
                sheba = "121212121212"
            )
        )
        ContentView(
            isLoading = false,
            cardsList = cardsList,
            addNewItem = {},
            deleteItem = {}
        )
    }
}

@Composable
fun CardsListScreen(viewModel: CardListViewModel = hiltViewModel()) {

    val activity = (LocalContext.current as Activity)

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            if (uriList.isNotEmpty()) {
                val file = FilePath.getFile(activity, uriList[0])
                viewModel.parseImage(file)
            }
        }

    if (viewModel.messageStateFlow.value != null) {
        if (!viewModel.messageStateFlow.value?.description.isNullOrEmpty()) {
            Toast.makeText(
                activity, viewModel.messageStateFlow.value?.description, Toast.LENGTH_LONG
            ).show()
        }
        viewModel.messageStateFlow.value = null
    }

    ContentView(
        isLoading = viewModel.isLoading,
        cardsList = viewModel.cardsList,
        addNewItem = {
            galleryLauncher.launch("image/*")
        },
        deleteItem = {
            viewModel.deleteItem(it)
        })


}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun ContentView(
    isLoading: Boolean,
    cardsList: List<CardItemModel>,
    addNewItem: () -> Unit,
    deleteItem: (CardItemModel) -> Unit
) {

    var itemForDelete: CardItemModel? by remember { mutableStateOf(null) }

    itemForDelete?.let {
        DeleteItemDialog(
            onConfirmation = {
                deleteItem(it)
                itemForDelete = null
            }, onDismissRequest = {
                itemForDelete = null
            })
    }


    Scaffold(
        topBar = {
            CustomAppBar(
                title = "Cards list"
            )
        }, content = {
            Box(Modifier.padding(it)) {
                Column(Modifier.fillMaxHeight()) {
                    cardsList.forEach { cardItem ->
                        CardItem(
                            cardItem = cardItem,
                            onDeleteItemClicked = {
                                itemForDelete = cardItem
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
                        contentDescription = "Add new card"
                    )
                }
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color(0xC6FFFFFF))
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(alignment = Alignment.Center))
                    }
                }
            }
        })
}

@Composable
private fun DeleteItemDialog(onConfirmation: () -> Unit, onDismissRequest: () -> Unit) {
    AlertDialog(
        title = {
            Text(text = stringResource(id = R.string.delete_item))
        },
        text = {
            Text(text = stringResource(id = R.string.delete_item_desc))
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}
