package org.softuni.eventures.web.controllers;

import org.modelmapper.ModelMapper;
import org.softuni.eventures.domain.models.view.AllOrdersOrderViewModel;
import org.softuni.eventures.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class OrderController extends BaseController {
    private final OrderService orderService;

    private final ModelMapper modelMapper;

    @Autowired
    public OrderController(OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public ModelAndView allOrders(ModelAndView modelAndView) {
        Set<AllOrdersOrderViewModel> allOrdersViewModel = this
                .orderService
                .getAll()
                .stream()
                .map(x -> this.modelMapper.map(x, AllOrdersOrderViewModel.class))
                .collect(Collectors.toUnmodifiableSet());

        modelAndView.addObject("allOrders", allOrdersViewModel);

        return this.view("orders-all", modelAndView);
    }
}
