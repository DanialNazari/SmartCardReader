package com.danial.smartcardreader.ui.screen.card.widgets

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danial.smartcardreader.R
import com.danial.smartcardreader.model.CardItemModel
import com.danial.smartcardreader.ui.widgets.CustomText

@Composable
fun CardItem(cardItem: CardItemModel, onDeleteItemClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(shape = RoundedCornerShape(12.dp), color = Color.White)
                .border(
                    1.dp, color = Color.Gray, RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                cardItem.label?.let {
                    CustomText(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = it
                    )
                }

                Text(text = cardItem.number)

                if (cardItem.sheba != null) {
                    Text(text = cardItem.sheba, color = Color.Gray, fontSize = 10.sp)
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
    CardItem(CardItemModel(
        label = "Pasargad",
        number = "3354778566954411",
        sheba = "IR3354778566954411"
    ),
        onDeleteItemClicked = {}
    )
}