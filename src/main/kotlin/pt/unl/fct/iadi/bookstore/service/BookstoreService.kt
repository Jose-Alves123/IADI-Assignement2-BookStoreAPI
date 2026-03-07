package pt.unl.fct.iadi.bookstore.service

import org.springframework.stereotype.Service
import pt.unl.fct.iadi.bookstore.domain.Book
import pt.unl.fct.iadi.bookstore.domain.Review
import java.util.UUID

@Service
class BookstoreService {

    private val books: MutableMap<String, Book> = mutableMapOf()
    private val reviewsByBookIsbn: MutableMap<String, MutableList<Review>> = mutableMapOf()

    fun createBook(book: Book)  {
        if (books.containsKey(book.isbn)) {
            throw BookAlreadyExistsException(book.isbn)
        }

        books[book.isbn] = book
    }

    fun getBook(isbn : String): Book {
        return books[isbn] ?: throw BookNotFoundException(isbn)
    }

    fun getAllBooks(): List<Book> {
        return books.values.toList()
    }

    fun replaceBook(isbn: String, book: Book): Boolean {
        val isCreate = !books.containsKey(isbn)
        books[isbn] = book.copy(isbn = isbn)
        return isCreate
    }

    fun updateBookPartially(
        isbn: String,
        title: String?,
        author: String?,
        price: Double?,
        image: String?
    ): Book {
        val existing = getBook(isbn)
        val updated = existing.copy(
            title = title ?: existing.title,
            author = author ?: existing.author,
            price = price ?: existing.price,
            image = image ?: existing.image
        )

        books[isbn] = updated
        return updated
    }

    fun deleteBook(isbn: String) {
        if (books.remove(isbn) == null) {
            throw BookNotFoundException(isbn)
        }

        reviewsByBookIsbn.remove(isbn)
    }

    fun getReviews(isbn: String): List<Review> {
        getBook(isbn)
        return reviewsByBookIsbn[isbn]?.toList() ?: emptyList()
    }

    fun createReview(isbn: String, rating: Int, comment: String?): Review {
        getBook(isbn)
        val review = Review(id = UUID.randomUUID(), rating = rating, comment = comment)
        reviewsByBookIsbn.getOrPut(isbn) { mutableListOf() }.add(review)
        return review
    }

    fun replaceReview(isbn: String, reviewId: UUID, rating: Int, comment: String?): Review {
        getBook(isbn)
        val reviews = reviewsByBookIsbn[isbn] ?: throw ReviewNotFoundException(isbn, reviewId)
        val index = reviews.indexOfFirst { it.id == reviewId }
        if (index == -1) {
            throw ReviewNotFoundException(isbn, reviewId)
        }

        val updated = Review(id = reviewId, rating = rating, comment = comment)
        reviews[index] = updated
        return updated
    }

    fun patchReview(isbn: String, reviewId: UUID, rating: Int?, comment: String?): Review {
        getBook(isbn)
        val reviews = reviewsByBookIsbn[isbn] ?: throw ReviewNotFoundException(isbn, reviewId)
        val index = reviews.indexOfFirst { it.id == reviewId }
        if (index == -1) {
            throw ReviewNotFoundException(isbn, reviewId)
        }

        val existing = reviews[index]
        val updated = existing.copy(
            rating = rating ?: existing.rating,
            comment = comment ?: existing.comment
        )
        reviews[index] = updated
        return updated
    }

    fun deleteReview(isbn: String, reviewId: UUID) {
        getBook(isbn)
        val reviews = reviewsByBookIsbn[isbn] ?: throw ReviewNotFoundException(isbn, reviewId)
        val removed = reviews.removeIf { it.id == reviewId }
        if (!removed) {
            throw ReviewNotFoundException(isbn, reviewId)
        }
    }
}