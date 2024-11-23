package com.danial.smartcardreader.cardslist.widgets

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.danial.cardslist.R


@Composable
@Preview
private fun Preview() {
    DeleteCardItemDialog(
        onConfirm = {},
        onDismissRequest = {}
    )
}

@Composable
fun DeleteCardItemDialog(onConfirm: () -> Unit, onDismissRequest: () -> Unit) {
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
                onClick = onConfirm
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
