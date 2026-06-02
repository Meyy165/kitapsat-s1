package entity;

import enums.OrderStatus;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_number")
    private String siparisNumarasi;
    
    @Column(name = "order_date")
    private LocalDateTime siparisTarihi;
    
    @Column(name = "total_amount")
    private BigDecimal toplamTutar;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus durum;
    
    @Column(name = "shipping_address")
    private String teslimatAdresi;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users kullanici;
    
    @OneToMany(mappedBy = "siparis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> siparisKalemleri;

    @OneToOne(mappedBy = "siparis")
    private Payment odeme;

    @OneToOne(mappedBy = "siparis")
    private Shipment kargo;

    public Order() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return siparisNumarasi;
    }

    public void setOrderNumber(String siparisNumarasi) {
        this.siparisNumarasi = siparisNumarasi;
    }

    public LocalDateTime getOrderDate() {
        return siparisTarihi;
    }

    public void setOrderDate(LocalDateTime siparisTarihi) {
        this.siparisTarihi = siparisTarihi;
    }

    public BigDecimal getTotalAmount() {
        return toplamTutar;
    }

    public void setTotalAmount(BigDecimal toplamTutar) {
        this.toplamTutar = toplamTutar;
    }

    public OrderStatus getStatus() {
        return durum;
    }

    public void setStatus(OrderStatus durum) {
        this.durum = durum;
    }

    public String getShippingAddress() {
        return teslimatAdresi;
    }

    public void setShippingAddress(String teslimatAdresi) {
        this.teslimatAdresi = teslimatAdresi;
    }

    public Users getUser() {
        return kullanici;
    }

    public void setUser(Users kullanici) {
        this.kullanici = kullanici;
    }

    public List<OrderItem> getOrderItems() {
        return siparisKalemleri;
    }

    public void setOrderItems(List<OrderItem> siparisKalemleri) {
        this.siparisKalemleri = siparisKalemleri;
    }

    public Payment getPayment() {
        return odeme;
    }

    public void setPayment(Payment odeme) {
        this.odeme = odeme;
    }

    public Shipment getShipment() {
        return kargo;
    }

    public void setShipment(Shipment kargo) {
        this.kargo = kargo;
    }
}
