package com.kraken.DataStructures.Items.DiscItems;

import com.kraken.DataStructures.Items.Books.Enumerations.Type;

/**
 * Created by Curtis on 11/14/2016.
 */
public class AudioBook extends DiscItem{
    String author;
    int ISBN;

    /**
     * Default constructor
     */
    public AudioBook() {
        super();
        setType(Type.AudioBook);
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
