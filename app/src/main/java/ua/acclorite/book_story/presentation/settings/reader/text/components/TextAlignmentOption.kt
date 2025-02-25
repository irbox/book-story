package ua.acclorite.book_story.presentation.settings.reader.text.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.reader.ReaderTextAlignment
import ua.acclorite.book_story.domain.ui.ButtonItem
import ua.acclorite.book_story.presentation.core.components.settings.SegmentedButtonWithTitle
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.main.MainModel

@Composable
fun TextAlignmentOption() {
    val mainModel = hiltViewModel<MainModel>()
    val state = mainModel.state.collectAsStateWithLifecycle()

    SegmentedButtonWithTitle(
        title = stringResource(id = R.string.text_alignment_option),
        buttons = ReaderTextAlignment.entries.map {
            ButtonItem(
                id = it.toString(),
                title = when (it) {
                    ReaderTextAlignment.START -> stringResource(id = R.string.text_alignment_start)
                    ReaderTextAlignment.JUSTIFY -> stringResource(id = R.string.text_alignment_justify)
                    ReaderTextAlignment.CENTER -> stringResource(id = R.string.text_alignment_center)
                    ReaderTextAlignment.END -> stringResource(id = R.string.text_alignment_end)
                },
                textStyle = MaterialTheme.typography.labelLarge,
                selected = it == state.value.textAlignment
            )
        },
        onClick = {
            mainModel.onEvent(
                MainEvent.OnChangeTextAlignment(
                    it.id
                )
            )
        }
    )
}