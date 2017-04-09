package com.kraken.DataStructures.Items.Books;

import com.kraken.DataStructures.Items.Books.Enumerations.Type;

/**
 * Created by Curtis on 11/14/2016.
 */
public class EBook extends Book {
    String accessPoint;

    /**
     * Default constructor
     */
    public EBook() {
        super();this.setType(Type.eBook);
    }

    @Override
    public String toString() {
        return super.toString()
                + "\naccessPoint: " + accessPoint;
    }

    public String getAccessPoint() {
        return accessPoint;
    }

    public void setAccessPoint(String accessPoint) {
        this.accessPoint = accessPoint;
    }
}
