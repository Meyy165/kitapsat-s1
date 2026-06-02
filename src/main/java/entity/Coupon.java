package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Coupon implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true)
    private String kod;

    @Column(name = "description")
    private String aciklama;

    @Column(name = "discount_rate")
    private BigDecimal indirimOrani;

    @Column(name = "minimum_amount")
    private BigDecimal minimumTutar;

    @Column(name = "valid_until")
    private LocalDate bitisTarihi;

    @Column(name = "active")
    private boolean aktif = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return kod; }
    public void setCode(String kod) { this.kod = kod; }
    public String getDescription() { return aciklama; }
    public void setDescription(String aciklama) { this.aciklama = aciklama; }
    public BigDecimal getDiscountRate() { return indirimOrani; }
    public void setDiscountRate(BigDecimal indirimOrani) { this.indirimOrani = indirimOrani; }
    public BigDecimal getMinimumAmount() { return minimumTutar; }
    public void setMinimumAmount(BigDecimal minimumTutar) { this.minimumTutar = minimumTutar; }
    public LocalDate getValidUntil() { return bitisTarihi; }
    public void setValidUntil(LocalDate bitisTarihi) { this.bitisTarihi = bitisTarihi; }
    public boolean isActive() { return aktif; }
    public void setActive(boolean aktif) { this.aktif = aktif; }
}
