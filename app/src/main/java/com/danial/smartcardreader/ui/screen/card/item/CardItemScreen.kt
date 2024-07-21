package com.danial.smartcardreader.ui.screen.card.item

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
        ContentView()
    }
}

@Composable
fun CardsItemScreen() {

    val activity = (LocalContext.current as Activity)

    ContentView()

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun ContentView(
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

            }
        })
}