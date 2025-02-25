package ua.acclorite.book_story.domain.use_case.book

import ua.acclorite.book_story.domain.reader.Chapter
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.util.Resource
import javax.inject.Inject

class CheckForTextUpdate @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(bookId: Int): Resource<Pair<List<String>, List<Chapter>>?> {
        return repository.checkForTextUpdate(bookId)
    }
}