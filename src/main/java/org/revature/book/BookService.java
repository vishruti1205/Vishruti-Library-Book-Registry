package org.revature.book;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book create(BookRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle().trim());
        book.setAuthor(request.getAuthor().trim());
        book.setGenre(request.getGenre().trim());
        book.setPages(request.getPages());
        book.setPublishedYear(request.getPublishedYear());
        return bookRepository.save(book);
    }

    public List<Book> findAll(String author, String genre) {
        List<Book> books = bookRepository.findAll(Sort.by("id"));

        if (author != null && !author.isBlank()) {
            books = books.stream()
                    .filter(book -> book.getAuthor().equalsIgnoreCase(author.trim()))
                    .toList();
        }

        if (genre != null && !genre.isBlank()) {
            books = books.stream()
                    .filter(book -> book.getGenre().equalsIgnoreCase(genre.trim()))
                    .toList();
        }

        return books;
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }
}
