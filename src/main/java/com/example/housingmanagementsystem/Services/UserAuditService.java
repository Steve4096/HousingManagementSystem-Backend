package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.Models.User;
import jakarta.persistence.EntityManager;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAuditService {

    private final EntityManager entityManager;

    public UserAuditService(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    public List<Object[]> getUserHistory(Long userId){
        AuditReader auditReader= AuditReaderFactory.get(entityManager);

        return auditReader.createQuery()
                .forRevisionsOfEntity(User.class,false,true)
                .add(AuditEntity.id().eq(userId))
                .getResultList();
    }
}
