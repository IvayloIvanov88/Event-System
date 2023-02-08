package org.softuni.eventures.domain.models.service;

import org.softuni.eventures.domain.entities.Event;
import org.softuni.eventures.domain.entities.User;

import java.time.LocalDateTime;

public class OrderServiceModel {
    private String id;

    private LocalDateTime orderedOn;

    private Event event;

    private User customer;

    private Integer ticketsCount;

    public OrderServiceModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getOrderedOn() {
        return orderedOn;
    }

    public void setOrderedOn(LocalDateTime orderedOn) {
        this.orderedOn = orderedOn;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Integer getTicketsCount() {
        return ticketsCount;
    }

    public void setTicketsCount(Integer ticketsCount) {
        this.ticketsCount = ticketsCount;
    }
}
