package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class CartItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private int adet;
    
    @Column(name = "unit_price")
    private BigDecimal birimFiyat;
    
    @Column(name = "total_price")
    private BigDecimal toplamFiyat;
    
    @ManyToOne
    @JoinColumn(name = "shoppingcart_id")
    private ShoppingCart alisverisSepeti;
    
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book kitap;

    public CartItem() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return adet;
    }

    public void setQuantity(int adet) {
        this.adet = adet;
    }

    public BigDecimal getUnitPrice() {
        return birimFiyat;
    }

    public void setUnitPrice(BigDecimal birimFiyat) {
        this.birimFiyat = birimFiyat;
    }

    public BigDecimal getTotalPrice() {
        return toplamFiyat;
    }

    public void setTotalPrice(BigDecimal toplamFiyat) {
        this.toplamFiyat = toplamFiyat;
    }

    public ShoppingCart getShoppingCart() {
        return alisverisSepeti;
    }

    public void setShoppingCart(ShoppingCart alisverisSepeti) {
        this.alisverisSepeti = alisverisSepeti;
    }

    public Book getBook() {
        return kitap;
    }

    public void setBook(Book kitap) {
        this.kitap = kitap;
    }
}
