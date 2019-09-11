package com.diakogiannis.transactionplayground.repository;

import com.diakogiannis.transactionplayground.model.entity.Cat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static javax.ejb.TransactionAttributeType.REQUIRED;

@Stateless
public class CatRepository {

    @PersistenceContext
    EntityManager em;

    Logger log = LoggerFactory.getLogger(CatRepository.class);

    public List<Cat> findAll(){
        return em.createQuery("from Cat c").getResultList();
    }

    @TransactionAttribute(REQUIRED)
    public Cat save(final Cat cat) {
        log.info("saving cat {}", cat);
        em.merge(cat);
        return cat;
    }

    public int truncateCats(){
        return em.createQuery("DELETE FROM Cat").executeUpdate();
    }

}
