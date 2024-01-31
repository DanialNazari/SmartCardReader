package com.danial.smartcardreader.ui.widgets

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CustomText(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.primary
) {
    Text(
        modifier = modifier,
        text = text,
        color = textColor
    )
}

@Composable
@Preview
private fun Preview() {
    CustomText(text = "test")
}