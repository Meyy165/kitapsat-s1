package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class SupportTicket implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject")
    private String konu;

    @Column(name = "message", length = 2000)
    private String mesaj;

    @Column(name = "status")
    private String durum = "ACIK";

    @Column(name = "created_at")
    private LocalDateTime olusturmaTarihi = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users kullanici;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSubject() { return konu; }
    public void setSubject(String konu) { this.konu = konu; }
    public String getMessage() { return mesaj; }
    public void setMessage(String mesaj) { this.mesaj = mesaj; }
    public String getStatus() { return durum; }
    public void setStatus(String durum) { this.durum = durum; }
    public LocalDateTime getCreatedAt() { return olusturmaTarihi; }
    public void setCreatedAt(LocalDateTime olusturmaTarihi) { this.olusturmaTarihi = olusturmaTarihi; }
    public Users getUser() { return kullanici; }
    public void setUser(Users kullanici) { this.kullanici = kullanici; }
}
