package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class WishlistItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "added_at")
    private LocalDateTime eklenmeTarihi = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "wishlist_id")
    private Wishlist istekListesi;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book kitap;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getAddedAt() { return eklenmeTarihi; }
    public void setAddedAt(LocalDateTime eklenmeTarihi) { this.eklenmeTarihi = eklenmeTarihi; }
    public Wishlist getWishlist() { return istekListesi; }
    public void setWishlist(Wishlist istekListesi) { this.istekListesi = istekListesi; }
    public Book getBook() { return kitap; }
    public void setBook(Book kitap) { this.kitap = kitap; }
}
