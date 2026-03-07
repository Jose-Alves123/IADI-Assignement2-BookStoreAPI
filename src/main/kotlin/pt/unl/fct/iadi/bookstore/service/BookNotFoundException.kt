package pt.unl.fct.iadi.bookstore.service

class BookNotFoundException(val isbn: String) : RuntimeException()
