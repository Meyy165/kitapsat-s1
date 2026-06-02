package bean;

import facadeLocal.BookFacadeLocal;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Singleton
@Startup
public class CatalogStartupBean {
    @EJB
    private BookFacadeLocal bookFacade;

    @PostConstruct
    public void init() {
        bookFacade.ensureMinimumBooks(200);
    }
}
