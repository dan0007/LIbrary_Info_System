package com.kraken.DataStructures.Items.DiscItems;

import com.kraken.DataStructures.Items.Books.Enumerations.Type;

/**
 * Created by Curtis on 11/14/2016.
 */
public class DVD extends DiscItem{

    String director;
    String mainActor;

    /**
     * Default constructor
     */
    public DVD() {
        super();
        setType(Type.DVD);
    }

    @Override
    public String toString() {
        return super.toString()
                + "\nDirector: " + director
                + "\nMainActor: " + mainActor;
    }

    public String getDirector() {
        return director;
    }

    public String getMainActor() {
        return mainActor;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setMainActor(String mainActor) {
        this.mainActor = mainActor;
    }
}
