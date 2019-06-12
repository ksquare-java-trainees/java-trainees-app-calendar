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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id", referencedColumnName = "id", nullable = false)
    private User creator;

    private String subject;

    private boolean isAllDay;

    private LocalDateTime dateBegin;

    private LocalDateTime dateEnd;

    private String description;

    @ManyToMany(fetch=FetchType.EAGER)
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

    public boolean getIsAllDay() {
        return isAllDay;
    }

    public void setIsAllDay(boolean isAllDay) {
        this.isAllDay = isAllDay;
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
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
                ", description='" + description + '\'' +
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
                Objects.equals(description, event.description) &&
                Objects.equals(guests, event.guests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creator, subject, isAllDay, dateBegin, dateEnd, description, guests);
    }
}
