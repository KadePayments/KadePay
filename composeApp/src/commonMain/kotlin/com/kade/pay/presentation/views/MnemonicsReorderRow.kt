package com.kade.pay.presentation.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kade.pay.presentation.theme.KadePayTheme
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MnemonicsReorderRow(
    index: Int,
    list: List<String>,
    onReorder: (List<String>) -> Unit = {},
) {
    val chipsList = remember { list.toMutableStateList() }

    val listState = rememberLazyListState()
    var draggedIndex by remember { mutableStateOf<Int?>(null) }
    var dragAccumulatedOffset by remember { mutableStateOf(0f) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            "$index.",
            style =
                LocalTextStyle.current.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                ),
        )

        LazyRow(
            state = listState,
            modifier =
                Modifier
                    .padding(16.dp)
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = { startOffset ->
                                val layoutInfo = listState.layoutInfo
                                val absoluteStartX = startOffset.x + layoutInfo.viewportStartOffset

                                val matchedItem =
                                    layoutInfo.visibleItemsInfo.find { item ->
                                        absoluteStartX >= item.offset && absoluteStartX <= (item.offset + item.size)
                                    }

                                matchedItem?.let {
                                    draggedIndex = it.index
                                    dragAccumulatedOffset = 0f
                                }
                            },
                            onDragEnd = {
                                draggedIndex = null
                                dragAccumulatedOffset = 0f
                                onReorder(chipsList.toList())
                            },
                            onDragCancel = {
                                draggedIndex = null
                                dragAccumulatedOffset = 0f
                                onReorder(chipsList.toList())
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                val currentIndex =
                                    draggedIndex ?: return@detectDragGesturesAfterLongPress

                                dragAccumulatedOffset += dragAmount.x

                                val layoutInfo = listState.layoutInfo
                                val currentVisibleItems = layoutInfo.visibleItemsInfo
                                val currentDraggedItemInfo =
                                    currentVisibleItems.find { it.index == currentIndex }

                                if (currentDraggedItemInfo != null) {
                                    // 1. Calculate the current absolute center coordinate of the dragged item
                                    val originalCenter =
                                        currentDraggedItemInfo.offset + (currentDraggedItemInfo.size / 2f)
                                    val currentDraggedCenter = originalCenter + dragAccumulatedOffset

                                    // 2. Find the closest item by comparing center-to-center distances
                                    val targetItem =
                                        currentVisibleItems
                                            .filter { it.index != currentIndex }
                                            .minByOrNull { item ->
                                                val itemCenter = item.offset + (item.size / 2f)
                                                (currentDraggedCenter - itemCenter).absoluteValue
                                            }

                                    // 3. Swap only if the dragged item's center has crossed past the target's center
                                    targetItem?.let { target ->
                                        val targetCenter = target.offset + (target.size / 2f)
                                        val isMovingRight = dragAccumulatedOffset > 0

                                        val shouldSwap =
                                            if (isMovingRight) {
                                                currentDraggedCenter > targetCenter && target.index > currentIndex
                                            } else {
                                                currentDraggedCenter < targetCenter && target.index < currentIndex
                                            }

                                        if (shouldSwap) {
                                            chipsList.add(
                                                target.index,
                                                chipsList.removeAt(currentIndex),
                                            )
                                            // Compensate structural layout jump to maintain clean visual continuity
                                            dragAccumulatedOffset += (currentDraggedItemInfo.offset - target.offset)
                                            draggedIndex = target.index
                                        }
                                    }
                                }
                            },
                        )
                    },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            itemsIndexed(chipsList) { index, chipText ->
                val isCurrentDragging = index == draggedIndex

                MnemonicWord(chipText, isCurrentDragging, dragAccumulatedOffset.roundToInt())
            }
        }
    }
}

@Preview
@Composable
fun PreviewMnemonicsReorderRow() {
    KadePayTheme {
        MnemonicsReorderRow(1, "abandon abandon abandon abandon abandon abandon".split(" "))
    }
}
