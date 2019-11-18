package com.javaawesome.tagtesting;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;
import java.util.List;

@Entity
public class Player {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String username;
    private Session gameSession;
    private boolean isIt;
    private List<LatLng> locations;

    public Player(String username, Session gameSession) {
        this.username = username;
        this.gameSession = gameSession;
        this.isIt = false;
        this.locations = new LinkedList<>();
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Session getGameSession() {
        return gameSession;
    }

    public boolean isIt() {
        return isIt;
    }

    public List<LatLng> getLocations() {
        return locations;
    }

    public void setIt(boolean it) {
        isIt = it;
    }

    public void addLocations(LatLng location) {
        this.locations.add(location);
    }
}
