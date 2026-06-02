package facadeLocal;

import entity.Review;
import java.util.List;

public interface ReviewFacadeLocal {
    void createReview(Review review);
    Review editReview(Review review);
    void remove(Review review);
    Review find(Long id);
    List<Review> findAll();
}
