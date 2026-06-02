package bean;

import entity.Book;
import entity.CartItem;
import entity.ShoppingCart;
import entity.Users;
import facadeLocal.BookFacadeLocal;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class CartBean implements Serializable {
    private ShoppingCart shoppingCart;
    private List<CartItem> cartItems;
    
    @EJB
    BookFacadeLocal bookFacade;
    
    @Inject
    FacesContext facesContext;

    public CartBean() {
        shoppingCart = new ShoppingCart();
        cartItems = new ArrayList<>();
    }

    public void addToCart(Long bookId) {
        Book book = bookFacade.find(bookId);
        if (book != null && book.getStockQuantity() > 0) {
            CartItem existingItem = findCartItemByBook(bookId);
            
            if (existingItem != null) {
                int newQuantity = existingItem.getQuantity() + 1;
                if (newQuantity <= book.getStockQuantity()) {
                    existingItem.setQuantity(newQuantity);
                    existingItem.setTotalPrice(existingItem.getUnitPrice().multiply(BigDecimal.valueOf(newQuantity)));
                    updateCartTotal();
                }
            } else {
                if (1 <= book.getStockQuantity()) {
                    CartItem newItem = new CartItem();
                    newItem.setBook(book);
                    newItem.setQuantity(1);
                    newItem.setUnitPrice(book.getPrice());
                    newItem.setTotalPrice(book.getPrice());
                    newItem.setShoppingCart(shoppingCart);
                    cartItems.add(newItem);
                    updateCartTotal();
                }
            }
        }
    }

    public void removeFromCart(Long bookId) {
        CartItem itemToRemove = findCartItemByBook(bookId);
        if (itemToRemove != null) {
            cartItems.remove(itemToRemove);
            updateCartTotal();
        }
    }

    public void updateQuantity(Long bookId, int quantity) {
        CartItem item = findCartItemByBook(bookId);
        if (item != null) {
            if (quantity <= 0) {
                cartItems.remove(item);
            } else {
                Book book = bookFacade.find(bookId);
                if (book != null && quantity > book.getStockQuantity()) {
                    quantity = book.getStockQuantity();
                }
                item.setQuantity(quantity);
                item.setTotalPrice(item.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
            }
            updateCartTotal();
        }
    }

    public void clearCart() {
        cartItems.clear();
        updateCartTotal();
    }

    private CartItem findCartItemByBook(Long bookId) {
        for (CartItem item : cartItems) {
            if (item.getBook().getId().equals(bookId)) {
                return item;
            }
        }
        return null;
    }

    private void updateCartTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            total = total.add(item.getTotalPrice());
        }
        shoppingCart.setTotalAmount(total);
    }

    public String checkout() {
        Users currentUser = (Users) facesContext.getExternalContext().getSessionMap().get("user");
        if (currentUser == null) {
            return "/login.xhtml?faces-redirect=true";
        }
        
        if (cartItems.isEmpty()) {
            return null;
        }
        
        return "/cart/checkout.xhtml?faces-redirect=true";
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public int getCartSize() {
        return cartItems.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public BigDecimal getCartTotal() {
        return shoppingCart.getTotalAmount() != null ? shoppingCart.getTotalAmount() : BigDecimal.ZERO;
    }
}
