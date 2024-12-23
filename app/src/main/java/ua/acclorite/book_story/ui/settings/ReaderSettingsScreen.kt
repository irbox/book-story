package ua.acclorite.book_story.ui.settings

import android.os.Parcelable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import kotlinx.parcelize.Parcelize
import ua.acclorite.book_story.domain.navigator.Screen
import ua.acclorite.book_story.presentation.core.components.top_bar.collapsibleTopAppBarScrollBehavior
import ua.acclorite.book_story.presentation.navigator.LocalNavigator
import ua.acclorite.book_story.presentation.settings.reader.ReaderSettingsContent

@Parcelize
object ReaderSettingsScreen : Screen, Parcelable {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val (scrollBehavior, listState) = TopAppBarDefaults.collapsibleTopAppBarScrollBehavior()

        ReaderSettingsContent(
            scrollBehavior = scrollBehavior,
            listState = listState,
            navigateBack = {
                navigator.pop()
            }
        )
    }
}