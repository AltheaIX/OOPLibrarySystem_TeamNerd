package com.example.umm_library_backend.repository;

import com.example.umm_library_backend.model.UsersEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class AuthRepositoryImpl implements AuthRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
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

    @Override
    public void save(UsersEntity user){
        em.persist(user);
    }

    @Transactional
    public void updatePasswordByEmail(String email, String password){
        em.createQuery("UPDATE UsersEntity u SET u.password=:password WHERE u.email=:email")
                .setParameter("password", password)
                .setParameter("email", email)
                .executeUpdate();

    }
}
