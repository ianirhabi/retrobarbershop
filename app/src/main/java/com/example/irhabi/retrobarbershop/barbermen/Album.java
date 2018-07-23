package com.example.irhabi.retrobarbershop.barbermen;

/**
 * Created by Lincoln on 18/05/16.
 */
public class Album {
    int id;
    String name;
    String description;
    double price;
    String thumbnail;
    String chef;
    String timestamp;

    int numOfSongs;
    int thumbnail2;

    public Album() {
    }
    public Album(String name, int numOfSongs, int thumbnail) {
        this.name = name;
        this.numOfSongs = numOfSongs;
        this.thumbnail2 = thumbnail;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getThumbnail() {
        return thumbnail2;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public int getNumOfSongs() {
        return numOfSongs;
    }

    public String getChef() {
        return chef;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
