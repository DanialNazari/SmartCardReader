package com.danial.smartcardreader.cardslist.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danial.cardslist.R
import com.danial.network.model.CardItemModel
import com.danial.smartcardreader.designsystem.components.CustomText
import com.danial.smartcardreader.designsystem.theme.SmartCardReaderTheme

@Composable
fun CardItem(
    cardItem: CardItemModel,
    onItemClicked: () -> Unit,
    onDeleteItemClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
                .clickable {
                    onItemClicked()
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                cardItem.label?.let {
                    CustomText(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = it,
                        textStyle = MaterialTheme.typography.titleLarge
                    )
                }

                Text(text = cardItem.number, color = MaterialTheme.colorScheme.onSurface)

                if (cardItem.sheba != null) {
                    Text(text = cardItem.sheba!!, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                }

            }

            Image(
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onDeleteItemClicked()
                    },
                painter = painterResource(id = R.drawable.baseline_delete_outline),
                contentDescription = "Delete item"
            )

        }
    }
}

@Composable
@Preview
fun CardItemPreview() {
    SmartCardReaderTheme(darkTheme = true) {
        CardItem(
            CardItemModel(
                label = "Pasargad",
                number = "3354778566954411",
                sheba = "IR3354778566954411"
            ),
            onItemClicked = {},
            onDeleteItemClicked = {}
        )
    }
}