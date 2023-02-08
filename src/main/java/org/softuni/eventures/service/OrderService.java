package org.softuni.eventures.service;

import org.softuni.eventures.domain.models.service.OrderServiceModel;

import java.util.Set;

public interface OrderService {
    boolean createOrder(OrderServiceModel orderServiceModel);

    Set<OrderServiceModel> getAll();

    Set<OrderServiceModel> getAllByUserId(String userId);
}
