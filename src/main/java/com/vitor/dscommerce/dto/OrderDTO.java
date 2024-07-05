package com.vitor.dscommerce.dto;

import com.vitor.dscommerce.entities.Order;
import com.vitor.dscommerce.entities.OrderItem;
import com.vitor.dscommerce.entities.OrderStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {

    private Long id;
    private Instant moment;
    private OrderStatus status;
    private UserMinDTO client;
    private PaymentMinDTO payment;
    private List<OrderItemDTO> items = new ArrayList<>();

    public OrderDTO(Long id, Instant moment, OrderStatus status, UserMinDTO client, PaymentMinDTO payment) {
        this.id = id;
        this.moment = moment;
        this.status = status;
        this.client = client;
        this.payment = payment;
    }

    public OrderDTO(Order entity) {
        id = entity.getId();
        moment = entity.getMoment();
        status = entity.getStatus();
        client = new UserMinDTO(entity.getClient());
        /*Pode ser um pedido que não está pago, então verifica se é nulo antes*/
        payment = (entity.getPayment() == null) ? null : new PaymentMinDTO(entity.getPayment());
        for (OrderItem orderItem : entity.getItems()) {
            items.add(new OrderItemDTO(orderItem));
        }
    }

    public Long getId() {
        return id;
    }

    public Instant getMoment() {
        return moment;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public UserMinDTO getClient() {
        return client;
    }

    public PaymentMinDTO getPayment() {
        return payment;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public Double getTotal() {
        double sum = 0.0;
        for (OrderItemDTO item : items) {
            sum += item.getSubTotal();
        }
        return sum;
    }

    /*public Double getTotal(){
        return items.stream().mapToDouble(OrderItemDTO::getSubTotal).sum();
    }*/
}
