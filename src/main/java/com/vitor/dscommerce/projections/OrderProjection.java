package com.vitor.dscommerce.projections;

import com.vitor.dscommerce.entities.OrderStatus;

import java.time.Instant;

public interface OrderProjection {

    Long getId();
    Instant getMoment();
    OrderStatus getStatus();
    Long getClientId();
    String getClientName();
    Long getPaymentId();
    Instant getPaymentMoment();
    Long getProductId();
    String getProductName();
    Double getProductPrice();
    Integer getProductQuantity();

    /*total() seria um método do próprio OrderDTO, calculado nele
    * Teria que ter OrderItemDTO recebendo os dados
    * subTotal() calculado nesse OrderItemDTO*/
}
