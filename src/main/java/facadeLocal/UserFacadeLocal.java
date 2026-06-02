package facadeLocal;

import entity.Users;
import java.util.List;

public interface UserFacadeLocal {
    void createUser(Users u);
    Users editUser(Users entity);
    void remove(Users entity);
    List<Users> usersList();
    Users login(String email, String password);
    Users findByEmail(String email);
    Users find(Long id);
}
