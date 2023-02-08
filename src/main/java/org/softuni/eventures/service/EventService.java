package org.softuni.eventures.service;

import org.softuni.eventures.domain.models.service.EventServiceModel;
import org.softuni.eventures.domain.models.service.MyEventsServiceModel;

import java.util.Set;

public interface EventService {
    boolean createEvent(EventServiceModel eventServiceModel);

    void orderEvent(String eventId, String username, Integer tickets);

    Set<EventServiceModel> getAll();

    Set<EventServiceModel> getAvailable();

    Set<EventServiceModel> getUnavailable();

    EventServiceModel getById(String id);

    Set<MyEventsServiceModel> myEvents(String currentUser);
}
