package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Author implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String ad;

    @Column(name = "surname")
    private String soyad;

    @Column(name = "biography")
    private String biyografi;
    
    @ManyToMany(mappedBy = "yazarlar")
    private List<Book> kitaplar;

    public Author() {}

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

    public String getBiography() {
        return biyografi;
    }

    public void setBiography(String biyografi) {
        this.biyografi = biyografi;
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
        Author author = (Author) o;
        return id != null && id.equals(author.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return ad + " " + soyad;
    }
}
