package bean;

import entity.Author;
import facadeLocal.BookFacadeLocal;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class AuthorBean implements Serializable {
    private Author author;
    private List<Author> authors;
    
    @EJB
    BookFacadeLocal bookFacade;

    public AuthorBean() {
        author = new Author();
    }

    @PostConstruct
    public void init() {
        // URL'den ID parametresini al
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String authorId = params.get("id");
        
        if (authorId != null && !authorId.isEmpty()) {
            try {
                Long id = Long.parseLong(authorId);
                author = bookFacade.findAuthor(id);
            } catch (NumberFormatException e) {
                author = new Author();
            }
        }
    }

    public List<Author> getAllAuthors() {
        if (authors == null) {
            authors = bookFacade.findAllAuthors();
        }
        return authors;
    }

    public String addAuthor() {
        bookFacade.createAuthor(author);
        author = new Author();
        authors = null; // Listeyi yenile
        return "/admin/authors.xhtml?faces-redirect=true";
    }

    public String updateAuthor() {
        bookFacade.editAuthor(author);
        return "/admin/authors.xhtml?faces-redirect=true";
    }

    public String deleteAuthor(Long authorId) {
        Author authorToDelete = bookFacade.findAuthor(authorId);
        if (authorToDelete != null) {
            bookFacade.removeAuthor(authorToDelete);
        }
        return "/admin/authors.xhtml?faces-redirect=true";
    }

    public String editAuthor(Long authorId) {
        author = bookFacade.findAuthor(authorId);
        return "/admin/edit-author.xhtml?faces-redirect=true";
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
}
