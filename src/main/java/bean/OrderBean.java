package bean;

import entity.Order;
import entity.OrderItem;
import entity.Users;
import entity.CartItem;
import enums.OrderStatus;
import facadeLocal.OrderFacadeLocal;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Named
@SessionScoped
public class OrderBean implements Serializable {
    private Order selectedOrder;
    private String searchKeyword;
    private List<Order> allOrders;
    private int firstRow = 0;
    private OrderStatus statusFilter;
    private Order currentOrder;
    
    @EJB
    OrderFacadeLocal orderFacade;
    
    @Inject
    FacesContext facesContext;

    public OrderBean() {
        selectedOrder = new Order();
    }
    
    public Order createOrderFromCart(List<CartItem> cartItems, Users user) {
        if (cartItems == null || cartItems.isEmpty()) {
            return null;
        }
        
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setOrderDate(LocalDateTime.now());
        order.setUser(user);
        order.setStatus(OrderStatus.PROCESSING);
        order.setShippingAddress(user.getAddress() != null ? user.getAddress() : "Adres belirtilmemiş");
        
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
            
            totalAmount = totalAmount.add(cartItem.getTotalPrice());
        }
        
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        
        try {
            orderFacade.createOrder(order);
            this.currentOrder = order;
            return order;
        } catch (Exception e) {
            System.err.println("Sipariş oluşturma hatası: " + e.getMessage());
            return null;
        }
    }
    
    private String generateOrderNumber() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);
        return "ORD-" + number;
    }

    public List<Order> getAllOrders() {
        if (allOrders == null) {
            allOrders = orderFacade.findAll();
        }
        return allOrders;
    }

    public String searchOrders() {
        try {
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                String cleanKeyword = searchKeyword.trim().toLowerCase();
                allOrders = orderFacade.findAll().stream()
                        .filter(o -> o.getOrderNumber() != null && o.getOrderNumber().toLowerCase().contains(cleanKeyword) ||
                                   o.getUser() != null && 
                                   (o.getUser().getName() != null && o.getUser().getName().toLowerCase().contains(cleanKeyword) ||
                                    o.getUser().getSurname() != null && o.getUser().getSurname().toLowerCase().contains(cleanKeyword) ||
                                    o.getUser().getEmail() != null && o.getUser().getEmail().toLowerCase().contains(cleanKeyword)))
                        .toList();
            } else {
                allOrders = orderFacade.findAll();
            }
            firstRow = 0; // Aramada sayfayı sıfırla
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Arama sırasında bir hata oluştu.");
            facesContext.addMessage(null, msg);
        }
        return null; // Aynı sayfada kal
    }

    public String filterByStatus() {
        try {
            if (statusFilter != null) {
                allOrders = orderFacade.findByStatus(statusFilter.name());
            } else {
                allOrders = orderFacade.findAll();
            }
            firstRow = 0; // Filtrelemede sayfayı sıfırla
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Filtreleme sırasında bir hata oluştu.");
            facesContext.addMessage(null, msg);
        }
        return null; // Aynı sayfada kal
    }

    public String updateOrderStatus() {
        try {
            if (selectedOrder != null && selectedOrder.getId() != null) {
                orderFacade.editOrder(selectedOrder);
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Sipariş durumu güncellendi.");
                facesContext.addMessage(null, msg);
                allOrders = null; // Listeyi yenile
                return "/admin/orders.xhtml?faces-redirect=true";
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Geçerli bir sipariş seçilmedi.");
                facesContext.addMessage(null, msg);
                return null;
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Sipariş güncellenirken bir hata oluştu.");
            facesContext.addMessage(null, msg);
            return null;
        }
    }

    public String deleteOrder(Long orderId) {
        try {
            Order orderToDelete = orderFacade.find(orderId);
            
            if (orderToDelete != null) {
                orderFacade.remove(orderToDelete);
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Sipariş silindi.");
                facesContext.addMessage(null, msg);
                allOrders = null; // Listeyi yenile
                return "/admin/orders.xhtml?faces-redirect=true";
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Sipariş bulunamadı.");
                facesContext.addMessage(null, msg);
                return null;
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Sipariş silinirken bir hata oluştu.");
            facesContext.addMessage(null, msg);
            return null;
        }
    }

    public String previousPage() {
        if (firstRow > 0) {
            firstRow = Math.max(0, firstRow - 10);
        }
        return null;
    }

    public String nextPage() {
        if (allOrders != null && firstRow + 10 < allOrders.size()) {
            firstRow += 10;
        }
        return null;
    }

    public int getTotalOrdersCount() {
        return getAllOrders().size();
    }

    public String getTotalRevenue() {
        return getAllOrders().stream()
                .filter(o -> OrderStatus.DELIVERED.equals(o.getStatus()))
                .mapToDouble(o -> o.getTotalAmount() != null ? o.getTotalAmount().doubleValue() : 0.0)
                .sum() + " TL";
    }

    // Getter ve Setter metotları
    public Order getSelectedOrder() {
        return selectedOrder;
    }

    public void setSelectedOrder(Order selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public OrderStatus getStatusFilter() {
        return statusFilter;
    }

    public void setStatusFilter(OrderStatus statusFilter) {
        this.statusFilter = statusFilter;
    }

    public int getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }
    
    public List<Order> getUserOrders() {
        try {
            Users currentUser = (Users) facesContext.getExternalContext().getSessionMap().get("user");
            if (currentUser != null) {
                return orderFacade.findByUser(currentUser.getId());
            }
        } catch (Exception e) {
            System.err.println("Kullanıcı siparişleri alınırken hata: " + e.getMessage());
        }
        return new ArrayList<>();
    }
    
    public String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "-";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return dateTime.format(formatter);
    }
    
    public String viewOrder(Order order) {
        this.selectedOrder = order;
        return null; // Aynı sayfada kal, order details bölümü görünecek
    }
    
    public String getAuthorsString(List<entity.Author> authors) {
        if (authors == null || authors.isEmpty()) {
            return "-";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < authors.size(); i++) {
            entity.Author author = authors.get(i);
            sb.append(author.getName()).append(" ").append(author.getSurname());
            if (i < authors.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
