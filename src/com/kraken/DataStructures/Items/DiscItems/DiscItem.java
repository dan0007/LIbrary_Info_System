package com.kraken.DataStructures.Items.DiscItems;

import com.kraken.DataStructures.Items.Item;

/**
 * Created by Curtis on 11/14/2016.
 *
 * Abstract representation of a DiscItem. All items that are discs must extend this.
 */
public abstract class DiscItem extends Item{
    int numDiscs;
    String runtime; //in minutes?

    /**
     * Default constructor
     */
    public DiscItem() {
        super();
    }

    @Override
    public String toString() {
        return super.toString()
                + "\nNumDiscs: " + numDiscs
                + "\nRuntime: " + runtime;
    }
    public String getRuntime() {
        return runtime;
    }

    public int getNumDiscs() {
        return numDiscs;
    }

    public void setNumDiscs(int numDiscs) {
        this.numDiscs = numDiscs;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }
}
