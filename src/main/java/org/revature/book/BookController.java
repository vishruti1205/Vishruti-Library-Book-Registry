package org.revature.book;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<Book> create(@Valid @RequestBody BookRequest request) {
        Book savedBook = bookService.create(request);
        return ResponseEntity
                .created(URI.create("/books/" + savedBook.getId()))
                .body(savedBook);
    }

    @GetMapping
    public ResponseEntity<List<Book>> findAll(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre
    ) {
        return ResponseEntity.ok(bookService.findAll(author, genre));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Book> book = bookService.findById(id);

        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Book with id " + id + " was not found."));
    }
}
