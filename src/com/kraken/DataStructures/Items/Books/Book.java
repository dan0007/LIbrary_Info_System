package com.kraken.DataStructures.Items.Books;

import com.kraken.DataStructures.Items.Item;

/**
 * Created by Curtis on 11/14/2016.
 *
 * Abstract Class to represent a book. All book types must extend this.
 */
public abstract class Book extends Item {
    String author;
    int ISBN;

    /**
     * Default Constructor, creates empty item object.
     */
    public Book() {
    }

    @Override
    public String toString() {
        return super.toString()
                + "\nAuthor: " + author
                + "\nISBN: " + ISBN;
    }

    public String getAuthor() {
        return author;
    }

    public int getISBN() {
        return ISBN;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
    }
}
