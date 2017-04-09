package com.kraken.DataStructures.Items;

import com.kraken.DataStructures.Items.Books.Enumerations.Status;
import com.kraken.DataStructures.Items.Books.Enumerations.Type;

/**
 * Created by Curtis on 11/14/2016.
 *
 * Abstract item superclass. Contains variables and methods used by all item objects.
 */
public abstract class Item {

    int cost;
    int itemID;
    String genre;
    String title;
    Status status;
    Type type;

    /**
     * Default Constructor, creates empty item object.
     */
    public Item() {

    }

    /**
     * Updates the item to a new status.
     * @param status the new status to update the item to
     */
    public void updateStatus(Status status) {
        this.status = status;

    }

    //TODO
    //Class diagram calls for deleteItem and createNewItem, I believe those are redundant since
    //the database manager handles that. I left them out for now.

    public String toString() {
        return    "Title: " + title
                + "\nID: "+ itemID
                + "\nGenre: " + genre
                + "\nCost: " + cost
                + "\nStatus: " + status
                + "\nType: " + type;

    }

    public int getCost() {
        return cost;
    }

    public Status getStatus() {
        return status;
    }

    public Type getType() {
        return type;
    }

    public int getItemID() {
        return itemID;
    }

    public String getGenre() {
        return genre;
    }

    public String getTitle() {
        return title;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
