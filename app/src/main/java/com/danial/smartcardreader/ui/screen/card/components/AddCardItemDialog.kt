package com.danial.smartcardreader.ui.screen.card.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun MinimalDialog(
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit
) {

    var title: String by remember {
        mutableStateOf("")
    }
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Enter the card tile:")
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(top = 8.dp),
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    placeholder = {
                        Text(text = "Card title ...")
                    })
                Button(modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp), onClick = { }) {
                    Text(text = "Confirm")
                }
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    MinimalDialog(
        onConfirm = {},
        onDismissRequest = {}
    )
}