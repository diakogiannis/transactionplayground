package com.diakogiannis.transactionplayground.model.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class Cat implements Serializable {


    private Long id;
    private String catName;

    public Cat(){
        //default constructor
    }

    public Cat(Long id, String catName) {
        this.id = id;
        this.catName = catName;
    }

    public Cat(String catName) {
        this.catName = catName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }


    @Override
    public String toString() {
        return "Cat{" +
                "id=" + id +
                ", catName='" + catName + '\'' +

                '}';
    }
}

