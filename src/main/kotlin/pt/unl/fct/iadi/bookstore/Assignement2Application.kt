package pt.unl.fct.iadi.bookstore

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@OpenAPIDefinition(
    info = Info(
        title = "Bookstore REST API",
        version = "1.0.0",
        description = "REST API for managing books and reviews"
    ),
    tags = [
        Tag(name = "Books", description = "Book catalog operations"),
        Tag(name = "Reviews", description = "Book review operations")
    ]
)
@SpringBootApplication
class Assignement2Application

fun main(args: Array<String>) {
    runApplication<Assignement2Application>(*args)
}
