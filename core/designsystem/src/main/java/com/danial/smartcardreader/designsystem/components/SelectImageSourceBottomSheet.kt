package com.danial.smartcardreader.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danial.smartcardreader.designsystem.theme.SmartCardReaderTheme
import com.example.designsystem.R
import kotlinx.coroutines.launch

@Preview
@Composable
private fun Preview() {
    SmartCardReaderTheme {
        Content(
            onCameraSelected = {},
            onGallerySelected = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectImageSourceBottomSheet(
    onDismiss: () -> Unit,
    onCameraSelected: () -> Unit,
    onGallerySelected: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState
    ) {
        Content(
            onCameraSelected = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    onCameraSelected()
                }
            },
            onGallerySelected = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    onGallerySelected()
                }
            })
    }
}

@Composable
private fun Content(
    onCameraSelected: () -> Unit,
    onGallerySelected: () -> Unit
) {

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.select_image_source))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 24.dp), Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                onCameraSelected()
            }) {
                Text(
                    stringResource(R.string.camera),
                    color = MaterialTheme.colorScheme.inversePrimary
                )
            }

            Button(onClick = {
                onGallerySelected()
            }) {
                Text(
                    stringResource(R.string.gallery),
                    color = MaterialTheme.colorScheme.inversePrimary
                )
            }
        }
    }
}