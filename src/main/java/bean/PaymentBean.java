package bean;

import entity.CartItem;
import entity.Order;
import entity.Payment;
import entity.Users;
import facadeLocal.PaymentFacadeLocal;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Named
@SessionScoped
public class PaymentBean implements Serializable {

    private String cardNumber;
    private String expiryMonth;
    private String expiryYear;
    private String cvv;

    @Inject
    private CartBean cartBean;

    @Inject
    private OrderBean orderBean;

    @EJB
    private PaymentFacadeLocal paymentFacade;

    @Inject
    private FacesContext facesContext;

    public PaymentBean() {
    }

    public String processPayment() {
        if (cardNumber == null || cardNumber.trim().isEmpty()
                || expiryMonth == null || expiryMonth.trim().isEmpty()
                || expiryYear == null || expiryYear.trim().isEmpty()
                || cvv == null || cvv.trim().isEmpty()) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Lutfen tum odeme alanlarini doldurun.");
            facesContext.addMessage("paymentForm", msg);
            return null;
        }

        Users currentUser = (Users) facesContext.getExternalContext().getSessionMap().get("user");
        if (currentUser == null) {
            return "/login.xhtml?faces-redirect=true";
        }

        List<CartItem> cartItems = cartBean.getCartItems();
        if (cartItems == null || cartItems.isEmpty()) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Sepetiniz bos. Odeme yapabilmek icin sepetinize urun ekleyin.");
            facesContext.addMessage("paymentForm", msg);
            return null;
        }

        Order order = orderBean.createOrderFromCart(cartItems, currentUser);
        if (order == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Siparis olusturulurken bir hata olustu. Lutfen tekrar deneyin.");
            facesContext.addMessage("paymentForm", msg);
            return null;
        }

        createPaymentRecord(order, currentUser);
        cartBean.clearCart();
        return "/order/confirmation.xhtml?faces-redirect=true";
    }

    private void createPaymentRecord(Order order, Users user) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setUser(user);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus("PAID");
        payment.setMaskedCardNumber(maskCardNumber(cardNumber));
        payment.setCardExpiry(expiryMonth + "/" + expiryYear);
        payment.setTransactionCode("PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        paymentFacade.create(payment);
        order.setPayment(payment);
    }

    private String maskCardNumber(String rawCardNumber) {
        String digits = rawCardNumber == null ? "" : rawCardNumber.replaceAll("\\s+", "");
        if (digits.length() <= 4) {
            return digits;
        }
        return "**** **** **** " + digits.substring(digits.length() - 4);
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
