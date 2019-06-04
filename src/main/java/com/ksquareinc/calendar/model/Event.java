package com.ksquareinc.calendar.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "Events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User creator;

    private String subject;

    private boolean isAllDay = false;

    private LocalDateTime dateBegin;

    private LocalDateTime dateEnd;

    private String Description;

    @OneToMany
    private List<User> guests;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<User> getGuests() {
        return guests;
    }

    public void setGuests(List<User> guests) {
        this.guests = guests;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isAllDay() {
        return isAllDay;
    }

    public void setAllDay(boolean allDay) {
        isAllDay = allDay;
    }

    public LocalDateTime getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(LocalDateTime dateBegin) {
        this.dateBegin = dateBegin;
    }

    public LocalDateTime getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDateTime dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}