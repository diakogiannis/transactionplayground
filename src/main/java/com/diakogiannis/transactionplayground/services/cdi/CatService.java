package com.diakogiannis.transactionplayground.services.cdi;

import com.diakogiannis.transactionplayground.model.entity.Cat;
import com.diakogiannis.transactionplayground.repository.CatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static javax.ejb.TransactionAttributeType.REQUIRED;


@Stateless
public class CatService {

    Logger log = LoggerFactory.getLogger(CatService.class);

    static final List<Cat> CATS = generateCats();

    @EJB
    CatRepository catRepository;

    public List<Cat> findAll() {
        return catRepository.findAll();
    }

    @TransactionAttribute(REQUIRED)
    public Cat saveACat(Cat c) {
        return catRepository.save(c);
    }

    @TransactionAttribute(REQUIRED)
    public void saveMultipleRandomCats(Boolean throwRandomException) {

        AtomicInteger ai = new AtomicInteger(0);

        CATS.stream().forEach(c->{
                saveACat(c);
                throwRandomException(ai,throwRandomException);
        });


    }

    @TransactionAttribute(REQUIRED)
    public void saveMultipleRandomCatsInParallel(Boolean throwRandomException) {
        AtomicInteger ai = new AtomicInteger(0);
        CATS.parallelStream().forEach(c->{
            saveACat(c);
            throwRandomException(ai,throwRandomException);
        });
    }

    public void deleteAllCats(){
        catRepository.truncateCats();
    }

    private void throwRandomException(AtomicInteger ai, Boolean throwRandomException) throws RuntimeException{
        if(ai.incrementAndGet() > 1 && System.currentTimeMillis()%2 == 0 && throwRandomException){
            log.info("Generating random exception to cause a rollback");
            throw new RuntimeException();
        }
    }

    private static final List<Cat> generateCats(){

        List<Cat> cats = new ArrayList<>();

        for (int i=0;i<10;i++){
            cats.add(new Cat("My Cat "+i));
        }
        return cats;
    }

}
