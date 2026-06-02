package facade;

import entity.Book;
import entity.Order;
import entity.OrderItem;
import enums.OrderStatus;
import facadeLocal.OrderFacadeLocal;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

@Stateless
public class OrderFacade extends AbstractFacade implements OrderFacadeLocal {

    @Override
    public void createOrder(Order order) {
        decreaseBookStock(order);
        this.entityManager.persist(order);
        this.entityManager.flush();
    }

    private void decreaseBookStock(Order order) {
        if (order == null || order.getOrderItems() == null) {
            return;
        }

        for (OrderItem item : order.getOrderItems()) {
            if (item.getBook() == null || item.getBook().getId() == null) {
                continue;
            }

            Book book = this.entityManager.find(Book.class, item.getBook().getId());
            if (book == null) {
                throw new IllegalStateException("Kitap bulunamadi: " + item.getBook().getId());
            }

            int orderedQuantity = item.getQuantity();
            if (orderedQuantity <= 0) {
                throw new IllegalStateException("Gecersiz siparis adedi: " + orderedQuantity);
            }

            if (book.getStockQuantity() < orderedQuantity) {
                throw new IllegalStateException("Yetersiz stok: " + book.getTitle());
            }

            book.setStockQuantity(book.getStockQuantity() - orderedQuantity);
            item.setBook(book);
        }
    }

    @Override
    public Order editOrder(Order order) {
        this.entityManager.merge(order);
        this.entityManager.flush();
        return order;
    }

    @Override
    public void remove(Order order) {
        Order merged = this.entityManager.merge(order);
        this.entityManager.remove(merged);
    }

    @Override
    public Order find(Long id) {
        return this.entityManager.find(Order.class, id);
    }

    @Override
    public List<Order> findAll() {
        try {
            CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
            CriteriaQuery<Order> cq = cb.createQuery(Order.class);
            Root<Order> root = cq.from(Order.class);
            CriteriaQuery<Order> all = cq.select(root);
            TypedQuery<Order> q = this.entityManager.createQuery(all);
            return q.getResultList();
        }catch (Exception e) {
            System.out.println("findAll() methodu hata oluştu: " + e.getMessage());
            return null;
        }

    }

    @Override
    public List<Order> findByUser(Long userId) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> root = cq.from(Order.class);
        cq.where(cb.equal(root.join("kullanici").get("id"), userId));
        CriteriaQuery<Order> all = cq.select(root);
        TypedQuery<Order> q = this.entityManager.createQuery(all);
        return q.getResultList();
    }

    @Override
    public List<Order> findByStatus(String status) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> root = cq.from(Order.class);
        cq.where(cb.equal(root.get("durum"), OrderStatus.valueOf(status)));
        CriteriaQuery<Order> all = cq.select(root);
        TypedQuery<Order> q = this.entityManager.createQuery(all);
        return q.getResultList();
    }
}
