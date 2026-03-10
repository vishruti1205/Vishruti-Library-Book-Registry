import { useState } from 'react';
import './App.css';
import BookForm from './components/BookForm';
import { createBook, deleteBookById, getBookById, updateBookById } from './services/bookApi';

const emptyForm = {
  title: '',
  author: '',
  genre: '',
  pages: '',
  publishedYear: '',
};

function App() {
  const [selectedBook, setSelectedBook] = useState(null);
  const [searchId, setSearchId] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [actionMessage, setActionMessage] = useState('');
  const [searchError, setSearchError] = useState('');
  const [formError, setFormError] = useState('');
  const [fieldErrors, setFieldErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState('');
  const [formData, setFormData] = useState(emptyForm);

  async function handleSearchBook(event) {
    event.preventDefault();
    setSearchError('');
    setActionMessage('');

    if (!searchId.trim()) {
      setSearchError('Enter a book ID.');
      return;
    }

    try {
      const selected = await getBookById(searchId.trim());
      setSelectedBook(selected);
    } catch (requestError) {
      setSelectedBook(null);
      setSearchError(getApiErrorMessage(requestError, 'Unable to load the selected book.'));
    }
  }

  function handleFormChange(event) {
    const { name, value } = event.target;
    setFormData((current) => ({ ...current, [name]: value }));
  }

  async function handleCreateBook(event) {
    event.preventDefault();
    setSubmitting(true);
    setFormError('');
    setFieldErrors({});
    setSuccessMessage('');

    try {
      const createdBook = await createBook(formData);
      setFormData(emptyForm);
      setSuccessMessage(`Book "${createdBook.title}" was added.`);
    } catch (requestError) {
      setFormError(getApiErrorMessage(requestError, 'Unable to create the book.'));
      setFieldErrors(requestError.response?.data?.validationErrors || {});
    } finally {
      setSubmitting(false);
    }
  }

  async function handleEditClick() {
    if (!selectedBook) {
      return;
    }

    setActionMessage('');

    try {
      await updateBookById(selectedBook.id, selectedBook);
    } catch (requestError) {
      setActionMessage(getApiErrorMessage(requestError, 'Unable to update the selected book.'));
    }
  }

  async function handleDeleteClick() {
    if (!selectedBook) {
      return;
    }

    setActionMessage('');

    try {
      await deleteBookById(selectedBook.id);
    } catch (requestError) {
      setActionMessage(getApiErrorMessage(requestError, 'Unable to delete the selected book.'));
    }
  }

  return (
    <div className="app-shell">
      <header className="page-header">
        <h1>Library Book Registry</h1>
      </header>

      <main className="dashboard-grid">
        <section className="panel form-panel">
          <h2>Add Book</h2>

          <BookForm
            fieldErrors={fieldErrors}
            formData={formData}
            formError={formError}
            onChange={handleFormChange}
            onSubmit={handleCreateBook}
            submitting={submitting}
            successMessage={successMessage}
          />
        </section>

        <section className="panel">
          <h2>Find Book By ID</h2>

          <form className="search-form" onSubmit={handleSearchBook}>
            <label>
              Enter ID
              <input
                type="number"
                min="1"
                value={searchId}
                onChange={(event) => setSearchId(event.target.value)}
              />
            </label>
            <button type="submit">Search</button>
          </form>

          {searchError ? <p className="feedback error-text">{searchError}</p> : null}

          {selectedBook ? (
            <div className="selected-details">
              <h3>Result</h3>
              <p><strong>ID:</strong> {selectedBook.id}</p>
              <p><strong>Title:</strong> {selectedBook.title}</p>
              <p><strong>Author:</strong> {selectedBook.author}</p>
              <p><strong>Genre:</strong> {selectedBook.genre}</p>
              <p><strong>Pages:</strong> {selectedBook.pages}</p>
              <p><strong>Published Year:</strong> {selectedBook.publishedYear}</p>
              <div className="action-row">
                <button type="button" onClick={handleEditClick}>Edit</button>
                <button type="button" onClick={handleDeleteClick}>Delete</button>
              </div>
              {actionMessage ? <p className="feedback error-text">{actionMessage}</p> : null}
            </div>
          ) : null}
        </section>
      </main>
    </div>
  );
}

function getApiErrorMessage(error, fallbackMessage) {
  const responseData = error.response?.data;

  if (typeof responseData === 'string' && responseData.trim()) {
    return responseData;
  }

  if (responseData?.message) {
    return responseData.message;
  }

  return fallbackMessage;
}

export default App;
