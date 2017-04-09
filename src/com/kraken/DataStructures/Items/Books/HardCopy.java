package com.kraken.DataStructures.Items.Books;

import com.kraken.DataStructures.Items.Books.Enumerations.Type;

/**
 * Created by Curtis on 11/14/2016.
 */
public class HardCopy extends Book {
    String locationInLibrary;

    /**
     * Default constructor
     */
    public HardCopy() {
        super();
        this.setType(Type.HardCopy);
    }

    @Override
    public String toString() {
        return super.toString()
                + "\nLocation: " + locationInLibrary;
    }

    public String getLocationInLibrary() {
        return locationInLibrary;
    }

    public void setLocationInLibrary(String locationInLibrary) {
        this.locationInLibrary = locationInLibrary;
    }
}
