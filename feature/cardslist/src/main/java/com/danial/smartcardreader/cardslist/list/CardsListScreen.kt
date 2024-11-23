package com.danial.smartcardreader.cardslist.list

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.danial.cardslist.R
import com.danial.network.model.CardItemModel
import com.danial.smartcardreader.cardslist.widgets.CardItem
import com.danial.smartcardreader.data.util.createImageFile
import com.danial.smartcardreader.designsystem.components.CustomAppBar
import com.danial.smartcardreader.designsystem.theme.SmartCardReaderTheme
import com.danial.smartcardreader.cardslist.widgets.AddCardItemDialog
import com.danial.smartcardreader.cardslist.widgets.DeleteCardItemDialog
import com.danial.smartcardreader.designsystem.components.SelectImageSourceBottomSheet
import com.danial.smartcardreader.util.FilePath

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
    val context = LocalContext.current

    val uiState = viewModel.uiState.collectAsState().value

    var uri by remember {
        mutableStateOf<Uri?>(null)
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { result ->
        if (result && uri != null) {
            val file = FilePath.getFile(activity, uri)
            viewModel.parseImage(file)
        }
        viewModel.dismissOnSelectedAsImageSource()
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
        if (uriList.isNotEmpty()) {
            val file = FilePath.getFile(activity, uriList[0])
            viewModel.parseImage(file)
        }
        viewModel.dismissOnSelectedAsImageSource()
    }

    LaunchedEffect(key1 = uiState.message) {
        uiState.message?.let { messageModel ->
            messageModel.msg.let {
                Toast.makeText(
                    activity, it, Toast.LENGTH_LONG
                ).show()
            }
            viewModel.clearMessage()
        }
    }

    uiState.itemForAdd?.let { cardItem ->
        AddCardItemDialog(
            onConfirm = {
                viewModel.addItem(cardItem.copy(label = it))
            },
            onDismissRequest = {
                viewModel.dismissAddCardItem()
            }
        )
    }

    uiState.itemForDelete?.let { cardItem ->

        DeleteCardItemDialog(
            onConfirm = {
                viewModel.deleteItem(cardItem)
            }, onDismissRequest = {
                viewModel.dismissDeleteItem()
            })
    }

    if (uiState.onCameraSelectedAsImageSource) {
        val tempFile = context.createImageFile()
        //todo: work on this later
        //val fileUri = FileProvider.getUriForFile(Objects.requireNonNull(context), BuildConfig.APPLICATION_ID + ".fileprovider", tempFile)
        //uri = fileUri
        //cameraLauncher.launch(fileUri)
    }

    if (uiState.onGallerySelectedAsImageSource) {
        galleryLauncher.launch("image/*")
    }

    if (uiState.showImageSourceSelectionDialog) {
        SelectImageSourceBottomSheet(
            onCameraSelected = {
                viewModel.onCameraSelectAsImageSource()
            },
            onGallerySelected = {
                viewModel.onGallerySelectAsImageSource()
            },
            onDismiss = {
                viewModel.dismissImageSourceSelectionDialog()
            })
    }

    ContentView(
        isLoading = uiState.showLoading,
        cardsList = uiState.cardsList,
        onItemClicked = onNavigateToCardItemScreen,
        addNewItem = {
            viewModel.showImageSourceSelectionDialog()
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

                if (!isLoading && cardsList.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier.align(alignment = Alignment.Center),
                        text = stringResource(R.string.no_card_added_yet),
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
                    CircularProgressIndicator(modifier = Modifier.align(alignment = Alignment.Center))
                }
            }
        })
}

