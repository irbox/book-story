@file:Suppress("UNCHECKED_CAST")

package ua.acclorite.book_story.ui.main

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.compose.runtime.Immutable
import androidx.datastore.preferences.core.Preferences
import kotlinx.parcelize.Parcelize
import ua.acclorite.book_story.domain.browse.BrowseFilesStructure
import ua.acclorite.book_story.domain.browse.BrowseLayout
import ua.acclorite.book_story.domain.browse.BrowseSortOrder
import ua.acclorite.book_story.domain.browse.toBrowseFilesStructure
import ua.acclorite.book_story.domain.browse.toBrowseLayout
import ua.acclorite.book_story.domain.browse.toBrowseSortOrder
import ua.acclorite.book_story.domain.reader.ReaderScreenOrientation
import ua.acclorite.book_story.domain.reader.ReaderTextAlignment
import ua.acclorite.book_story.domain.reader.toReaderScreenOrientation
import ua.acclorite.book_story.domain.reader.toTextAlignment
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.DataStoreConstants
import ua.acclorite.book_story.presentation.core.constants.provideFonts
import ua.acclorite.book_story.presentation.core.constants.provideLanguages
import ua.acclorite.book_story.ui.theme.DarkTheme
import ua.acclorite.book_story.ui.theme.PureDark
import ua.acclorite.book_story.ui.theme.Theme
import ua.acclorite.book_story.ui.theme.ThemeContrast
import ua.acclorite.book_story.ui.theme.toDarkTheme
import ua.acclorite.book_story.ui.theme.toPureDark
import ua.acclorite.book_story.ui.theme.toTheme
import ua.acclorite.book_story.ui.theme.toThemeContrast
import java.util.Locale

/**
 * Main State.
 * All app's settings/preferences/permanent-variables are here.
 * Wrapped in SavedStateHandle, so it won't reset.
 */
