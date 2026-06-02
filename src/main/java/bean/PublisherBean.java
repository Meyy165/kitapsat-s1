package bean;

import entity.Publisher;
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
public class PublisherBean implements Serializable {
    private Publisher publisher;
    private List<Publisher> publishers;
    
    @EJB
    BookFacadeLocal bookFacade;

    public PublisherBean() {
        publisher = new Publisher();
    }

    @PostConstruct
    public void init() {
        // URL'den ID parametresini al
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String publisherId = params.get("id");
        
        if (publisherId != null && !publisherId.isEmpty()) {
            try {
                Long id = Long.parseLong(publisherId);
                publisher = bookFacade.findPublisher(id);
            } catch (NumberFormatException e) {
                publisher = new Publisher();
            }
        }
    }

    public List<Publisher> getAllPublishers() {
        if (publishers == null) {
            publishers = bookFacade.findAllPublishers();
        }
        return publishers;
    }

    public String addPublisher() {
        bookFacade.createPublisher(publisher);
        publisher = new Publisher();
        publishers = null; // Listeyi yenile
        return "/admin/publishers.xhtml?faces-redirect=true";
    }

    public String updatePublisher() {
        bookFacade.editPublisher(publisher);
        return "/admin/publishers.xhtml?faces-redirect=true";
    }

    public String deletePublisher(Long publisherId) {
        Publisher publisherToDelete = bookFacade.findPublisher(publisherId);
        if (publisherToDelete != null) {
            bookFacade.removePublisher(publisherToDelete);
        }
        return "/admin/publishers.xhtml?faces-redirect=true";
    }

    public String editPublisher(Long publisherId) {
        publisher = bookFacade.findPublisher(publisherId);
        return "/admin/edit-publisher.xhtml?faces-redirect=true";
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public List<Publisher> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<Publisher> publishers) {
        this.publishers = publishers;
    }
}
