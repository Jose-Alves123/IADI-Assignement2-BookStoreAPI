package pt.unl.fct.iadi.bookstore.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.URL
import pt.unl.fct.iadi.bookstore.domain.Book

class CreateBookRequest(

    @field:Schema(description = "Unique ISBN identifier", example = "9780134685991", required = true)
    @field:NotBlank
    val isbn : String,

    @field:NotBlank
    @field:Size(min = 1, max = 120)
    @field:Schema(description = "Title of the book", example = "Effective Java", required = true)
    val title : String,

    @field:NotBlank
    @field:Size(min = 1, max = 80)
    @field:Schema(description = "Author of the book", example = "Joshua Bloch", required = true)
    val author : String,

    @field:Positive
    @field:Schema(
        description = "Price of the book, must be greater than zero",
        example = "29.99",
        required = true,
        minimum = "0.01"
    )
    val price : Double,

    @field:NotBlank
    @field:URL
    @field:Schema(description = "URL to the book cover image", example = "https://example.com/cover.jpg")
    val image : String

){
    fun toBook(): Book {
        return Book(
            isbn = isbn,
            title = title,
            author = author,
            price = price,
            image = image
        )
    }
}
