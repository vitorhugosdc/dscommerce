package com.vitor.dscommerce.services;

import com.vitor.dscommerce.dto.OrderDTO;
import com.vitor.dscommerce.entities.Order;
import com.vitor.dscommerce.repositories.OrderRepository;
import com.vitor.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return new OrderDTO(order);
    }
}
