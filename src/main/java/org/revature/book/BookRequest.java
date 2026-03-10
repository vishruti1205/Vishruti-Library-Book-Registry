package org.revature.book;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BookRequest {

    @NotBlank(message = "Title is required.")
    @Size(max = 50, message = "Title must be 255 characters or fewer.")
    private String title;

    @NotBlank(message = "Author is required.")
    @Size(max = 50, message = "Author must be 50 characters or fewer.")
    private String author;

    @NotBlank(message = "Genre is required.")
    @Size(max = 50, message = "Genre must be 50 characters or fewer.")
    private String genre;

    @NotNull(message = "Pages is required.")
    @Min(value = 1, message = "Pages must be at least 1.")
    @Max(value = 10000, message = "Pages must be 10000 or fewer.")
    private Integer pages;

    @NotNull(message = "Published year is required.")
    @Min(value = 1996, message = "Published year must be 1996 or later.")
    @Max(value = 2026, message = "Published year must be 2026 or earlier.")
    private Integer publishedYear;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }
}
