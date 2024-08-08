package com.example.loldex.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.loldex.core.designsystem.theme.ThemePreviews
import com.example.loldex.core.model.YugiohCardData
import com.example.loldex.feature.home.ui.YugiohCardItem
import com.example.loldex.feature.home.ui.component.LoadStateAppendError
import com.example.loldex.feature.home.ui.component.LoadStateAppendSkeletonLoading
import com.example.loldex.feature.home.ui.component.LoadStateRefreshError
import com.example.loldex.feature.home.ui.component.LoadStateRefreshSkeletonLoading
import com.example.loldex.feature.home.ui.component.pagingLoadStateHandler

@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val yugiohListPagingItems: LazyPagingItems<YugiohCardData> =
        viewModel.yugiohListState.collectAsLazyPagingItems()
    val scrollState = rememberLazyGridState()
    var hasAppendErrorShown = remember { mutableStateOf(false) }

    HomeScreen(
        yugiohListPagingItems = yugiohListPagingItems,
        scrollState = scrollState,
        onClickedContentItem = {},
        hasAppendErrorShown = hasAppendErrorShown,
    )
}

@Composable
internal fun HomeScreen(
    yugiohListPagingItems: LazyPagingItems<YugiohCardData>,
    scrollState: LazyGridState,
    onClickedContentItem: (Long) -> Unit,
    hasAppendErrorShown: MutableState<Boolean>,
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        columns = GridCells.Fixed(2),
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(yugiohListPagingItems.itemCount) { index ->
            yugiohListPagingItems[index]?.let { yugiohCardData ->
                YugiohCardItem(
                    onClickedItem = onClickedContentItem,
                    yugiohCardData = yugiohCardData,
                )
            }
        }
        handlePagingLoadState(
            yugiohListPagingItems = yugiohListPagingItems,
            hasAppendErrorShown = hasAppendErrorShown
        )
    }
}

private fun LazyGridScope.handlePagingLoadState(
    yugiohListPagingItems: LazyPagingItems<YugiohCardData>,
    hasAppendErrorShown: MutableState<Boolean>
) {
    pagingLoadStateHandler(
        pagingItems = yugiohListPagingItems,
        loadStateRefreshLoading = {
            this@handlePagingLoadState.LoadStateRefreshSkeletonLoading()
        },
        loadStateRefreshError = { error ->
            this@handlePagingLoadState.LoadStateRefreshError(
                error = error,
                onClickRetry = {
                    yugiohListPagingItems.retry()
                }
            )

        },
        loadStateAppendLoading = {
            this@handlePagingLoadState.LoadStateAppendSkeletonLoading()
        },
        loadStateAppendError = { error ->
            if (!hasAppendErrorShown.value) {
                hasAppendErrorShown.value = true
                this@handlePagingLoadState.LoadStateAppendError(
                    error = error,
                    onClickRetry = {
                        hasAppendErrorShown.value = false
                        yugiohListPagingItems.retry()
                    }
                )
            }
        }
    )
}

@ThemePreviews
@Composable
fun HomeScreenPreview() {
}