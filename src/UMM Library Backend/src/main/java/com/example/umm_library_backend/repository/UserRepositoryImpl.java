package com.example.umm_library_backend.repository;

import com.example.umm_library_backend.exception.DataNotExistsException;
import com.example.umm_library_backend.model.BooksEntity;
import com.example.umm_library_backend.model.UsersEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private EntityManager em;

    public List<UsersEntity> findAll() {
        return em.createQuery("SELECT u FROM UsersEntity u", UsersEntity.class).getResultList();
    }

    public List<UsersEntity> findId(long id){
        UsersEntity user = em.find(UsersEntity.class, id);
        if(user == null){
            return null;
        }

        List<UsersEntity> list = new ArrayList<>();
        list.add(user);

        return list;
    }

    public void save(UsersEntity user){
        em.persist(user);
    }

    public UsersEntity update(UsersEntity user) {
        return em.merge(user);
    }

    public Optional<UsersEntity> findByEmail(String email){
        String sql = "SELECT u FROM UsersEntity u WHERE u.email=:email";
        TypedQuery<UsersEntity> query = em.createQuery(sql, UsersEntity.class);
        query.setParameter("email", email);

        List<UsersEntity> users = query.getResultList();

        if (users.isEmpty()){
            return Optional.empty();
        } else {
            return Optional.of(users.get(0));
        }
    }

    public void deleteById(Long id) {
        UsersEntity user = em.find(UsersEntity.class, id);
        if (user != null) {
            em.remove(user);
            return;
        }

        throw new DataNotExistsException("User not found");
    }
}
