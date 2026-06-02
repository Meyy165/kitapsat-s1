package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Review implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating")
    private Integer puan;

    @Column(name = "comment", length = 1000)
    private String yorum;

    @Column(name = "review_date")
    private LocalDateTime yorumTarihi;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book kitap;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users kullanici;

    public Review() {
        yorumTarihi = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return puan;
    }

    public void setRating(Integer puan) {
        this.puan = puan;
    }

    public String getComment() {
        return yorum;
    }

    public void setComment(String yorum) {
        this.yorum = yorum;
    }

    public LocalDateTime getReviewDate() {
        return yorumTarihi;
    }

    public void setReviewDate(LocalDateTime yorumTarihi) {
        this.yorumTarihi = yorumTarihi;
    }

    public Book getBook() {
        return kitap;
    }

    public void setBook(Book kitap) {
        this.kitap = kitap;
    }

    public Users getUser() {
        return kullanici;
    }

    public void setUser(Users kullanici) {
        this.kullanici = kullanici;
    }
}
