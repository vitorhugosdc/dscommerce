package com.vitor.dscommerce.entities;
/*enum por padrão já compara com equals, então não precisa gerar*/
public enum OrderStatus {

    WAITING_PAYMENT,
    PAID,
    SHIPPED,
    DELIVERED,
    CANCELED;
}
