package org.softuni.eventures.web.controllers;

import org.modelmapper.ModelMapper;
import org.softuni.eventures.domain.models.binding.EventCreateBindingModel;
import org.softuni.eventures.domain.models.binding.EventOrderBindingModel;
import org.softuni.eventures.domain.models.service.EventServiceModel;
import org.softuni.eventures.domain.models.view.AllEventsEventViewModel;
import org.softuni.eventures.domain.models.view.MyEventsEventViewModel;
import org.softuni.eventures.service.CloudinaryService;
import org.softuni.eventures.service.EventService;
import org.softuni.eventures.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/events")
public class EventController extends BaseController {
    private final EventService eventService;

    private final CloudinaryService cloudinaryService;

    private final ModelMapper modelMapper;

    private final JmsTemplate jmsTemplate;

    @Autowired
    public EventController(EventService eventService, UserService userService, CloudinaryService cloudinaryService, ModelMapper modelMapper, JmsTemplate jmsTemplate) {
        this.eventService = eventService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
        this.jmsTemplate = jmsTemplate;
    }


    @GetMapping("/all")
    public ModelAndView allEvents(ModelAndView modelAndView) {
        return this.view("events-all");
    }

    @GetMapping(value = "/api/all", produces = "application/json")
    @ResponseBody
    public Set<AllEventsEventViewModel> apiAllEvents() {
        return this
                .eventService
                .getAll()
                .stream()
                .map(x -> {
                    AllEventsEventViewModel viewModel = this.modelMapper.map(x, AllEventsEventViewModel.class);

                    viewModel.setRemainingTickets(x.getTotalTickets() - x.getSoldTickets());
                    viewModel.setAvailable(
                            viewModel.getStartTime().compareTo(LocalDateTime.now()) > 0 && viewModel.getRemainingTickets() > 0
                    );

                    return viewModel;
                })
                .collect(Collectors.toUnmodifiableSet());
    }

    @GetMapping(value = "/api/available", produces = "application/json")
    @ResponseBody
    public Set<AllEventsEventViewModel> apiAvailableEvents() {

        return this
                .eventService
                .getAvailable()
                .stream()
                .map(x -> {
                    AllEventsEventViewModel viewModel = this.modelMapper.map(x, AllEventsEventViewModel.class);

                    viewModel.setAvailable(true);
                    viewModel.setRemainingTickets(x.getTotalTickets() - x.getSoldTickets());

                    return viewModel;
                })
                .collect(Collectors.toUnmodifiableSet());
    }

    @GetMapping(value = "/api/unavailable", produces = "application/json")
    @ResponseBody
    public Set<AllEventsEventViewModel> apiUnavailableEvents() {
        return this
                .eventService
                .getUnavailable()
                .stream()
                .map(x -> {
                    AllEventsEventViewModel viewModel = this.modelMapper.map(x, AllEventsEventViewModel.class);

                    viewModel.setAvailable(false);
                    viewModel.setRemainingTickets(x.getTotalTickets() - x.getSoldTickets());

                    return viewModel;
                })
                .collect(Collectors.toUnmodifiableSet());
    }

    @GetMapping("/my")
    public ModelAndView myEvents(Principal principal, ModelAndView modelAndView) {
        Set<MyEventsEventViewModel> myEventsViewModel = this
                .eventService
                .myEvents(principal.getName())
                .stream()
                .map(x -> this.modelMapper.map(x, MyEventsEventViewModel.class))
                .collect(Collectors.toUnmodifiableSet());

        modelAndView.addObject("myEvents", myEventsViewModel);

        return this.view("events-my", modelAndView);
    }

    @GetMapping("/create")
    public ModelAndView createEvent() {
        return this.view("events-create");
    }

    @PostMapping("/create")
    public ModelAndView createEventConfirm(@ModelAttribute EventCreateBindingModel eventCreateBindingModel) throws IOException {
        EventServiceModel eventServiceModel = this.modelMapper
                .map(eventCreateBindingModel, EventServiceModel.class);

        String pictureUrl = this.cloudinaryService.uploadImage(eventCreateBindingModel.getEventPicture());

        if (pictureUrl == null) {
            throw new IllegalArgumentException("Event Picture upload failed.");
        }

        eventServiceModel.setPictureUrl(pictureUrl);

        boolean result = this.eventService
                .createEvent(eventServiceModel);

        if (!result) {
            throw new IllegalArgumentException("Event creation failed.");
        }

        return this.redirect("all");
    }

    @PostMapping("/order")
    public ModelAndView order(@ModelAttribute EventOrderBindingModel eventOrderBindingModel, Principal principal, ModelAndView modelAndView) {
        Map<String, Object> jmsArguments = new HashMap<>() {{
            put("tickets", eventOrderBindingModel.getTickets());
            put("eventId", eventOrderBindingModel.getEventId());
            put("username", principal.getName());
        }};

        jmsTemplate.setDefaultDestinationName("order-event-listener");
        jmsTemplate.convertAndSend(jmsArguments);

        return this.redirect("all");
    }
}
