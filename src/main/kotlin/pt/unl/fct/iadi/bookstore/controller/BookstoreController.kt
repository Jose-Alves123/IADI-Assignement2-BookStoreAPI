package pt.unl.fct.iadi.bookstore.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import pt.unl.fct.iadi.bookstore.controller.dto.CreateBookRequest
import pt.unl.fct.iadi.bookstore.controller.dto.CreateReviewRequest
import pt.unl.fct.iadi.bookstore.controller.dto.GetBookResponse
import pt.unl.fct.iadi.bookstore.controller.dto.GetReviewResponse
import pt.unl.fct.iadi.bookstore.controller.dto.UpdateBookPatchRequest
import pt.unl.fct.iadi.bookstore.controller.dto.UpdateReviewPatchRequest
import pt.unl.fct.iadi.bookstore.service.BookstoreService
import java.util.UUID

@RestController
@RequestMapping("/api")
class BookstoreController(private val service: BookstoreService) : BookstoreAPI {

    override fun bookstoreAdd(@Valid @RequestBody book: CreateBookRequest): ResponseEntity<Void> {
        service.createBook(book.toBook())
        val location =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{isbn}")
                        .buildAndExpand(book.isbn)
                        .toUri()

        return ResponseEntity.created(location).build()
    }

    override fun bookstoreUpdate(
            @PathVariable isbn: String,
            @Valid @RequestBody book: CreateBookRequest
    ): ResponseEntity<GetBookResponse> {
        if (isbn != book.isbn) {
            throw IllegalArgumentException("Path ISBN and body ISBN must match")
        }

        val wasCreated = service.replaceBook(isbn, book.toBook())
        val response = GetBookResponse.fromBook(service.getBook(isbn))

        val location =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{isbn}")
                        .buildAndExpand(book.isbn)
                        .toUri()
                        
        return if (wasCreated) {
            ResponseEntity.created(location).body(response)
        } else {
            ResponseEntity.ok(response)
        }
    }

    override fun bookstorePatch(
            @PathVariable isbn: String,
            @Valid @RequestBody book: UpdateBookPatchRequest
    ): ResponseEntity<GetBookResponse> {
        val updated =
                service.updateBookPartially(
                        isbn = isbn,
                        title = book.title,
                        author = book.author,
                        price = book.price,
                        image = book.image
                )

        return ResponseEntity.ok(GetBookResponse.fromBook(updated))
    }

    override fun bookstoreRemove(@PathVariable isbn: String): ResponseEntity<Void> {
        service.deleteBook(isbn)
        return ResponseEntity.noContent().build()
    }

    override fun bookstoreGet(@PathVariable isbn: String): ResponseEntity<GetBookResponse> {
        return ResponseEntity.ok(GetBookResponse.fromBook(service.getBook(isbn)))
    }

    override fun bookstoreGetAll(): ResponseEntity<List<GetBookResponse>> {
        val books = service.getAllBooks()
        val bookResponses = books.map { GetBookResponse.fromBook(it) }
        return ResponseEntity.ok<List<GetBookResponse>>(bookResponses)
    }

    override fun bookstoreGetReviews(@PathVariable isbn: String): ResponseEntity<List<GetReviewResponse>> {
        val reviews = service.getReviews(isbn).map { GetReviewResponse.fromReview(it) }
        return ResponseEntity.ok(reviews)
    }

    override fun bookstoreAddReview(
        @PathVariable isbn: String,
        @Valid @RequestBody review: CreateReviewRequest
    ): ResponseEntity<Void> {
        val created = service.createReview(isbn, review.rating, review.comment)
        val location =
            ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{reviewId}")
                .buildAndExpand(created.id)
                .toUri()

        return ResponseEntity.created(location).build()
    }

    override fun bookstoreReplaceReview(
        @PathVariable isbn: String,
        @PathVariable reviewId: UUID,
        @Valid @RequestBody review: CreateReviewRequest
    ): ResponseEntity<GetReviewResponse> {
        val replaced = service.replaceReview(isbn, reviewId, review.rating, review.comment)
        return ResponseEntity.ok(GetReviewResponse.fromReview(replaced))
    }

    override fun bookstorePatchReview(
        @PathVariable isbn: String,
        @PathVariable reviewId: UUID,
        @Valid @RequestBody review: UpdateReviewPatchRequest
    ): ResponseEntity<GetReviewResponse> {
        val patched = service.patchReview(isbn, reviewId, review.rating, review.comment)
        return ResponseEntity.ok(GetReviewResponse.fromReview(patched))
    }

    override fun bookstoreDeleteReview(
        @PathVariable isbn: String,
        @PathVariable reviewId: UUID
    ): ResponseEntity<Void> {
        service.deleteReview(isbn, reviewId)
        return ResponseEntity.noContent().build()
    }
}
