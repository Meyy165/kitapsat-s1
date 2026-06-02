package bean;

import entity.Users;
import enums.Role;
import facadeLocal.UserFacadeLocal;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class LoginBean implements Serializable {
    private Users user;

    @EJB
    UserFacadeLocal userFacade;

    @Inject
    FacesContext facesContext;

    public String login() {
        String email = user.getEmail() != null ? user.getEmail().trim() : null;
        String password = user.getPassword();

        Users loggedInUser = userFacade.login(email, password);
        if (loggedInUser != null) {
            facesContext.getExternalContext().getSessionMap().put("user", loggedInUser);
            if (Role.ADMIN.equals(loggedInUser.getRole())) {
                return "/admin/index.xhtml?faces-redirect=true";
            }
            return "/book/list.xhtml?faces-redirect=true";
        }

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Giriş başarısız", "E-posta veya parola hatalı.");
        facesContext.addMessage(null, msg);
        return null;
    }

    public Users getUser() {
        if (user == null) {
            user = new Users();
        }
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String logout() {
        facesContext.getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }
}
