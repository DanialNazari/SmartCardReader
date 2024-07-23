package com.danial.smartcardreader.ui.screen.card.widgets

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.danial.smartcardreader.R
import com.danial.smartcardreader.ui.screen.card.list.CardListViewModel


@Composable
@Preview
private fun Preview() {
    SelectImageSourceBottomSheet(
        onImageSourceSelected = {},
        onDismissRequest = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectImageSourceBottomSheet(
    onImageSourceSelected: (src: CardListViewModel.ImageSource) -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
        content = {
            Text(text = "Select source of image:")
        }
    )
}
