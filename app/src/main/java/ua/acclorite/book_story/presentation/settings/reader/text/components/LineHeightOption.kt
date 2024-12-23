package ua.acclorite.book_story.presentation.settings.reader.text.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.settings.SliderWithTitle
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.main.MainModel

@Composable
fun LineHeightOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    SliderWithTitle(
        value = state.value.lineHeight to "pt",
        fromValue = 1,
        toValue = 24,
        title = stringResource(id = R.string.line_height_option),
        onValueChange = {
            mainModel.onEvent(
                MainEvent.OnChangeLineHeight(it)
            )
        }
    )
}