package ua.acclorite.book_story.data.parser.pdf

import android.util.Log
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import ua.acclorite.book_story.R
import ua.acclorite.book_story.data.parser.TextParser
import ua.acclorite.book_story.domain.model.ChapterWithText
import ua.acclorite.book_story.domain.util.Resource
import ua.acclorite.book_story.domain.util.UIText
import ua.acclorite.book_story.presentation.core.constants.Constants
import java.io.File
import javax.inject.Inject

private const val PDF_TAG = "PDF Parser"

class PdfTextParser @Inject constructor() : TextParser {

    override suspend fun parse(file: File): Resource<List<ChapterWithText>> {
        Log.i(PDF_TAG, "Started PDF parsing: ${file.name}.")

        return try {
            val document = PDDocument.load(file)
            val strings = mutableListOf<String>()

            val pdfStripper = PDFTextStripper()
            pdfStripper.paragraphStart = "</br>"

            val oldText = pdfStripper.getText(document)
                .replace("\r", "")

            document.close()

            val text = oldText.filterIndexed { index, c ->
                if (c == ' ') {
                    oldText[index - 1] != ' '
                } else {
                    true
                }
            }

            val unformattedLines = text.split("${pdfStripper.paragraphStart}|\\n".toRegex())
                .filter { it.isNotBlank() }

            val lines = mutableListOf<String>()
            unformattedLines.forEachIndexed { index, string ->
                try {
                    val line = string.trim()

                    if (index == 0) {
                        lines.add(line)
                        return@forEachIndexed
                    }

                    if (line.all { it.isDigit() }) {
                        return@forEachIndexed
                    }

                    if (line.first().isLowerCase()) {
                        val currentLine = lines[lines.lastIndex]

                        if (currentLine.last() == '-') {
                            if (currentLine[currentLine.lastIndex - 1].isLowerCase()) {
                                lines[lines.lastIndex] = currentLine.dropLast(1) + line
                                return@forEachIndexed
                            }
                        }

                        lines[lines.lastIndex] += " $line"
                        return@forEachIndexed
                    }

                    if (line.first().isUpperCase() || line.first().isDigit()) {
                        lines.add(line)
                        return@forEachIndexed
                    }

                    if (line.first().isLetter()) {
                        lines[lines.lastIndex] += " $line"
                        return@forEachIndexed
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    return@forEachIndexed
                }
            }

            lines.forEach { line ->
                strings.add(line.trim())
            }

            if (strings.isEmpty()) {
                return Resource.Error(UIText.StringResource(R.string.error_file_empty))
            }

            Log.i(PDF_TAG, "Successfully finished PDF parsing.")
            Resource.Success(
                listOf(
                    ChapterWithText(
                        chapter = Constants.EMPTY_CHAPTER,
                        text = strings
                    )
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                UIText.StringResource(
                    R.string.error_query,
                    e.message?.take(40)?.trim() ?: ""
                )
            )
        }
    }
}
















