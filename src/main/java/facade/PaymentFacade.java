package facade;

import entity.Payment;
import facadeLocal.PaymentFacadeLocal;
import jakarta.ejb.Stateless;

@Stateless
public class PaymentFacade extends AbstractFacade implements PaymentFacadeLocal {

    @Override
    public void create(Payment payment) {
        this.entityManager.persist(payment);
        this.entityManager.flush();
    }
}
