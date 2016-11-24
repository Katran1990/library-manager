package com.botscrew.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private String author;
    @NotNull
    private String name;

    public Book(){}

    public Book(String author, String name) {
        this.author = author;
        this.name = name;
    }

    public Book(long id, String author, String name) {
        this.id = id;
        this.author = author;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
