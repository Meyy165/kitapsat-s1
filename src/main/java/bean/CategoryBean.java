package bean;

import entity.Category;
import facadeLocal.BookFacadeLocal;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class CategoryBean implements Serializable {
    private Category category;
    private List<Category> categories;
    
    @EJB
    BookFacadeLocal bookFacade;

    public CategoryBean() {
        category = new Category();
    }

    @PostConstruct
    public void init() {
        // URL'den ID parametresini al
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String categoryId = params.get("id");
        
        if (categoryId != null && !categoryId.isEmpty()) {
            try {
                Long id = Long.parseLong(categoryId);
                category = bookFacade.findCategory(id);
            } catch (NumberFormatException e) {
                category = new Category();
            }
        }
    }

    public List<Category> getAllCategories() {
        if (categories == null) {
            categories = bookFacade.findAllCategories();
        }
        return categories;
    }

    public String addCategory() {
        bookFacade.createCategory(category);
        category = new Category();
        categories = null; // Listeyi yenile
        return "/admin/categories.xhtml?faces-redirect=true";
    }

    public String updateCategory() {
        bookFacade.editCategory(category);
        return "/admin/categories.xhtml?faces-redirect=true";
    }

    public String deleteCategory(Long categoryId) {
        Category categoryToDelete = bookFacade.findCategory(categoryId);
        if (categoryToDelete != null) {
            bookFacade.removeCategory(categoryToDelete);
        }
        return "/admin/categories.xhtml?faces-redirect=true";
    }

    public String editCategory(Long categoryId) {
        category = bookFacade.findCategory(categoryId);
        return "/admin/edit-category.xhtml?faces-redirect=true";
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
