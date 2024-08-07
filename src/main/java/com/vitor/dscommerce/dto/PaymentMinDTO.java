package com.vitor.dscommerce.dto;

import com.vitor.dscommerce.entities.Payment;

import java.time.Instant;

public class PaymentMinDTO {

    private Long id;
    private Instant moment;

    public PaymentMinDTO(Long id, Instant moment) {
        this.id = id;
        this.moment = moment;
    }

    public PaymentMinDTO(Payment entity) {
        id = entity.getId();
        moment = entity.getMoment();
    }

    public Long getId() {
        return id;
    }

    public Instant getMoment() {
        return moment;
    }
}
