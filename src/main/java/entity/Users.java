package entity;

import enums.Role;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Users implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String ad;

    @Column(name = "surname")
    private String soyad;

    @Column(name = "email")
    private String eposta;

    @Column(name = "password")
    private String sifre;

    @Column(name = "phone")
    private String telefon;

    @Column(name = "address")
    private String adres;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role rol;

    @OneToMany(mappedBy = "kullanici")
    private List<Order> siparisler;

    @OneToMany(mappedBy = "kullanici")
    private List<Payment> odemeler;

    @OneToMany(mappedBy = "kullanici")
    private List<Review> yorumlar;

    @OneToOne(mappedBy = "kullanici")
    private ShoppingCart alisverisSepeti;

    @OneToMany(mappedBy = "kullanici")
    private List<Address> adresler;

    @OneToOne(mappedBy = "kullanici")
    private Wishlist istekListesi;

    @OneToMany(mappedBy = "kullanici")
    private List<SupportTicket> destekTalepleri;

    public Users() {}

    public Users(Long id, String name, String surname, String email, String password) {
        this.id = id;
        this.ad = name;
        this.soyad = surname;
        this.eposta = email;
        this.sifre = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return ad;
    }

    public void setName(String ad) {
        this.ad = ad;
    }

    public String getSurname() {
        return soyad;
    }

    public void setSurname(String soyad) {
        this.soyad = soyad;
    }

    public String getEmail() {
        return eposta;
    }

    public void setEmail(String eposta) {
        this.eposta = eposta;
    }

    public String getPassword() {
        return sifre;
    }

    public void setPassword(String sifre) {
        this.sifre = sifre;
    }

    public String getPhone() {
        return telefon;
    }

    public void setPhone(String telefon) {
        this.telefon = telefon;
    }

    public String getAddress() {
        return adres;
    }

    public void setAddress(String adres) {
        this.adres = adres;
    }

    public Role getRole() {
        return rol;
    }

    public void setRole(Role rol) {
        this.rol = rol;
    }

    public boolean isAdmin() {
        return Role.ADMIN.equals(rol);
    }

    public boolean isUser() {
        return Role.USER.equals(rol);
    }

    public List<Order> getOrders() {
        return siparisler;
    }

    public void setOrders(List<Order> siparisler) {
        this.siparisler = siparisler;
    }

    public List<Payment> getPayments() {
        return odemeler;
    }

    public void setPayments(List<Payment> odemeler) {
        this.odemeler = odemeler;
    }

    public List<Review> getReviews() {
        return yorumlar;
    }

    public void setReviews(List<Review> yorumlar) {
        this.yorumlar = yorumlar;
    }

    public ShoppingCart getShoppingCart() {
        return alisverisSepeti;
    }

    public void setShoppingCart(ShoppingCart alisverisSepeti) {
        this.alisverisSepeti = alisverisSepeti;
    }

    public List<Address> getAddresses() {
        return adresler;
    }

    public void setAddresses(List<Address> adresler) {
        this.adresler = adresler;
    }

    public Wishlist getWishlist() {
        return istekListesi;
    }

    public void setWishlist(Wishlist istekListesi) {
        this.istekListesi = istekListesi;
    }

    public List<SupportTicket> getSupportTickets() {
        return destekTalepleri;
    }

    public void setSupportTickets(List<SupportTicket> destekTalepleri) {
        this.destekTalepleri = destekTalepleri;
    }
}
