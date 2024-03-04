package ua.acclorite.book_story.domain.model

sealed class NullableBook(val book: Pair<Book, Boolean>?, val fileName: String?) {
    class NotNull(book: Pair<Book, Boolean>) : NullableBook(book, null)
    class Null(fileName: String) : NullableBook(null, fileName)
}