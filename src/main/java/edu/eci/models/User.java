package edu.eci.models;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable{
    private String name;
    private UUID id;

    public User() {
    }

    public User(String name, UUID id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


}
