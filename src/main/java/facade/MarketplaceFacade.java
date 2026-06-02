package facade;

import entity.Book;
import entity.Campaign;
import entity.Coupon;
import entity.SupportTicket;
import entity.Users;
import entity.Wishlist;
import entity.WishlistItem;
import facadeLocal.MarketplaceFacadeLocal;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

@Stateless
public class MarketplaceFacade extends AbstractFacade implements MarketplaceFacadeLocal {

    @Override
    public List<Campaign> findActiveCampaigns() {
        LocalDate today = LocalDate.now();
        return entityManager.createQuery("""
                select c from Campaign c
                where c.aktif = true
                  and (c.baslangicTarihi is null or c.baslangicTarihi <= :today)
                  and (c.bitisTarihi is null or c.bitisTarihi >= :today)
                order by c.id desc
                """, Campaign.class)
                .setParameter("today", today)
                .getResultList();
    }

    @Override
    public List<Coupon> findActiveCoupons() {
        LocalDate today = LocalDate.now();
        return entityManager.createQuery("""
                select c from Coupon c
                where c.aktif = true
                  and (c.bitisTarihi is null or c.bitisTarihi >= :today)
                order by c.id desc
                """, Coupon.class)
                .setParameter("today", today)
                .getResultList();
    }

    @Override
    public Coupon findCouponByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        try {
            return entityManager.createQuery("""
                    select c from Coupon c
                    where lower(c.kod) = :code and c.aktif = true
                    """, Coupon.class)
                    .setParameter("code", code.trim().toLowerCase(TURKISH))
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void addToWishlist(Users user, Long bookId) {
        if (user == null || bookId == null) {
            return;
        }
        Wishlist wishlist = findOrCreateWishlist(user);
        Book book = entityManager.find(Book.class, bookId);
        if (book == null || wishlistContains(wishlist, bookId)) {
            return;
        }
        WishlistItem item = new WishlistItem();
        item.setWishlist(wishlist);
        item.setBook(book);
        entityManager.persist(item);
        entityManager.flush();
    }

    @Override
    public void removeWishlistItem(Long itemId) {
        WishlistItem item = entityManager.find(WishlistItem.class, itemId);
        if (item != null) {
            entityManager.remove(item);
            entityManager.flush();
        }
    }

    @Override
    public List<WishlistItem> findWishlistItems(Users user) {
        if (user == null || user.getId() == null) {
            return List.of();
        }
        return entityManager.createQuery("""
                select wi from WishlistItem wi
                join wi.istekListesi w
                where w.kullanici.id = :userId
                order by wi.eklenmeTarihi desc
                """, WishlistItem.class)
                .setParameter("userId", user.getId())
                .getResultList();
    }

    @Override
    public void createSupportTicket(Users user, SupportTicket ticket) {
        if (user == null || ticket == null) {
            return;
        }
        ticket.setUser(entityManager.merge(user));
        entityManager.persist(ticket);
        entityManager.flush();
    }

    @Override
    public List<SupportTicket> findSupportTickets(Users user) {
        if (user == null || user.getId() == null) {
            return List.of();
        }
        return entityManager.createQuery("""
                select t from SupportTicket t
                where t.kullanici.id = :userId
                order by t.olusturmaTarihi desc
                """, SupportTicket.class)
                .setParameter("userId", user.getId())
                .getResultList();
    }

    @Override
    public long countCoupons() {
        return count("Coupon");
    }

    @Override
    public long countShipments() {
        return count("Shipment");
    }

    @Override
    public long countSupportTickets() {
        return count("SupportTicket");
    }

    private Wishlist findOrCreateWishlist(Users user) {
        TypedQuery<Wishlist> query = entityManager.createQuery(
                "select w from Wishlist w where w.kullanici.id = :userId", Wishlist.class);
        query.setParameter("userId", user.getId());
        query.setMaxResults(1);
        List<Wishlist> result = query.getResultList();
        if (!result.isEmpty()) {
            return result.get(0);
        }
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(entityManager.merge(user));
        entityManager.persist(wishlist);
        entityManager.flush();
        return wishlist;
    }

    private boolean wishlistContains(Wishlist wishlist, Long bookId) {
        Long count = entityManager.createQuery("""
                select count(wi) from WishlistItem wi
                where wi.istekListesi.id = :wishlistId and wi.kitap.id = :bookId
                """, Long.class)
                .setParameter("wishlistId", wishlist.getId())
                .setParameter("bookId", bookId)
                .getSingleResult();
        return count > 0;
    }

    private long count(String entityName) {
        return entityManager.createQuery("select count(e) from " + entityName + " e", Long.class)
                .getSingleResult();
    }
}
