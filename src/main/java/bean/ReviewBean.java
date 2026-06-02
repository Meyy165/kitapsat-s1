package bean;

import entity.Book;
import entity.Review;
import entity.Users;
import facadeLocal.BookFacadeLocal;
import facadeLocal.ReviewFacadeLocal;
import facadeLocal.UserFacadeLocal;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class ReviewBean implements Serializable {
    private Review review;
    private List<Review> reviews;
    private List<Book> books;
    private List<Users> users;
    private Long selectedBookId;
    private Long selectedUserId;

    @EJB
    private ReviewFacadeLocal reviewFacade;

    @EJB
    private BookFacadeLocal bookFacade;

    @EJB
    private UserFacadeLocal userFacade;

    public ReviewBean() {
        review = new Review();
    }

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();
        String reviewId = params.get("id");

        if (reviewId != null && !reviewId.isEmpty()) {
            try {
                review = reviewFacade.find(Long.parseLong(reviewId));
                if (review != null && review.getBook() != null) {
                    selectedBookId = review.getBook().getId();
                }
                if (review != null && review.getUser() != null) {
                    selectedUserId = review.getUser().getId();
                }
            } catch (NumberFormatException e) {
                review = new Review();
            }
        }
    }

    public List<Review> getAllReviews() {
        if (reviews == null) {
            reviews = reviewFacade.findAll();
        }
        return reviews;
    }

    public String addReview() {
        bindRelations();
        reviewFacade.createReview(review);
        return "/admin/reviews.xhtml?faces-redirect=true";
    }

    public String updateReview() {
        bindRelations();
        reviewFacade.editReview(review);
        return "/admin/reviews.xhtml?faces-redirect=true";
    }

    public String deleteReview(Long reviewId) {
        Review reviewToDelete = reviewFacade.find(reviewId);
        if (reviewToDelete != null) {
            reviewFacade.remove(reviewToDelete);
        }
        return "/admin/reviews.xhtml?faces-redirect=true";
    }

    private void bindRelations() {
        if (selectedBookId != null) {
            review.setBook(bookFacade.find(selectedBookId));
        }
        if (selectedUserId != null) {
            review.setUser(userFacade.find(selectedUserId));
        }
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public List<Book> getBooks() {
        if (books == null) {
            books = bookFacade.findAll();
        }
        return books;
    }

    public List<Users> getUsers() {
        if (users == null) {
            users = userFacade.usersList();
        }
        return users;
    }

    public Long getSelectedBookId() {
        return selectedBookId;
    }

    public void setSelectedBookId(Long selectedBookId) {
        this.selectedBookId = selectedBookId;
    }

    public Long getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(Long selectedUserId) {
        this.selectedUserId = selectedUserId;
    }
}
