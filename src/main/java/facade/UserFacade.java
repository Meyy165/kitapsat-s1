package facade;

import entity.Users;
import enums.Role;
import facadeLocal.UserFacadeLocal;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import util.PasswordUtil;

import java.util.List;

@Stateless
public class UserFacade extends AbstractFacade implements UserFacadeLocal {

    @Override
    public void createUser(Users u) {
        u.setRole(Role.USER);
        u.setPassword(PasswordUtil.hash(u.getPassword()));
        this.entityManager.persist(u);
    }

    @Override
    public Users editUser(Users entity) {
        return this.entityManager.merge(entity);
    }

    @Override
    public void remove(Users entity) {
        Users merged = this.entityManager.merge(entity);
        this.entityManager.remove(merged);
    }

    @Override
    public List<Users> usersList() {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);
        CriteriaQuery<Users> all = cq.select(root);
        TypedQuery<Users> q = this.entityManager.createQuery(all);
        return q.getResultList();
    }

    @Override
    public Users login(String email, String password) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);
        cq.where(cb.equal(root.get("eposta"), email));
        CriteriaQuery<Users> all = cq.select(root);
        TypedQuery<Users> q = this.entityManager.createQuery(all);
        List<Users> found = q.getResultList();
        if (found.isEmpty()) {
            return null;
        }

        Users user = found.get(0);
        if (!PasswordUtil.verify(password, user.getPassword())) {
            return null;
        }
        if (!PasswordUtil.isHashed(user.getPassword())) {
            user.setPassword(PasswordUtil.hash(password));
            user = this.entityManager.merge(user);
        }
        return user;
    }

    @Override
    public Users findByEmail(String email) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);
        cq.where(cb.equal(root.get("eposta"), email));
        CriteriaQuery<Users> all = cq.select(root);
        TypedQuery<Users> q = this.entityManager.createQuery(all);
        List<Users> found = q.getResultList();
        if (found.isEmpty()) {
            return null;
        } else {
            return found.get(0);
        }
    }

    @Override
    public Users find(Long id) {
        return this.entityManager.find(Users.class, id);
    }
}
