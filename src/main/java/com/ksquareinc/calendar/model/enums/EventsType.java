package com.ksquareinc.calendar.model.enums;

public enum EventsType {
    CREATIONS ("eventsCreated"),
    INVITATIONS("eventInvitations");

    private String attribute;

    EventsType(String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }

    @Override
    public String toString() {
        return "EventsType{" +
                "attribute='" + attribute + '\'' +
                '}';
    }
}
