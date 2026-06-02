package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String ad;

    @Column(name = "description")
    private String aciklama;
    
    @ManyToMany(mappedBy = "kategoriler")
    private List<Book> kitaplar;

    public Category() {}

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

    public String getDescription() {
        return aciklama;
    }

    public void setDescription(String aciklama) {
        this.aciklama = aciklama;
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
        Category category = (Category) o;
        return id != null && id.equals(category.id);
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
