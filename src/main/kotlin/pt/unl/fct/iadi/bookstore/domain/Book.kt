package pt.unl.fct.iadi.bookstore.domain

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.URL

data class Book(
    @field:Schema(description = "Unique ISBN identifier", example = "9780134685991", required = true)
    @field:NotBlank
    @field:Size(min = 1, max = 30)
    val isbn : String,

    @field:Schema(description = "Title of the book", example = "Effective Java", required = true)
    @field:NotBlank
    @field:Size(min = 1, max = 120)
    val title : String,

    @field:Schema(description = "Author of the book", example = "Joshua Bloch", required = true)
    @field:NotBlank
    @field:Size(min = 1, max = 80)
    val author : String,

    @field:Schema(
        description = "Price of the book, must be greater than zero",
        example = "29.99",
        required = true,
        minimum = "0.01"
    )
    @field:Positive
    val price : Double,

    @field:Schema(description = "Remote URL of the book cover image", example = "https://example.com/cover.jpg", required = true)
    @field:NotBlank
    @field:URL
    val image : String
)
