package com.newsbhaskar.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public String getBooks() {
        return books;
    }

    public void setBooks(String books) {
        this.books = books;
    }

    private String books;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
