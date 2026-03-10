import axios from 'axios';

export async function getBookById(bookId) {
  const response = await axios.get(`/books/${bookId}`);
  return response.data;
}

export async function createBook(formData) {
  const payload = {
    title: formData.title.trim(),
    author: formData.author.trim(),
    genre: formData.genre.trim(),
    pages: Number(formData.pages),
    publishedYear: Number(formData.publishedYear),
  };

  const response = await axios.post('/books', payload);
  return response.data;
}

export async function updateBookById(bookId, bookData) {
  const response = await axios.put(`/books/${bookId}`, bookData);
  return response.data;
}

export async function deleteBookById(bookId) {
  const response = await axios.delete(`/books/${bookId}`);
  return response.data;
}
