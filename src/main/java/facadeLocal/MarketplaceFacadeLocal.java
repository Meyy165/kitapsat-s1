package facadeLocal;

import entity.Campaign;
import entity.Coupon;
import entity.SupportTicket;
import entity.Users;
import entity.WishlistItem;
import java.util.List;

public interface MarketplaceFacadeLocal {
    List<Campaign> findActiveCampaigns();
    List<Coupon> findActiveCoupons();
    Coupon findCouponByCode(String code);
    void addToWishlist(Users user, Long bookId);
    void removeWishlistItem(Long itemId);
    List<WishlistItem> findWishlistItems(Users user);
    void createSupportTicket(Users user, SupportTicket ticket);
    List<SupportTicket> findSupportTickets(Users user);
    long countCoupons();
    long countShipments();
    long countSupportTickets();
}
