package com.danial.smartcardreader.ui.widgets

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CustomTextFiled(
    modifier: Modifier = Modifier,
    value: String,
    hint: String? = null,
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = hint ?: "")
        })
}

@Composable
@Preview
private fun Preview() {
    CustomTextFiled(value = "test", onValueChange = {})
}