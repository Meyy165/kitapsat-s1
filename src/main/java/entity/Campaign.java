package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
public class Campaign implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String ad;

    @Column(name = "description", length = 1000)
    private String aciklama;

    @Column(name = "banner_text")
    private String bannerMetni;

    @Column(name = "start_date")
    private LocalDate baslangicTarihi;

    @Column(name = "end_date")
    private LocalDate bitisTarihi;

    @Column(name = "active")
    private boolean aktif = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return ad; }
    public void setName(String ad) { this.ad = ad; }
    public String getDescription() { return aciklama; }
    public void setDescription(String aciklama) { this.aciklama = aciklama; }
    public String getBannerText() { return bannerMetni; }
    public void setBannerText(String bannerMetni) { this.bannerMetni = bannerMetni; }
    public LocalDate getStartDate() { return baslangicTarihi; }
    public void setStartDate(LocalDate baslangicTarihi) { this.baslangicTarihi = baslangicTarihi; }
    public LocalDate getEndDate() { return bitisTarihi; }
    public void setEndDate(LocalDate bitisTarihi) { this.bitisTarihi = bitisTarihi; }
    public boolean isActive() { return aktif; }
    public void setActive(boolean aktif) { this.aktif = aktif; }
}
