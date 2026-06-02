package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Shipment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tracking_number")
    private String takipNumarasi;

    @Column(name = "carrier")
    private String kargoFirmasi;

    @Column(name = "status")
    private String durum;

    @Column(name = "shipped_at")
    private LocalDateTime kargoyaVerilmeTarihi;

    @Column(name = "delivered_at")
    private LocalDateTime teslimTarihi;

    @OneToOne
    @JoinColumn(name = "order_id", unique = true)
    private Order siparis;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTrackingNumber() { return takipNumarasi; }
    public void setTrackingNumber(String takipNumarasi) { this.takipNumarasi = takipNumarasi; }
    public String getCarrier() { return kargoFirmasi; }
    public void setCarrier(String kargoFirmasi) { this.kargoFirmasi = kargoFirmasi; }
    public String getStatus() { return durum; }
    public void setStatus(String durum) { this.durum = durum; }
    public LocalDateTime getShippedAt() { return kargoyaVerilmeTarihi; }
    public void setShippedAt(LocalDateTime kargoyaVerilmeTarihi) { this.kargoyaVerilmeTarihi = kargoyaVerilmeTarihi; }
    public LocalDateTime getDeliveredAt() { return teslimTarihi; }
    public void setDeliveredAt(LocalDateTime teslimTarihi) { this.teslimTarihi = teslimTarihi; }
    public Order getOrder() { return siparis; }
    public void setOrder(Order siparis) { this.siparis = siparis; }
}
