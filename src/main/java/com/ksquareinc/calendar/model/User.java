package com.ksquareinc.calendar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "Users")
@Transactional
public class User implements Serializable {

    public static final String USER_USERNAME = "username";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Event> eventsCreated = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "guests", cascade = CascadeType.ALL)
    private List<Event> eventInvitations = new ArrayList<>();

    @Column(unique = true, name = USER_USERNAME)
    private String username;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Event> getEventsCreated() {
        return eventsCreated;
    }

    public void setEventsCreated(List<Event> eventsCreated) {
        this.eventsCreated = eventsCreated;
    }

    public List<Event> getEventInvitations() {
        return eventInvitations;
    }

    public void setEventInvitations(List<Event> eventInvitations) {
        this.eventInvitations = eventInvitations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) &&
                Objects.equals(eventsCreated, user.eventsCreated) &&
                Objects.equals(eventInvitations, user.eventInvitations) &&
                username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventsCreated, eventInvitations, username);
    }
}
