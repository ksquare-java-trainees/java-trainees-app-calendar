package com.ksquareinc.calendar.model;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "Events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id", referencedColumnName = "id" ,nullable = false)
    private User creator;

    private String subject;

    private boolean isAllDay;

    private LocalDateTime dateBegin;

    private LocalDateTime dateEnd;

    private String Description;

    @ManyToMany(cascade = {
            CascadeType.ALL
    }, fetch=FetchType.EAGER)
    @JoinTable(name = "event_guests",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> guests  = new ArrayList<>();

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

    public LocalDateTime getBegin() {
        return dateBegin;
    }

    public void setBegin(LocalDateTime begin) {
        this.dateBegin = begin;
    }

    public LocalDateTime getEnd() {
        return dateEnd;
    }

    public void setEnd(LocalDateTime end) {
        this.dateEnd = end;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", creator=" + creator.getUsername() +
                ", subject='" + subject + '\'' +
                ", isAllDay=" + isAllDay +
                ", dateBegin=" + dateBegin +
                ", dateEnd=" + dateEnd +
                ", Description='" + Description + '\'' +
                ", guests=" + guests.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return isAllDay == event.isAllDay &&
                Objects.equals(id, event.id) &&
                Objects.equals(creator, event.creator) &&
                Objects.equals(subject, event.subject) &&
                Objects.equals(dateBegin, event.dateBegin) &&
                Objects.equals(dateEnd, event.dateEnd) &&
                Objects.equals(Description, event.Description) &&
                Objects.equals(guests, event.guests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creator, subject, isAllDay, dateBegin, dateEnd, Description, guests);
    }
}
