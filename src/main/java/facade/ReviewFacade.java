package facade;

import entity.Review;
import facadeLocal.ReviewFacadeLocal;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

@Stateless
public class ReviewFacade extends AbstractFacade implements ReviewFacadeLocal {

    @Override
    public void createReview(Review review) {
        this.entityManager.persist(review);
        this.entityManager.flush();
    }

    @Override
    public Review editReview(Review review) {
        Review merged = this.entityManager.merge(review);
        this.entityManager.flush();
        return merged;
    }

    @Override
    public void remove(Review review) {
        Review merged = this.entityManager.merge(review);
        this.entityManager.remove(merged);
        this.entityManager.flush();
    }

    @Override
    public Review find(Long id) {
        return this.entityManager.find(Review.class, id);
    }

    @Override
    public List<Review> findAll() {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Review> cq = cb.createQuery(Review.class);
        Root<Review> root = cq.from(Review.class);
        cq.select(root);
        TypedQuery<Review> query = this.entityManager.createQuery(cq);
        return query.getResultList();
    }
}
