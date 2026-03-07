package pt.unl.fct.iadi.bookstore.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody as OasRequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import pt.unl.fct.iadi.bookstore.controller.dto.CreateBookRequest
import pt.unl.fct.iadi.bookstore.controller.dto.CreateReviewRequest
import pt.unl.fct.iadi.bookstore.controller.dto.ErrorResponse
import pt.unl.fct.iadi.bookstore.controller.dto.GetBookResponse
import pt.unl.fct.iadi.bookstore.controller.dto.GetReviewResponse
import pt.unl.fct.iadi.bookstore.controller.dto.UpdateBookPatchRequest
import pt.unl.fct.iadi.bookstore.controller.dto.UpdateReviewPatchRequest
import java.util.UUID

interface BookstoreAPI {

    @Operation(summary = "Create a book", description = "Registers a new book in the catalog.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Book created"),
            ApiResponse(
                responseCode = "400",
                description = "Validation error",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "409",
                description = "Book already exists",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping("/books")
    fun bookstoreAdd(
        @OasRequestBody(
            required = true,
            description = "Complete book representation",
            content = [Content(schema = Schema(implementation = CreateBookRequest::class))]
        )
        @Valid @RequestBody book: CreateBookRequest
    ): ResponseEntity<Void>

    @Operation(summary = "Replace a book", description = "Fully replaces a book by ISBN, creating it if absent.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Book replaced",
                content = [Content(schema = Schema(implementation = GetBookResponse::class))]
            ),
            ApiResponse(
                responseCode = "201",
                description = "Book created by upsert",
                content = [Content(schema = Schema(implementation = GetBookResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Validation or ISBN mismatch error",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PutMapping("/books/{isbn}")
    fun bookstoreUpdate(
        @Parameter(description = "ISBN of the target book", example = "9780134685991")
        @PathVariable isbn: String,
        @OasRequestBody(
            required = true,
            description = "Complete book representation",
            content = [Content(schema = Schema(implementation = CreateBookRequest::class))]
        )
        @Valid @RequestBody book: CreateBookRequest
    ): ResponseEntity<GetBookResponse>

    @Operation(summary = "Partially update a book", description = "Updates selected fields of a book by ISBN.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Book updated",
                content = [Content(schema = Schema(implementation = GetBookResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Validation error",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Book not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PatchMapping("/books/{isbn}")
    fun bookstorePatch(
        @Parameter(description = "ISBN of the target book", example = "9780134685991")
        @PathVariable isbn: String,
        @OasRequestBody(
            required = true,
            description = "Partial book update",
            content = [Content(schema = Schema(implementation = UpdateBookPatchRequest::class))]
        )
        @Valid @RequestBody book: UpdateBookPatchRequest
    ): ResponseEntity<GetBookResponse>

    @Operation(summary = "Delete a book", description = "Deletes a book and all its reviews.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Book deleted"),
            ApiResponse(
                responseCode = "404",
                description = "Book not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @DeleteMapping("/books/{isbn}")
    fun bookstoreRemove(
        @Parameter(description = "ISBN of the target book", example = "9780134685991")
        @PathVariable isbn: String
    ): ResponseEntity<Void>

    @Operation(summary = "Get a book", description = "Retrieves a single book by ISBN.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Book found",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = GetBookResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Book not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping("/books/{isbn}")
    fun bookstoreGet(
        @Parameter(description = "ISBN of the target book", example = "9780134685991")
        @PathVariable isbn: String
    ): ResponseEntity<GetBookResponse>

    @Operation(summary = "List books", description = "Lists all books in the catalog.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Books retrieved",
                content = [
                    Content(
                        array = ArraySchema(schema = Schema(implementation = GetBookResponse::class))
                    )
                ]
            )
        ]
    )
    @GetMapping("/books")
    fun bookstoreGetAll(): ResponseEntity<List<GetBookResponse>>

    @Operation(summary = "List reviews", description = "Lists all reviews for a book.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Reviews retrieved",
                content = [
                    Content(
                        array = ArraySchema(schema = Schema(implementation = GetReviewResponse::class))
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Book not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping("/books/{isbn}/reviews")
    fun bookstoreGetReviews(
        @Parameter(description = "ISBN of the target book", example = "9780134685991")
        @PathVariable isbn: String
    ): ResponseEntity<List<GetReviewResponse>>

    @Operation(summary = "Create review", description = "Creates a new review for a specific book.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Review created"),
            ApiResponse(
                responseCode = "400",
                description = "Validation error",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Book not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping("/books/{isbn}/reviews")
    fun bookstoreAddReview(
        @Parameter(description = "ISBN of the target book", example = "9780134685991")
        @PathVariable isbn: String,
        @OasRequestBody(
            required = true,
            description = "Full review representation",
            content = [Content(schema = Schema(implementation = CreateReviewRequest::class))]
        )
        @Valid @RequestBody review: CreateReviewRequest
    ): ResponseEntity<Void>

    @Operation(summary = "Replace review", description = "Fully replaces a review by id.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Review replaced",
                content = [Content(schema = Schema(implementation = GetReviewResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Validation error",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Book or review not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PutMapping("/books/{isbn}/reviews/{reviewId}")
    fun bookstoreReplaceReview(
        @Parameter(description = "ISBN of the target book", example = "9780134685991")
        @PathVariable isbn: String,
        @Parameter(description = "Review identifier", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable reviewId: UUID,
        @OasRequestBody(
            required = true,
            description = "Full review representation",
            content = [Content(schema = Schema(implementation = CreateReviewRequest::class))]
        )
        @Valid @RequestBody review: CreateReviewRequest
    ): ResponseEntity<GetReviewResponse>

    @Operation(summary = "Patch review", description = "Partially updates rating and/or comment of a review.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Review updated",
                content = [Content(schema = Schema(implementation = GetReviewResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Validation error",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Book or review not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PatchMapping("/books/{isbn}/reviews/{reviewId}")
    fun bookstorePatchReview(
        @Parameter(description = "ISBN of the target book", example = "9780134685991")
        @PathVariable isbn: String,
        @Parameter(description = "Review identifier", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable reviewId: UUID,
        @OasRequestBody(
            required = true,
            description = "Partial review update",
            content = [Content(schema = Schema(implementation = UpdateReviewPatchRequest::class))]
        )
        @Valid @RequestBody review: UpdateReviewPatchRequest
    ): ResponseEntity<GetReviewResponse>

    @Operation(summary = "Delete review", description = "Deletes a review from a book.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Review deleted"),
            ApiResponse(
                responseCode = "404",
                description = "Book or review not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @DeleteMapping("/books/{isbn}/reviews/{reviewId}")
    fun bookstoreDeleteReview(
        @Parameter(description = "ISBN of the target book", example = "9780134685991")
        @PathVariable isbn: String,
        @Parameter(description = "Review identifier", example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable reviewId: UUID
    ): ResponseEntity<Void>
}
