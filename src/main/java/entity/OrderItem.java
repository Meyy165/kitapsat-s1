package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
public class OrderItem implements Serializable {
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
    @JoinColumn(name = "order_id")
    private Order siparis;
    
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book kitap;

    public OrderItem() {}

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

    public Order getOrder() {
        return siparis;
    }

    public void setOrder(Order siparis) {
        this.siparis = siparis;
    }

    public Book getBook() {
        return kitap;
    }

    public void setBook(Book kitap) {
        this.kitap = kitap;
    }
}
