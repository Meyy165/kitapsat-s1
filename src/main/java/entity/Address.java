package entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class Address implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String baslik;

    @Column(name = "recipient_name")
    private String aliciAdi;

    @Column(name = "city")
    private String sehir;

    @Column(name = "district")
    private String ilce;

    @Column(name = "full_address", length = 1000)
    private String tamAdres;

    @Column(name = "postal_code")
    private String postaKodu;

    @Column(name = "default_address")
    private boolean varsayilanAdres;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users kullanici;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return baslik; }
    public void setTitle(String baslik) { this.baslik = baslik; }
    public String getRecipientName() { return aliciAdi; }
    public void setRecipientName(String aliciAdi) { this.aliciAdi = aliciAdi; }
    public String getCity() { return sehir; }
    public void setCity(String sehir) { this.sehir = sehir; }
    public String getDistrict() { return ilce; }
    public void setDistrict(String ilce) { this.ilce = ilce; }
    public String getFullAddress() { return tamAdres; }
    public void setFullAddress(String tamAdres) { this.tamAdres = tamAdres; }
    public String getPostalCode() { return postaKodu; }
    public void setPostalCode(String postaKodu) { this.postaKodu = postaKodu; }
    public boolean isDefaultAddress() { return varsayilanAdres; }
    public void setDefaultAddress(boolean varsayilanAdres) { this.varsayilanAdres = varsayilanAdres; }
    public Users getUser() { return kullanici; }
    public void setUser(Users kullanici) { this.kullanici = kullanici; }
}
