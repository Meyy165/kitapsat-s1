package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "shopping_cart")
public class ShoppingCart implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_amount")
    private BigDecimal toplamTutar;
    
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private Users kullanici;
    
    @OneToMany(mappedBy = "alisverisSepeti", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> sepetUrunleri;

    public ShoppingCart() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalAmount() {
        return toplamTutar;
    }

    public void setTotalAmount(BigDecimal toplamTutar) {
        this.toplamTutar = toplamTutar;
    }

    public Users getUser() {
        return kullanici;
    }

    public void setUser(Users kullanici) {
        this.kullanici = kullanici;
    }

    public List<CartItem> getCartItems() {
        return sepetUrunleri;
    }

    public void setCartItems(List<CartItem> sepetUrunleri) {
        this.sepetUrunleri = sepetUrunleri;
    }
}
