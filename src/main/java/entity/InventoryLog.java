package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class InventoryLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "change_amount")
    private int degisimMiktari;

    @Column(name = "reason")
    private String neden;

    @Column(name = "created_at")
    private LocalDateTime olusturmaTarihi = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book kitap;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getChangeAmount() { return degisimMiktari; }
    public void setChangeAmount(int degisimMiktari) { this.degisimMiktari = degisimMiktari; }
    public String getReason() { return neden; }
    public void setReason(String neden) { this.neden = neden; }
    public LocalDateTime getCreatedAt() { return olusturmaTarihi; }
    public void setCreatedAt(LocalDateTime olusturmaTarihi) { this.olusturmaTarihi = olusturmaTarihi; }
    public Book getBook() { return kitap; }
    public void setBook(Book kitap) { this.kitap = kitap; }
}
