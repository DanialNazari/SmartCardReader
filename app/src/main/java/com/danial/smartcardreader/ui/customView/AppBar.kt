package com.danial.smartcardreader.ui.customView

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danial.smartcardreader.R

@Composable
fun CustomAppBar(
    title: String, secondButtonIcon: Int? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    onSecondButtonPressed: (() -> Unit)? = null,
    onBackPressed: (() -> Unit)? = null
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(backgroundColor)
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, color = Color.White, fontSize = 16.sp)
        Spacer(
            Modifier
                .weight(1f)
                .fillMaxHeight()
        )
        if (secondButtonIcon != null && onSecondButtonPressed != null) {
            IconButton(onClick = {
                onSecondButtonPressed()
            }) {
                Image(
                    modifier = Modifier.size(22.dp),
                    painter = painterResource(secondButtonIcon),
                    contentDescription = "location icon",
                    colorFilter = ColorFilter.tint(color = Color.White)
                )
            }
        }
        if (onBackPressed!=null) {
            IconButton(onClick = {
                onBackPressed()
            }) {
                Image(
                    modifier = Modifier.size(22.dp),
                    painter = painterResource(R.drawable.ic_baseline_arrow_forward_24),
                    contentDescription = "location icon",
                    colorFilter = ColorFilter.tint(color = Color.White)
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CustomAppBar(title = "Skin type calculator", onBackPressed = {})
}