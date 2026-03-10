package org.revature.book;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateBook() throws Exception {
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "The Great Gatsby",
                                  "author": "F. Scott Fitzgerald",
                                  "genre": "Fiction",
                                  "pages": 180,
                                  "publishedYear": 1925
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/books/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("The Great Gatsby"));
    }

    @Test
    void shouldReturnBooksFilteredByAuthorAndGenre() throws Exception {
        createBook("1984", "George Orwell", "Dystopian", 328, 1949);
        createBook("Animal Farm", "George Orwell", "Satire", 112, 1945);
        createBook("Pride and Prejudice", "Jane Austen", "Romance", 279, 1813);

        mockMvc.perform(get("/books")
                        .param("author", "George Orwell")
                        .param("genre", "Satire"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].title").value("Animal Farm"));
    }

    @Test
    void shouldReturnBookById() throws Exception {
        createBook("Dune", "Frank Herbert", "Science Fiction", 412, 1965);

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("Frank Herbert"))
                .andExpect(jsonPath("$.publishedYear").value(1965));
    }

    @Test
    void shouldReturnNotFoundForMissingBook() throws Exception {
        mockMvc.perform(get("/books/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book with id 99 was not found."));
    }

    @Test
    void shouldRejectDisallowedMethodsOnBookById() throws Exception {
        createBook("Dune", "Frank Herbert", "Science Fiction", 412, 1965);

        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isMethodNotAllowed());

        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isMethodNotAllowed());

        mockMvc.perform(patch("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void shouldValidateBookPayload() throws Exception {
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "",
                                  "author": "  ",
                                  "genre": "Fiction",
                                  "pages": 0,
                                  "publishedYear": 1200
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.title").value("Title is required."))
                .andExpect(jsonPath("$.validationErrors.author").value("Author is required."))
                .andExpect(jsonPath("$.validationErrors.pages").value("Pages must be at least 1."))
                .andExpect(jsonPath("$.validationErrors.publishedYear").value("Published year must be 1450 or later."));
    }

    private void createBook(String title, String author, String genre, int pages, int publishedYear) throws Exception {
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "%s",
                                  "author": "%s",
                                  "genre": "%s",
                                  "pages": %d,
                                  "publishedYear": %d
                                }
                                """.formatted(title, author, genre, pages, publishedYear)))
                .andExpect(status().isCreated());
    }
}
