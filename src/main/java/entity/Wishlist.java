package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Wishlist implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime olusturmaTarihi = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private Users kullanici;

    @OneToMany(mappedBy = "istekListesi", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistItem> urunler;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getCreatedAt() { return olusturmaTarihi; }
    public void setCreatedAt(LocalDateTime olusturmaTarihi) { this.olusturmaTarihi = olusturmaTarihi; }
    public Users getUser() { return kullanici; }
    public void setUser(Users kullanici) { this.kullanici = kullanici; }
    public List<WishlistItem> getItems() { return urunler; }
    public void setItems(List<WishlistItem> urunler) { this.urunler = urunler; }
}
