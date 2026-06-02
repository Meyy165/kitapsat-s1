package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Publisher implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String ad;

    @Column(name = "address")
    private String adres;

    @Column(name = "phone")
    private String telefon;

    @Column(name = "email")
    private String eposta;
    
    @OneToMany(mappedBy = "yayinevi")
    private List<Book> kitaplar;

    public Publisher() {}

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

    public String getAddress() {
        return adres;
    }

    public void setAddress(String adres) {
        this.adres = adres;
    }

    public String getPhone() {
        return telefon;
    }

    public void setPhone(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return eposta;
    }

    public void setEmail(String eposta) {
        this.eposta = eposta;
    }

    public List<Book> getBooks() {
        return kitaplar;
    }

    public void setBooks(List<Book> kitaplar) {
        this.kitaplar = kitaplar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publisher publisher = (Publisher) o;
        return id != null && id.equals(publisher.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return ad;
    }
}
