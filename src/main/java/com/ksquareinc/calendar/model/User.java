package com.ksquareinc.calendar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "Users")
@Transactional
public class User {

    public static final String USER_USERNAME = "username";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy = "creator")
    private List<Event> eventsCreated;

    @JsonIgnore
    @ManyToMany(mappedBy = "guests")
    private List<Event> eventInvitations  = new ArrayList<>();

    private Long ssoId = -1L;

    @Column(unique = true, name = USER_USERNAME)
    private String username;

    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSsoId() {
        return ssoId;
    }

    public void setSsoId(Long ssoId) {
        this.ssoId = ssoId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
        return Objects.equals(id, user.id) &&
                Objects.equals(eventsCreated, user.eventsCreated) &&
                Objects.equals(eventInvitations, user.eventInvitations) &&
                Objects.equals(ssoId, user.ssoId) &&
                Objects.equals(username, user.username) &&
                Objects.equals(token, user.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventsCreated, eventInvitations, ssoId, username, token);
    }
}
