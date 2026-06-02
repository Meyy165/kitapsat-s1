package entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Payment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_date")
    private LocalDateTime odemeTarihi;

    @Column(name = "amount")
    private BigDecimal tutar;

    @Column(name = "status")
    private String durum;

    @Column(name = "masked_card_number")
    private String maskeliKartNumarasi;

    @Column(name = "card_expiry")
    private String kartSonKullanma;

    @Column(name = "transaction_code")
    private String islemKodu;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users kullanici;

    @OneToOne
    @JoinColumn(name = "order_id", unique = true)
    private Order siparis;

    public Payment() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getPaymentDate() {
        return odemeTarihi;
    }

    public void setPaymentDate(LocalDateTime odemeTarihi) {
        this.odemeTarihi = odemeTarihi;
    }

    public BigDecimal getAmount() {
        return tutar;
    }

    public void setAmount(BigDecimal tutar) {
        this.tutar = tutar;
    }

    public String getStatus() {
        return durum;
    }

    public void setStatus(String durum) {
        this.durum = durum;
    }

    public String getMaskedCardNumber() {
        return maskeliKartNumarasi;
    }

    public void setMaskedCardNumber(String maskeliKartNumarasi) {
        this.maskeliKartNumarasi = maskeliKartNumarasi;
    }

    public String getCardExpiry() {
        return kartSonKullanma;
    }

    public void setCardExpiry(String kartSonKullanma) {
        this.kartSonKullanma = kartSonKullanma;
    }

    public String getTransactionCode() {
        return islemKodu;
    }

    public void setTransactionCode(String islemKodu) {
        this.islemKodu = islemKodu;
    }

    public Users getUser() {
        return kullanici;
    }

    public void setUser(Users kullanici) {
        this.kullanici = kullanici;
    }

    public Order getOrder() {
        return siparis;
    }

    public void setOrder(Order siparis) {
        this.siparis = siparis;
    }
}
