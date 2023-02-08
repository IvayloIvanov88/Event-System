package org.softuni.eventures.service;

import org.modelmapper.ModelMapper;
import org.softuni.eventures.domain.entities.Event;
import org.softuni.eventures.domain.entities.Order;
import org.softuni.eventures.domain.entities.User;
import org.softuni.eventures.domain.models.service.OrderServiceModel;
import org.softuni.eventures.repository.EventRepository;
import org.softuni.eventures.repository.OrderRepository;
import org.softuni.eventures.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public boolean createOrder(OrderServiceModel orderServiceModel) { ;
        Order orderEntity = this.modelMapper.map(orderServiceModel, Order.class);

        try {
            this.orderRepository.save(orderEntity);
        } catch (Exception ignored) {
            //TODO: Fix this when discover exception type.
            return false;
        }

        return true;
    }

    @Override
    public Set<OrderServiceModel> getAll() {
        return this.orderRepository
                .findAll()
                .stream()
                .map(x -> this.modelMapper.map(x, OrderServiceModel.class))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<OrderServiceModel> getAllByUserId(String userId) {
        return this.orderRepository
                .findAllByUserId(userId)
                .stream()
                .map(x -> this.modelMapper.map(x, OrderServiceModel.class))
                .collect(Collectors.toUnmodifiableSet());
    }
}
