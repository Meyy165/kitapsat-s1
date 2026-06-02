package bean;

import entity.Campaign;
import entity.Coupon;
import entity.SupportTicket;
import entity.Users;
import entity.WishlistItem;
import facadeLocal.MarketplaceFacadeLocal;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class MarketplaceBean implements Serializable {
    private String couponCode;
    private String couponMessage;
    private SupportTicket supportTicket = new SupportTicket();
    private List<WishlistItem> wishlistItems;

    @EJB
    private MarketplaceFacadeLocal marketplaceFacade;

    public String addToWishlist(Long bookId) {
        Users user = currentUser();
        if (user == null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Giris gerekli", "Istek listesi icin once giris yapmalisiniz.");
            return "/login.xhtml?faces-redirect=true";
        }
        marketplaceFacade.addToWishlist(user, bookId);
        wishlistItems = null;
        addMessage(FacesMessage.SEVERITY_INFO, "Eklendi", "Kitap istek listenize eklendi.");
        return null;
    }

    public String removeWishlistItem(Long itemId) {
        marketplaceFacade.removeWishlistItem(itemId);
        wishlistItems = null;
        return "/user/wishlist.xhtml?faces-redirect=true";
    }

    public String submitSupportTicket() {
        Users user = currentUser();
        if (user == null) {
            return "/login.xhtml?faces-redirect=true";
        }
        marketplaceFacade.createSupportTicket(user, supportTicket);
        supportTicket = new SupportTicket();
        addMessage(FacesMessage.SEVERITY_INFO, "Talep alindi", "Destek talebiniz kaydedildi.");
        return "/support.xhtml?faces-redirect=true";
    }

    public String applyCoupon() {
        Coupon coupon = marketplaceFacade.findCouponByCode(couponCode);
        if (coupon == null) {
            couponMessage = "Kupon bulunamadi veya aktif degil.";
        } else {
            couponMessage = coupon.getCode() + " kuponu aktif: %" + coupon.getDiscountRate() + " indirim.";
        }
        return null;
    }

    public List<Campaign> getActiveCampaigns() {
        return marketplaceFacade.findActiveCampaigns();
    }

    public List<Coupon> getActiveCoupons() {
        return marketplaceFacade.findActiveCoupons();
    }

    public List<WishlistItem> getWishlistItems() {
        Users user = currentUser();
        if (user == null) {
            return List.of();
        }
        if (wishlistItems == null) {
            wishlistItems = marketplaceFacade.findWishlistItems(user);
        }
        return wishlistItems;
    }

    public List<SupportTicket> getMySupportTickets() {
        Users user = currentUser();
        return user == null ? List.of() : marketplaceFacade.findSupportTickets(user);
    }

    public long getCouponCount() {
        return marketplaceFacade.countCoupons();
    }

    public long getShipmentCount() {
        return marketplaceFacade.countShipments();
    }

    public long getSupportTicketCount() {
        return marketplaceFacade.countSupportTickets();
    }

    private Users currentUser() {
        return (Users) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
    public String getCouponMessage() { return couponMessage; }
    public void setCouponMessage(String couponMessage) { this.couponMessage = couponMessage; }
    public SupportTicket getSupportTicket() { return supportTicket; }
    public void setSupportTicket(SupportTicket supportTicket) { this.supportTicket = supportTicket; }
}
