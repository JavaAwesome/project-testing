package com.javaawesome.tagtesting;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;
import java.util.List;

@Entity
public class Session {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private List<Player> players;
    private LatLng location;
    private int radius;

    public Session(String title, LatLng location, int radius) {
        this.title = title;
        this.players = new LinkedList<>();
        this.location = location;
        this.radius = radius;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public LatLng getLocation() {
        return location;
    }

    public int getRadius() {
        return radius;
    }

    public void addPlayers(Player player) {
        this.players.add(player);
    }
}
