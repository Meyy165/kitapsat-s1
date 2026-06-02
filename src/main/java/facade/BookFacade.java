package facade;

import entity.Book;
import entity.Author;
import entity.Publisher;
import entity.Category;
import facadeLocal.BookFacadeLocal;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Stateless
public class BookFacade extends AbstractFacade implements BookFacadeLocal {


    @Override
    public void createBook(Book book) {
        this.entityManager.persist(book);
        this.entityManager.flush();
    }

    @Override
    public Book editBook(Book book) {
        this.entityManager.merge(book);
        this.entityManager.flush();
        return book;
    }

    @Override
    public void remove(Book book) {
        Book merged = this.entityManager.merge(book);
        this.entityManager.remove(merged);
    }

    @Override
    public Book find(Long id) {
        return this.entityManager.find(Book.class, id);
    }

    @Override
    public List<Book> findAll() {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> root = cq.from(Book.class);
        CriteriaQuery<Book> all = cq.select(root);
        TypedQuery<Book> q = this.entityManager.createQuery(all);
        return q.getResultList();
    }

    @Override
    public List<Book> findByTitle(String title) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> root = cq.from(Book.class);
        cq.where(cb.like(cb.lower(root.get("kitapAdi")), "%" + title.toLowerCase(TURKISH) + "%"));
        CriteriaQuery<Book> all = cq.select(root);
        TypedQuery<Book> q = this.entityManager.createQuery(all);
        return q.getResultList();
    }

    @Override
    public List<Book> findByCategory(Long categoryId) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> root = cq.from(Book.class);
        cq.where(cb.equal(root.join("kategoriler").get("id"), categoryId));
        CriteriaQuery<Book> all = cq.select(root);
        TypedQuery<Book> q = this.entityManager.createQuery(all);
        return q.getResultList();
    }

    @Override
    public List<Book> findByAuthor(Long authorId) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> root = cq.from(Book.class);
        cq.where(cb.equal(root.join("yazarlar").get("id"), authorId));
        CriteriaQuery<Book> all = cq.select(root);
        TypedQuery<Book> q = this.entityManager.createQuery(all);
        return q.getResultList();
    }

    @Override
    public List<Book> searchBooks(String keyword) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> root = cq.from(Book.class);
        
        List<Predicate> predicates = new ArrayList<>();
        String cleanKeyword = keyword.trim().toLowerCase(TURKISH);
        predicates.add(cb.like(cb.lower(root.get("kitapAdi")), "%" + cleanKeyword + "%"));
        predicates.add(cb.like(cb.lower(root.get("isbn")), "%" + cleanKeyword + "%"));
        predicates.add(cb.like(cb.lower(root.get("aciklama")), "%" + cleanKeyword + "%"));
        
        cq.where(cb.or(predicates.toArray(new Predicate[0])));
        CriteriaQuery<Book> all = cq.select(root);
        TypedQuery<Book> q = this.entityManager.createQuery(all);
        return q.getResultList();
    }

    @Override
    public void ensureMinimumBooks(int targetCount) {
        long currentCount = countBooks();
        if (currentCount >= targetCount) {
            return;
        }

        List<Author> authors = ensureSeedAuthors();
        List<Category> categories = ensureSeedCategories();
        List<Publisher> publishers = ensureSeedPublishers();
        int seedIndex = 1;

        while (currentCount < targetCount) {
            String isbn = "9786259" + String.format("%06d", seedIndex);
            if (!bookExistsByIsbn(isbn)) {
                Book book = new Book();
                String title = seedTitle(seedIndex);
                book.setTitle(title);
                book.setIsbn(isbn);
                book.setDescription(seedDescription(seedIndex));
                book.setPrice(BigDecimal.valueOf(80 + ((seedIndex * 9L) % 190)));
                book.setStockQuantity(6 + ((seedIndex * 7) % 44));
                book.setImageUrl(seedImageUrl(title, seedIndex));
                book.setPublisher(publishers.get((seedIndex - 1) % publishers.size()));
                book.setAuthors(Collections.singletonList(authors.get((seedIndex - 1) % authors.size())));
                book.setCategories(Collections.singletonList(categories.get((seedIndex - 1) % categories.size())));
                this.entityManager.persist(book);
                currentCount++;
            }
            seedIndex++;
        }
        this.entityManager.flush();
    }

    private long countBooks() {
        return this.entityManager.createQuery("select count(b) from Book b", Long.class).getSingleResult();
    }

    private boolean bookExistsByIsbn(String isbn) {
        Long count = this.entityManager
                .createQuery("select count(b) from Book b where b.isbn = :isbn", Long.class)
                .setParameter("isbn", isbn)
                .getSingleResult();
        return count > 0;
    }

    private List<Author> ensureSeedAuthors() {
        String[][] data = {
                {"Ada", "Yilmaz"}, {"Mert", "Kaya"}, {"Elif", "Demir"}, {"Deniz", "Arslan"},
                {"Selin", "Aydin"}, {"Kerem", "Celik"}, {"Nehir", "Sahin"}, {"Baran", "Koc"},
                {"Derya", "Yildiz"}, {"Ege", "Aksoy"}, {"Mina", "Eren"}, {"Arda", "Polat"}
        };
        List<Author> result = new ArrayList<>();
        for (String[] row : data) {
            result.add(findOrCreateAuthor(row[0], row[1]));
        }
        return result;
    }

    private List<Category> ensureSeedCategories() {
        String[] names = {"Roman", "Klasik", "Turk Edebiyati", "Bilim Kurgu", "Polisiye", "Kisisel Gelisim",
                "Tarih", "Cocuk", "Felsefe", "Biyografi", "Psikoloji", "Is Dunyasi"};
        List<Category> result = new ArrayList<>();
        for (String name : names) {
            result.add(findOrCreateCategory(name));
        }
        return result;
    }

    private List<Publisher> ensureSeedPublishers() {
        String[] names = {"Rafine Yayinlari", "Kuzey Kitap", "Mavi Sayfa", "Kent Yayin", "Atlas Kitap", "Liman Yayin"};
        List<Publisher> result = new ArrayList<>();
        for (String name : names) {
            result.add(findOrCreatePublisher(name));
        }
        return result;
    }

    private Author findOrCreateAuthor(String name, String surname) {
        List<Author> matches = this.entityManager
                .createQuery("select a from Author a where lower(a.ad) = :name and lower(a.soyad) = :surname", Author.class)
                .setParameter("name", name.toLowerCase(TURKISH))
                .setParameter("surname", surname.toLowerCase(TURKISH))
                .setMaxResults(1)
                .getResultList();
        if (!matches.isEmpty()) {
            return matches.get(0);
        }
        Author author = new Author();
        author.setName(name);
        author.setSurname(surname);
        author.setBiography("Rafine Kitap katalog yazari.");
        this.entityManager.persist(author);
        return author;
    }

    private Category findOrCreateCategory(String name) {
        List<Category> matches = this.entityManager
                .createQuery("select c from Category c where lower(c.ad) = :name", Category.class)
                .setParameter("name", name.toLowerCase(TURKISH))
                .setMaxResults(1)
                .getResultList();
        if (!matches.isEmpty()) {
            return matches.get(0);
        }
        Category category = new Category();
        category.setName(name);
        category.setDescription(name + " kitaplari");
        this.entityManager.persist(category);
        return category;
    }

    private Publisher findOrCreatePublisher(String name) {
        List<Publisher> matches = this.entityManager
                .createQuery("select p from Publisher p where lower(p.ad) = :name", Publisher.class)
                .setParameter("name", name.toLowerCase(TURKISH))
                .setMaxResults(1)
                .getResultList();
        if (!matches.isEmpty()) {
            return matches.get(0);
        }
        Publisher publisher = new Publisher();
        publisher.setName(name);
        publisher.setAddress("Istanbul");
        publisher.setPhone("0212 000 00 00");
        publisher.setEmail(name.toLowerCase(TURKISH).replace(" ", "") + "@rafinekitap.local");
        this.entityManager.persist(publisher);
        return publisher;
    }

    private String seedTitle(int index) {
        String[] titles = {
                "Sessiz Liman", "Kuzey Defteri", "Geceye Kalan", "Mavi Harita", "Sonbahar Notlari",
                "Kirik Saatler", "Uzak Bahce", "Golge Atlas", "Yol Ayrimi", "Cam Kent",
                "Kutuphane Sirri", "Sakli Mektup", "Denizin Hafizasi", "Yitik Mevsim", "Kagit Ev",
                "Ruzgarin Izinde", "Kucuk Istasyon", "Eski Sokaklar", "Zamanin Kapisi", "Derin Uyku"
        };
        return titles[(index - 1) % titles.length] + " " + String.format("%03d", index);
    }

    private String seedDescription(int index) {
        String[] descriptions = {
                "Akici anlatimi ve guclu karakterleriyle raflarda one cikan bir kitap.",
                "Modern okur icin tempolu, sade ve etkileyici bir secim.",
                "Kitap kulubu, hafta sonu okumasi ve hediye secimi icin uygun.",
                "Duygusal derinligi olan, kolay okunan ve surukleyici bir eser.",
                "Merak duygusunu canli tutan, temiz kurgulanmis bir okuma deneyimi."
        };
        return descriptions[(index - 1) % descriptions.length];
    }

    private String seedImageUrl(String title, int index) {
        String[] colors = {"172554", "0f766e", "be123c", "7c2d12", "365314", "4338ca", "854d0e", "334155"};
        return "https://placehold.co/300x450/" + colors[(index - 1) % colors.length]
                + "/ffffff?text=" + title.replace(" ", "+");
    }

    @Override
    public List<Author> findAllAuthors() {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Author> cq = cb.createQuery(Author.class);
        Root<Author> root = cq.from(Author.class);
        CriteriaQuery<Author> all = cq.select(root);
        TypedQuery<Author> q = this.entityManager.createQuery(all);
        return q.getResultList();
    }

    @Override
    public List<Publisher> findAllPublishers() {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Publisher> cq = cb.createQuery(Publisher.class);
        Root<Publisher> root = cq.from(Publisher.class);
        CriteriaQuery<Publisher> all = cq.select(root);
        TypedQuery<Publisher> q = this.entityManager.createQuery(all);
        return q.getResultList();
    }

    @Override
    public List<Category> findAllCategories() {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Category> cq = cb.createQuery(Category.class);
        Root<Category> root = cq.from(Category.class);
        CriteriaQuery<Category> all = cq.select(root);
        TypedQuery<Category> q = this.entityManager.createQuery(all);
        return q.getResultList();
    }

    @Override
    public void createAuthor(Author author) {
        this.entityManager.persist(author);
        this.entityManager.flush();
    }

    @Override
    public void createPublisher(Publisher publisher) {
        this.entityManager.persist(publisher);
        this.entityManager.flush();
    }

    @Override
    public void createCategory(Category category) {
        this.entityManager.persist(category);
        this.entityManager.flush();
    }

    @Override
    public Author editAuthor(Author author) {
        this.entityManager.merge(author);
        this.entityManager.flush();
        return author;
    }

    @Override
    public Publisher editPublisher(Publisher publisher) {
        this.entityManager.merge(publisher);
        this.entityManager.flush();
        return publisher;
    }

    @Override
    public Category editCategory(Category category) {
        this.entityManager.merge(category);
        this.entityManager.flush();
        return category;
    }

    @Override
    public void removeAuthor(Author author) {
        Author merged = this.entityManager.merge(author);
        this.entityManager.remove(merged);
    }

    @Override
    public void removePublisher(Publisher publisher) {
        Publisher merged = this.entityManager.merge(publisher);
        this.entityManager.remove(merged);
    }

    @Override
    public void removeCategory(Category category) {
        Category merged = this.entityManager.merge(category);
        this.entityManager.remove(merged);
    }

    @Override
    public Author findAuthor(Long id) {
        return this.entityManager.find(Author.class, id);
    }

    @Override
    public Publisher findPublisher(Long id) {
        return this.entityManager.find(Publisher.class, id);
    }

    @Override
    public Category findCategory(Long id) {
        return this.entityManager.find(Category.class, id);
    }
}
