package com.example.loldex.feature.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.loldex.core.designsystem.component.DynamicAsyncImage
import com.example.loldex.core.designsystem.theme.ThemePreviews
import com.example.loldex.core.designsystem.theme.ldTypography
import com.example.loldex.core.model.DigimonContentData

@Composable
fun DigimonContentItem(
    onClickedItem: (Int) -> Unit,
    digimonContentData: DigimonContentData
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        onClick = { onClickedItem(digimonContentData.id) },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DynamicAsyncImage(
                modifier = Modifier
                    .weight(1f),
                imageUrl = digimonContentData.image,
                contentDescription = "Digimon Card Item"
            )

            Text(
                text = digimonContentData.name,
                style = MaterialTheme.ldTypography.fontTitleS,
                color = Color.Red,
            )
        }
    }
}

@ThemePreviews
@Composable
fun DigimonContentItemPreview() {
    val digimonContentData = DigimonContentData(
        href = "",
        id = 1,
        image = "",
        name = "Name"
    )

    DigimonContentItem(
        onClickedItem = {},
        digimonContentData = digimonContentData
    )
}