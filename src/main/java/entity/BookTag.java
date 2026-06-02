package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class BookTag implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String ad;

    @ManyToMany(mappedBy = "etiketler")
    private List<Book> kitaplar;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return ad; }
    public void setName(String ad) { this.ad = ad; }
    public List<Book> getBooks() { return kitaplar; }
    public void setBooks(List<Book> kitaplar) { this.kitaplar = kitaplar; }
}
