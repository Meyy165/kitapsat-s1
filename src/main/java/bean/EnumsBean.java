package bean;

import enums.OrderStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.util.Arrays;
import java.util.List;

@Named
@ApplicationScoped
public class EnumsBean {
    
    public List<OrderStatus> getOrderStatusValues() {
        return Arrays.asList(OrderStatus.values());
    }
}
