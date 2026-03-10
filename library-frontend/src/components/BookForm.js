function BookForm({
  fieldErrors,
  formData,
  formError,
  onChange,
  onSubmit,
  submitting,
  successMessage,
}) {
  return (
    <form className="create-form" onSubmit={onSubmit}>
      <label>
        Title
        <input
          name="title"
          maxLength="50"
          value={formData.title}
          onChange={onChange}
          required
        />
        {fieldErrors.title ? <span className="field-error">{fieldErrors.title}</span> : null}
      </label>
      <label>
        Author
        <input
          name="author"
          maxLength="50"
          value={formData.author}
          onChange={onChange}
          required
        />
        {fieldErrors.author ? <span className="field-error">{fieldErrors.author}</span> : null}
      </label>
      <label>
        Genre
        <input
          name="genre"
          maxLength="50"
          value={formData.genre}
          onChange={onChange}
          required
        />
        {fieldErrors.genre ? <span className="field-error">{fieldErrors.genre}</span> : null}
      </label>
      <label>
        Pages
        <input
          type="number"
          name="pages"
          min="1"
          max="10000"
          value={formData.pages}
          onChange={onChange}
          required
        />
        {fieldErrors.pages ? <span className="field-error">{fieldErrors.pages}</span> : null}
      </label>
      <label>
        Published Year
        <input
          type="number"
          name="publishedYear"
          min="1996"
          max="2026"
          value={formData.publishedYear}
          onChange={onChange}
          required
        />
        {fieldErrors.publishedYear ? (
          <span className="field-error">{fieldErrors.publishedYear}</span>
        ) : null}
      </label>

      {formError ? <p className="feedback error-text">{formError}</p> : null}
      {successMessage ? <p className="feedback success-text">{successMessage}</p> : null}

      <button type="submit" disabled={submitting}>
        {submitting ? 'Saving...' : 'Create Book'}
      </button>
    </form>
  );
}

export default BookForm;
