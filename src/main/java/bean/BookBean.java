package bean;

import entity.Book;
import entity.Author;
import entity.Publisher;
import entity.Category;
import facadeLocal.BookFacadeLocal;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class BookBean implements Serializable {
    private Book book;
    private List<Book> books;
    private String searchKeyword;
    private List<Author> authors;
    private List<Publisher> publishers;
    private List<Category> categories;
    private Long selectedPublisherId;
    private List<Long> selectedAuthorIds;
    private List<Long> selectedCategoryIds;
    
    // Yeni ekleme alanları
    private Author newAuthor = new Author();
    private Publisher newPublisher = new Publisher();
    private Category newCategory = new Category();
    
    @EJB
    BookFacadeLocal bookFacade;

    public BookBean() {
        book = new Book();
    }

    @PostConstruct
    public void init() {
        // URL'den ID parametresini al
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String bookId = params.get("id");
        
        if (bookId != null && !bookId.isEmpty()) {
            try {
                Long id = Long.parseLong(bookId);
                book = bookFacade.find(id);
                // Edit modunda mevcut yayınevini seç
                if (book != null && book.getPublisher() != null) {
                    selectedPublisherId = book.getPublisher().getId();
                }
                
                // Edit modunda mevcut yazarları seç
                if (book != null && book.getAuthors() != null) {
                    selectedAuthorIds = new java.util.ArrayList<>();
                    for (Author author : book.getAuthors()) {
                        selectedAuthorIds.add(author.getId());
                    }
                }
                
                // Edit modunda mevcut kategorileri seç
                if (book != null && book.getCategories() != null) {
                    selectedCategoryIds = new java.util.ArrayList<>();
                    for (Category category : book.getCategories()) {
                        selectedCategoryIds.add(category.getId());
                    }
                }
            } catch (NumberFormatException e) {
                book = new Book();
            }
        }
        
        // Listeleri başlangıçta yükle
        getAuthors();
        getPublishers();
        getCategories();
    }

    public List<Book> getAllBooks() {
        if (books == null) {
            books = bookFacade.findAll();
        }
        return books;
    }

    public void searchBooks() {
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            books = bookFacade.searchBooks(searchKeyword);
        } else {
            books = bookFacade.findAll();
        }
    }

    public String resetSearch() {
        setSearchKeyword(null);
        books = null; // Reset to force reload
        return null; // Stay on same page
    }

    public String viewBook(Long bookId) {
        book = bookFacade.find(bookId);
        return "/book/detail.xhtml?faces-redirect=true&id=" + bookId;
    }

    public String addBook() {
        // Publisher ID'sini Publisher objesine çevir
        if (selectedPublisherId != null) {
            Publisher selectedPublisher = findPublisherById(selectedPublisherId);
            book.setPublisher(selectedPublisher);
        }
        
        // Author ID'lerini Author objelerine çevir
        if (selectedAuthorIds != null && !selectedAuthorIds.isEmpty()) {
            List<Author> selectedAuthors = findAuthorsByIds(selectedAuthorIds);
            book.setAuthors(selectedAuthors);
        }
        
        // Category ID'lerini Category objelerine çevir
        if (selectedCategoryIds != null && !selectedCategoryIds.isEmpty()) {
            List<Category> selectedCategories = findCategoriesByIds(selectedCategoryIds);
            book.setCategories(selectedCategories);
        }
        
        bookFacade.createBook(book);
        return "/admin/books.xhtml?faces-redirect=true";
    }

    public String updateBook() {
        // Publisher ID'sini Publisher objesine çevir
        if (selectedPublisherId != null) {
            Publisher selectedPublisher = findPublisherById(selectedPublisherId);
            book.setPublisher(selectedPublisher);
        }
        
        // Author ID'lerini Author objelerine çevir
        if (selectedAuthorIds != null && !selectedAuthorIds.isEmpty()) {
            List<Author> selectedAuthors = findAuthorsByIds(selectedAuthorIds);
            book.setAuthors(selectedAuthors);
        }
        
        // Category ID'lerini Category objelerine çevir
        if (selectedCategoryIds != null && !selectedCategoryIds.isEmpty()) {
            List<Category> selectedCategories = findCategoriesByIds(selectedCategoryIds);
            book.setCategories(selectedCategories);
        }
        
        bookFacade.editBook(book);
        return "/admin/books.xhtml?faces-redirect=true";
    }

    public String deleteBook(Long bookId) {
        Book bookToDelete = bookFacade.find(bookId);
        if (bookToDelete != null) {
            bookFacade.remove(bookToDelete);
        }
        return "/admin/books.xhtml?faces-redirect=true";
    }

    public String editBook(Long bookId) {
        book = bookFacade.find(bookId);
        return "/admin/edit-book.xhtml?faces-redirect=true";
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public List<Author> getAuthors() {
        if (authors == null) {
            authors = bookFacade.findAllAuthors();
        }
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Publisher> getPublishers() {
        if (publishers == null) {
            publishers = bookFacade.findAllPublishers();
        }
        return publishers;
    }

    public void setPublishers(List<Publisher> publishers) {
        this.publishers = publishers;
    }

    public List<Category> getCategories() {
        if (categories == null) {
            categories = bookFacade.findAllCategories();
        }
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    // Yeni ekleme metotları
    public void addAuthor() {
        if (newAuthor.getName() != null && !newAuthor.getName().trim().isEmpty()) {
            bookFacade.createAuthor(newAuthor);
            authors = null; // Listeyi yenile
            newAuthor = new Author(); // Formu temizle
        }
    }

    public void addPublisher() {
        if (newPublisher.getName() != null && !newPublisher.getName().trim().isEmpty()) {
            bookFacade.createPublisher(newPublisher);
            publishers = null; // Listeyi yenile
            newPublisher = new Publisher(); // Formu temizle
        }
    }

    public void addCategory() {
        if (newCategory.getName() != null && !newCategory.getName().trim().isEmpty()) {
            bookFacade.createCategory(newCategory);
            categories = null; // Listeyi yenile
            newCategory = new Category(); // Formu temizle
        }
    }

    // Getter ve Setter metotları
    public Author getNewAuthor() {
        return newAuthor;
    }

    public void setNewAuthor(Author newAuthor) {
        this.newAuthor = newAuthor;
    }

    public Publisher getNewPublisher() {
        return newPublisher;
    }

    public void setNewPublisher(Publisher newPublisher) {
        this.newPublisher = newPublisher;
    }

    public Category getNewCategory() {
        return newCategory;
    }

    public void setNewCategory(Category newCategory) {
        this.newCategory = newCategory;
    }

    public Long getSelectedPublisherId() {
        return selectedPublisherId;
    }

    public void setSelectedPublisherId(Long selectedPublisherId) {
        this.selectedPublisherId = selectedPublisherId;
    }

    private Publisher findPublisherById(Long id) {
        if (publishers != null) {
            for (Publisher publisher : publishers) {
                if (publisher.getId().equals(id)) {
                    return publisher;
                }
            }
        }
        return null;
    }

    private List<Author> findAuthorsByIds(List<Long> ids) {
        List<Author> result = new java.util.ArrayList<>();
        if (authors != null && ids != null) {
            for (Author author : authors) {
                if (ids.contains(author.getId())) {
                    result.add(author);
                }
            }
        }
        return result;
    }

    private List<Category> findCategoriesByIds(List<Long> ids) {
        List<Category> result = new java.util.ArrayList<>();
        if (categories != null && ids != null) {
            for (Category category : categories) {
                if (ids.contains(category.getId())) {
                    result.add(category);
                }
            }
        }
        return result;
    }

    public List<Long> getSelectedAuthorIds() {
        return selectedAuthorIds;
    }

    public void setSelectedAuthorIds(List<Long> selectedAuthorIds) {
        this.selectedAuthorIds = selectedAuthorIds;
    }

    public List<Long> getSelectedCategoryIds() {
        return selectedCategoryIds;
    }

    public void setSelectedCategoryIds(List<Long> selectedCategoryIds) {
        this.selectedCategoryIds = selectedCategoryIds;
    }
}
