package com.ebac.segundoProyectoJava.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAuthor;

    @Column(name = "completeName")
    private String completeName;

    @Column(name = "biography")
    private String biography;

    @OneToMany(mappedBy = "authorBook", cascade = {CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    private List<Book> booksPublished = new ArrayList<>();

    public void setIdAuthor(int idAuthor){
        this.idAuthor = idAuthor;
    }

    public int getIdAuthor(){
        return idAuthor;
    }

    public void setCompleteName(String completeName){
        this.completeName = completeName;
    }

    public String getCompleteName(){
        return completeName;
    }

    public void setBiography(String biography){
        this.biography = biography;
    }

    public String getBiography(){
        return biography;
    }

    public void addBookPublished(Book book) {
        booksPublished.add(book);
    }

    public List<Book> getBooksPublished(){
        return booksPublished;
    }


    @Override
    public String toString() {
        return "Author{" +
                "idAuthor=" + idAuthor +
                ", completeName='" + completeName + '\'' +
                ", biography='" + biography + '\'' +
                ", bookPublished=" + (booksPublished != null ? booksPublished.size() : 0) +
                '}';
    }
}
