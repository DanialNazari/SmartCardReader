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
import androidx.compose.ui.graphics.Color
import com.danial.smartcardreader.model.CardItemModel
import com.danial.smartcardreader.ui.customView.CustomAppBar

@Composable
@Preview
fun CardsListScreenPreview() {
    val cardsList = listOf(CardItemModel(number = "50222912365455588", sheba = "121212121212"))
    ContentView(isLoading = false, cardsList = cardsList, addNewItem = {})
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

    ContentView(isLoading = viewModel.isLoading, cardsList = viewModel.cardsList, addNewItem = {
        galleryLauncher.launch("image/*")
    })


}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun ContentView(
    isLoading: Boolean,
    cardsList: List<CardItemModel>,
    addNewItem: () -> Unit
) {

    Scaffold(
        topBar = {
            CustomAppBar(
                title = "Cards list",
                disableBackButton = true,
                onBackPressed = {})
        }, content = {
            Box(Modifier.padding(it)) {
                Column(Modifier.fillMaxHeight()) {
                    cardsList.forEach {
                        CardItem(it)
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
