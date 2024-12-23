package ua.acclorite.book_story.presentation.history

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BoxScope.HistoryRefreshIndicator(
    isRefreshing: Boolean,
    refreshState: PullRefreshState
) {
    PullRefreshIndicator(
        isRefreshing,
        refreshState,
        Modifier.align(Alignment.TopCenter),
        backgroundColor = MaterialTheme.colorScheme.inverseSurface,
        contentColor = MaterialTheme.colorScheme.inverseOnSurface
    )
}