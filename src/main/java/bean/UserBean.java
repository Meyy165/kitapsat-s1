package bean;

import entity.Users;
import facadeLocal.UserFacadeLocal;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import util.PasswordUtil;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class UserBean implements Serializable {
    private Users user;
    private String confirmPassword;
    private String searchKeyword;
    private List<Users> allUsers;
    private int firstRow = 0;
    
    @EJB
    UserFacadeLocal userFacade;
    
    @Inject
    FacesContext facesContext;

    public UserBean() {
        user = new Users();
    }

    public String register() {
        if (!user.getPassword().equals(confirmPassword)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kayıt Hatası", "Şifreler eşleşmiyor.");
            facesContext.addMessage(null, msg);
            return null;
        }

        Users existingUser = userFacade.findByEmail(user.getEmail());
        if (existingUser != null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kayıt Hatası", "Bu e-posta adresi zaten kullanılıyor.");
            facesContext.addMessage(null, msg);
            return null;
        }

        try {
            userFacade.createUser(user);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Hesabınız başarıyla oluşturuldu. Giriş yapabilirsiniz.");
            facesContext.addMessage(null, msg);
            return "/login.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kayıt Hatası", "Bir hata oluştu. Lütfen tekrar deneyin.");
            facesContext.addMessage(null, msg);
            return null;
        }
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void loadCurrentUser() {
        Users currentUser = (Users) facesContext.getExternalContext().getSessionMap().get("user");
        if (currentUser != null) {
            this.user = currentUser;
        }
    }

    public String editProfile() {
        System.out.println("=== editProfile() METODU ÇAĞRILDI ===");
        Users currentUser = (Users) facesContext.getExternalContext().getSessionMap().get("user");
        if (currentUser == null) {
            System.out.println("Kullanıcı bulunamadı, login sayfasına yönlendiriliyor");
            return "/login.xhtml?faces-redirect=true";
        }
        
        try {
            // Form verilerini currentUser'a kopyala
            currentUser.setName(this.user.getName());
            currentUser.setSurname(this.user.getSurname());
            currentUser.setEmail(this.user.getEmail());
            currentUser.setPhone(this.user.getPhone());
            currentUser.setAddress(this.user.getAddress());
            
            // Veritabanında güncelle
            userFacade.editUser(currentUser);
            
            // Session'daki kullanıcıyı güncelle
            facesContext.getExternalContext().getSessionMap().put("user", currentUser);
            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Profil bilgileriniz güncellendi.");
            facesContext.addMessage(null, msg);
            return "/user/profile.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Profil güncellenirken bir hata oluştu.");
            facesContext.addMessage(null, msg);
            return null;
        }
    }

    public String changePassword() {
        Users currentUser = (Users) facesContext.getExternalContext().getSessionMap().get("user");
        if (currentUser == null) {
            return "/login.xhtml?faces-redirect=true";
        }
        
        if (!user.getPassword().equals(confirmPassword)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Yeni şifreler eşleşmiyor.");
            facesContext.addMessage(null, msg);
            return null;
        }
        
        try {
            currentUser.setPassword(PasswordUtil.hash(user.getPassword()));
            userFacade.editUser(currentUser);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Şifreniz başarıyla değiştirildi.");
            facesContext.addMessage(null, msg);
            return "/user/profile.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Şifre değiştirilirken bir hata oluştu.");
            facesContext.addMessage(null, msg);
            return null;
        }
    }

    // Admin metotları
    public List<Users> getAllUsers() {
        if (allUsers == null) {
            allUsers = userFacade.usersList();
        }
        return allUsers;
    }

    public String searchUsers() {
        try {
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                String cleanKeyword = searchKeyword.trim().toLowerCase();
                allUsers = userFacade.usersList().stream()
                        .filter(u -> u.getName().toLowerCase().contains(cleanKeyword) ||
                                   u.getSurname().toLowerCase().contains(cleanKeyword) ||
                                   u.getEmail().toLowerCase().contains(cleanKeyword))
                        .toList();
            } else {
                allUsers = userFacade.usersList();
            }
            firstRow = 0; // Aramada sayfayı sıfırla
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Arama sırasında bir hata oluştu.");
            facesContext.addMessage(null, msg);
        }
        return null; // Aynı sayfada kal
    }

    
    public String deleteUser(Long userId) {
        try {
            Users userToDelete = userFacade.find(userId);
            
            if (userToDelete != null && userToDelete.getRole() != enums.Role.ADMIN) {
                userFacade.remove(userToDelete);
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Kullanıcı silindi.");
                facesContext.addMessage(null, msg);
                allUsers = null; // Listeyi yenile
                return "/admin/users.xhtml?faces-redirect=true";
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Admin kullanıcıları silinemez.");
                facesContext.addMessage(null, msg);
                return null;
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Kullanıcı silinirken bir hata oluştu.");
            facesContext.addMessage(null, msg);
            return null;
        }
    }

    public String previousPage() {
        if (firstRow > 0) {
            firstRow = Math.max(0, firstRow - 10);
        }
        return null;
    }

    public String nextPage() {
        if (allUsers != null && firstRow + 10 < allUsers.size()) {
            firstRow += 10;
        }
        return null;
    }

    public int getTotalUsersCount() {
        return getAllUsers().size();
    }

    // Getter ve Setter metotları
    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public int getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }
}
