package enums;

public enum OrderStatus {
    PENDING("Beklemede"),
    PROCESSING("İşleniyor"),
    SHIPPED("Kargolandı"),
    DELIVERED("Teslim Edildi"),
    CANCELLED("İptal Edildi");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
