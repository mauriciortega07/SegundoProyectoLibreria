package com.ebac.segundoProyectoJava.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBook;

    @ManyToOne
    @JoinColumn(name = "idAuthor")
    private Author authorBook;

    @Column(name = "titleBook")
    private String titleBook;

    @Column(name = "releaseYear")
    private int releaseYear;

    @Column(name = "isbnCode")
    private String isbnCode;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private User userRents;

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    public Author getAuthorBook() {
        return authorBook;
    }

    public void setAuthorBook(Author authorBook) {
        this.authorBook = authorBook;
    }

    public String getTitleBook() {
        return titleBook;
    }

    public void setTitleBook(String titleBook) {
        this.titleBook = titleBook;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getIsbnCode() {
        return isbnCode;
    }

    public void setIsbnCode(String isbnCode) {
        this.isbnCode = isbnCode;
    }

    @Override
    public String toString() {
        return "Book{" +
                "idBook=" + idBook +
                ", auhtorBook=" + (authorBook != null ? authorBook.getCompleteName() : "null") +
                ", titleBook='" + titleBook + '\'' +
                ", releaseYear=" + releaseYear + '\'' +
                ", isbnCode='" + isbnCode +
                '}';
    }
}
