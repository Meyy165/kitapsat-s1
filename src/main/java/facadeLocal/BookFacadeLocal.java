package facadeLocal;

import entity.Book;
import entity.Author;
import entity.Publisher;
import entity.Category;
import java.util.List;

public interface BookFacadeLocal {
    void createBook(Book book);
    Book editBook(Book book);
    void remove(Book book);
    Book find(Long id);
    List<Book> findAll();
    List<Book> findByTitle(String title);
    List<Book> findByCategory(Long categoryId);
    List<Book> findByAuthor(Long authorId);
    List<Book> searchBooks(String keyword);
    void ensureMinimumBooks(int targetCount);
    
    List<Author> findAllAuthors();
    List<Publisher> findAllPublishers();
    List<Category> findAllCategories();
    
    void createAuthor(Author author);
    void createPublisher(Publisher publisher);
    void createCategory(Category category);
    
    Author editAuthor(Author author);
    Publisher editPublisher(Publisher publisher);
    Category editCategory(Category category);
    
    void removeAuthor(Author author);
    void removePublisher(Publisher publisher);
    void removeCategory(Category category);
    
    Author findAuthor(Long id);
    Publisher findPublisher(Long id);
    Category findCategory(Long id);
}
