package facadeLocal;

import entity.Order;
import java.util.List;

public interface OrderFacadeLocal {
    void createOrder(Order order);
    Order editOrder(Order order);
    void remove(Order order);
    Order find(Long id);
    List<Order> findAll();
    List<Order> findByUser(Long userId);
    List<Order> findByStatus(String status);
}
