package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String kitapAdi;

    private String isbn;

    @Column(name = "description")
    private String aciklama;

    @Column(name = "price")
    private BigDecimal fiyat;
    
    @Column(name = "stock_quantity")
    private int stokMiktari;
    
    @Column(name = "image_url")
    private String resimUrl;
    
    @ManyToMany
    @JoinTable(
        name = "book_categories",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> kategoriler;
    
    @ManyToMany
    @JoinTable(
        name = "book_authors",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> yazarlar;
    
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher yayinevi;

    @OneToMany(mappedBy = "kitap")
    private List<Review> yorumlar;

    @OneToMany(mappedBy = "kitap")
    private List<WishlistItem> istekListesiUrunleri;

    @OneToMany(mappedBy = "kitap")
    private List<InventoryLog> stokHareketleri;

    @ManyToMany
    @JoinTable(
        name = "book_tags",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<BookTag> etiketler;

    public Book() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return kitapAdi;
    }

    public void setTitle(String kitapAdi) {
        this.kitapAdi = kitapAdi;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return aciklama;
    }

    public void setDescription(String aciklama) {
        this.aciklama = aciklama;
    }

    public BigDecimal getPrice() {
        return fiyat;
    }

    public void setPrice(BigDecimal fiyat) {
        this.fiyat = fiyat;
    }

    public int getStockQuantity() {
        return stokMiktari;
    }

    public void setStockQuantity(int stokMiktari) {
        this.stokMiktari = stokMiktari;
    }

    public String getImageUrl() {
        return resimUrl;
    }

    public void setImageUrl(String resimUrl) {
        this.resimUrl = resimUrl;
    }

    public List<Category> getCategories() {
        return kategoriler;
    }

    public void setCategories(List<Category> kategoriler) {
        this.kategoriler = kategoriler;
    }

    public List<Author> getAuthors() {
        return yazarlar;
    }

    public void setAuthors(List<Author> yazarlar) {
        this.yazarlar = yazarlar;
    }

    public Publisher getPublisher() {
        return yayinevi;
    }

    public void setPublisher(Publisher yayinevi) {
        this.yayinevi = yayinevi;
    }

    public List<Review> getReviews() {
        return yorumlar;
    }

    public void setReviews(List<Review> yorumlar) {
        this.yorumlar = yorumlar;
    }

    public List<WishlistItem> getWishlistItems() {
        return istekListesiUrunleri;
    }

    public void setWishlistItems(List<WishlistItem> istekListesiUrunleri) {
        this.istekListesiUrunleri = istekListesiUrunleri;
    }

    public List<InventoryLog> getInventoryLogs() {
        return stokHareketleri;
    }

    public void setInventoryLogs(List<InventoryLog> stokHareketleri) {
        this.stokHareketleri = stokHareketleri;
    }

    public List<BookTag> getTags() {
        return etiketler;
    }

    public void setTags(List<BookTag> etiketler) {
        this.etiketler = etiketler;
    }
}