@Immutable
@Keep
@Parcelize
data class MainState(
    // General Settings
    val language: String = provideDefaultValue {
        val locale = Locale.getDefault().language.take(2)
        Constants.provideLanguages().any { locale == it.first }.run {
            if (this) locale
            else "en"// Default language.
        }
    },
    val theme: Theme = provideDefaultValue { Theme.entries().first() },
    val darkTheme: DarkTheme = provideDefaultValue { DarkTheme.FOLLOW_SYSTEM },
    val pureDark: PureDark = provideDefaultValue { PureDark.OFF },
    val absoluteDark: Boolean = provideDefaultValue { false },
    val themeContrast: ThemeContrast = provideDefaultValue { ThemeContrast.STANDARD },
    val showStartScreen: Boolean = provideDefaultValue { true },
    val checkForUpdates: Boolean = provideDefaultValue { false },
    val doublePressExit: Boolean = provideDefaultValue { false },

    // Reader Settings
    val fontFamily: String = provideDefaultValue { Constants.provideFonts(withRandom = false)[0].id },
    val isItalic: Boolean = provideDefaultValue { false },
    val fontSize: Int = provideDefaultValue { 16 },
    val lineHeight: Int = provideDefaultValue { 4 },
    val paragraphHeight: Int = provideDefaultValue { 8 },
    val paragraphIndentation: Int = provideDefaultValue { 0 },
    val sidePadding: Int = provideDefaultValue { 6 },
    val verticalPadding: Int = provideDefaultValue { 0 },
    val doubleClickTranslation: Boolean = provideDefaultValue { false },
    val fastColorPresetChange: Boolean = provideDefaultValue { true },
    val textAlignment: ReaderTextAlignment = provideDefaultValue { ReaderTextAlignment.START },
    val letterSpacing: Int = provideDefaultValue { 0 },
    val cutoutPadding: Boolean = provideDefaultValue { false },
    val fullscreen: Boolean = provideDefaultValue { true },
    val keepScreenOn: Boolean = provideDefaultValue { true },
    val hideBarsOnFastScroll: Boolean = provideDefaultValue { false },
    val perceptionExpander: Boolean = provideDefaultValue { false },
    val perceptionExpanderPadding: Int = provideDefaultValue { 5 },
    val perceptionExpanderThickness: Int = provideDefaultValue { 4 },
    val checkForTextUpdate: Boolean = provideDefaultValue { true },
    val checkForTextUpdateToast: Boolean = provideDefaultValue { true },
    val screenOrientation: ReaderScreenOrientation = provideDefaultValue {
        ReaderScreenOrientation.DEFAULT
    },
    val customScreenBrightness: Boolean = provideDefaultValue { false },
    val screenBrightness: Float = provideDefaultValue { 0.5f },

    // Browse Settings
    val browseFilesStructure: BrowseFilesStructure = provideDefaultValue {
        BrowseFilesStructure.DIRECTORIES
    },
    val browseLayout: BrowseLayout = provideDefaultValue { BrowseLayout.LIST },
    val browseAutoGridSize: Boolean = provideDefaultValue { true },
    val browseGridSize: Int = provideDefaultValue { 0 },
    val browsePinFavoriteDirectories: Boolean = provideDefaultValue { true },
    val browseSortOrder: BrowseSortOrder = provideDefaultValue { BrowseSortOrder.LAST_MODIFIED },
    val browseSortOrderDescending: Boolean = provideDefaultValue { true },
    val browseIncludedFilterItems: List<String> = provideDefaultValue { emptyList() },
) : Parcelable {
    companion object {
        private fun <D> provideDefaultValue(calculation: () -> D): D {
            return calculation()
        }

        /**
         * Initializes [MainState] by given [Map].
         * If no value provided in [data], assigns default value.
         */
        fun initialize(data: Map<String, Any>): MainState {
            val defaultState = MainState()
            fun <V, T> provideValue(
                key: Preferences.Key<T>,
                convert: T.() -> V = { this as V },
                default: MainState.() -> V
            ): V {
                return (data[key.name] as? T)?.convert() ?: defaultState.default()
            }

            return DataStoreConstants.run {
                MainState(
                    language = provideValue(
                        LANGUAGE
                    ) { language },

                    theme = provideValue(
                        THEME, convert = { toTheme() }
                    ) { theme },

                    darkTheme = provideValue(
                        DARK_THEME, convert = { toDarkTheme() }
                    ) { darkTheme },

                    pureDark = provideValue(
                        PURE_DARK, convert = { toPureDark() }
                    ) { pureDark },

                    absoluteDark = provideValue(
                        ABSOLUTE_DARK
                    ) { absoluteDark },

                    themeContrast = provideValue(
                        THEME_CONTRAST, convert = { toThemeContrast() }
                    ) { themeContrast },

                    showStartScreen = provideValue(
                        SHOW_START_SCREEN
                    ) { showStartScreen },

                    fontFamily = provideValue(
                        FONT
                    ) { fontFamily },

                    isItalic = provideValue(
                        IS_ITALIC
                    ) { isItalic },

                    fontSize = provideValue(
                        FONT_SIZE
                    ) { fontSize },

                    lineHeight = provideValue(
                        LINE_HEIGHT
                    ) { lineHeight },

                    paragraphHeight = provideValue(
                        PARAGRAPH_HEIGHT
                    ) { paragraphHeight },

                    paragraphIndentation = provideValue(
                        PARAGRAPH_INDENTATION
                    ) { paragraphIndentation },

                    checkForUpdates = provideValue(
                        CHECK_FOR_UPDATES
                    ) { checkForUpdates },

                    sidePadding = provideValue(
                        SIDE_PADDING
                    ) { sidePadding },

                    doubleClickTranslation = provideValue(
                        DOUBLE_CLICK_TRANSLATION
                    ) { doubleClickTranslation },

                    fastColorPresetChange = provideValue(
                        FAST_COLOR_PRESET_CHANGE
                    ) { fastColorPresetChange },

                    browseFilesStructure = provideValue(
                        BROWSE_FILES_STRUCTURE, convert = { toBrowseFilesStructure() }
                    ) { browseFilesStructure },

                    browseLayout = provideValue(
                        BROWSE_LAYOUT, convert = { toBrowseLayout() }
                    ) { browseLayout },

                    browseAutoGridSize = provideValue(
                        BROWSE_AUTO_GRID_SIZE
                    ) { browseAutoGridSize },

                    browseGridSize = provideValue(
                        BROWSE_GRID_SIZE
                    ) { browseGridSize },

                    browsePinFavoriteDirectories = provideValue(
                        BROWSE_PIN_FAVORITE_DIRECTORIES
                    ) { browsePinFavoriteDirectories },

                    browseSortOrder = provideValue(
                        BROWSE_SORT_ORDER, convert = { toBrowseSortOrder() }
                    ) { browseSortOrder },

                    browseSortOrderDescending = provideValue(
                        BROWSE_SORT_ORDER_DESCENDING
                    ) { browseSortOrderDescending },

                    browseIncludedFilterItems = provideValue(
                        BROWSE_INCLUDED_FILTER_ITEMS, convert = { toList() }
                    ) { browseIncludedFilterItems },

                    textAlignment = provideValue(
                        TEXT_ALIGNMENT, convert = { toTextAlignment() }
                    ) { textAlignment },

                    doublePressExit = provideValue(
                        DOUBLE_PRESS_EXIT
                    ) { doublePressExit },

                    letterSpacing = provideValue(
                        LETTER_SPACING
                    ) { letterSpacing },

                    cutoutPadding = provideValue(
                        CUTOUT_PADDING
                    ) { cutoutPadding },

                    fullscreen = provideValue(
                        FULLSCREEN
                    ) { fullscreen },

                    keepScreenOn = provideValue(
                        KEEP_SCREEN_ON
                    ) { keepScreenOn },

                    verticalPadding = provideValue(
                        VERTICAL_PADDING
                    ) { verticalPadding },

                    hideBarsOnFastScroll = provideValue(
                        HIDE_BARS_ON_FAST_SCROLL
                    ) { hideBarsOnFastScroll },

                    perceptionExpander = provideValue(
                        PERCEPTION_EXPANDER
                    ) { perceptionExpander },

                    perceptionExpanderPadding = provideValue(
                        PERCEPTION_EXPANDER_PADDING
                    ) { perceptionExpanderPadding },

                    perceptionExpanderThickness = provideValue(
                        PERCEPTION_EXPANDER_THICKNESS
                    ) { perceptionExpanderThickness },

                    checkForTextUpdate = provideValue(
                        CHECK_FOR_TEXT_UPDATE
                    ) { checkForTextUpdate },

                    checkForTextUpdateToast = provideValue(
                        CHECK_FOR_TEXT_UPDATE_TOAST
                    ) { checkForTextUpdateToast },

                    screenOrientation = provideValue(
                        SCREEN_ORIENTATION, convert = { toReaderScreenOrientation() }
                    ) { screenOrientation },

                    customScreenBrightness = provideValue(
                        CUSTOM_SCREEN_BRIGHTNESS
                    ) { customScreenBrightness },

                    screenBrightness = provideValue(
                        SCREEN_BRIGHTNESS, convert = { this.toFloat() }
                    ) { screenBrightness },
                )
            }
        }
    }
}