package com.kraken.DataStructures.Items.DiscItems;

import com.kraken.DataStructures.Items.Books.Enumerations.Type;

/**
 * Created by Curtis on 11/14/2016.
 */
public class CD extends DiscItem {

    String artist;

    /**
     * Default constructor
     */
    public CD() {
        super();
        setType(Type.CD);
    }

    @Override
    public String toString() {
        return super.toString()
                + "\nArtist: " + artist;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
