package com.kade.pay.presentation.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun MnemonicWord(
    text: String,
    isDragging: Boolean,
    xOffset: Int,
) {
    Box(
        modifier =
            Modifier
                .padding(horizontal = 4.dp)
                .zIndex(if (isDragging) 1f else 0f)
                .offset {
                    if (isDragging) {
                        IntOffset(x = xOffset, y = 0)
                    } else {
                        IntOffset.Zero
                    }
                },
    ) {
        AssistChip(
            onClick = {},
            label = { Text(text) },
        )
    }
}
