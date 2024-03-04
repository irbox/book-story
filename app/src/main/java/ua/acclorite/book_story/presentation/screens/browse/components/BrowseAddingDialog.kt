package ua.acclorite.book_story.presentation.screens.browse.components

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddChart
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.Navigator
import ua.acclorite.book_story.presentation.components.CustomDialogWithLazyColumn
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel

@Composable
fun BrowseAddingDialog(
    libraryViewModel: LibraryViewModel = hiltViewModel(),
    viewModel: BrowseViewModel,
    navigator: Navigator
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    CustomDialogWithLazyColumn(
        title = stringResource(id = R.string.add_books),
        imageVectorIcon = Icons.Default.AddChart,
        description = stringResource(id = R.string.add_books_description),
        actionText = stringResource(id = R.string.add),
        isActionEnabled = !state.isBooksLoading,
        onDismiss = { viewModel.onEvent(BrowseEvent.OnAddingDialogDismiss) },
        onAction = {
            viewModel.onEvent(
                BrowseEvent.OnAddBooks(
                    navigator,
                    resetScroll = {
                        libraryViewModel.onEvent(LibraryEvent.OnUpdateCurrentPage(0))
                    }
                )
            )
            Toast.makeText(
                context,
                context.getString(R.string.books_added),
                Toast.LENGTH_LONG
            ).show()
        },
        withDivider = true,
        items = {
            if (state.isBooksLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(36.dp)
                        )
                    }
                }
            } else {
                items(state.selectedBooks) { book ->
                    BrowseAddingDialogItem(result = book) {
                        viewModel.onEvent(BrowseEvent.OnSelectBook(book))
                    }
                }
            }
        }
    )
}