package com.vitor.dscommerce.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
/*
 * A palavra Order é uma palavra reservada no banco de dados H2, por isso foi
 * posto tb_order
 */
@Table(name = "tb_order")
public class Order {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*Colocamos isso aqui para configurar esse campo moment para que seja salvo no banco de dados como um instante no tempo, no padrão UTC*/
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant moment;
    private OrderStatus status;

    /* Muitos pedidos para 1 cliente */
    @ManyToOne
    /*
     * Quando a relação é para-um, a gente geralmente já implementa junto com a classe.
     *
     * client_id vai ser o nome da chave estrangeira na tabela Order que armazena o
     * id do Usuário que esta associado a esse pedido.
     */
    @JoinColumn(name = "client_id")
    private User client;

    /*
     * Order é a classe dominante
     *
     * Colocamos a anotação abaixo nas classes dominantes
     *
     * No caso do 1 para 1, estamos mapeando as duas entidades para ter o mesmo Id,
     * ou seja, se o pedido tiver o código 5, o pagamento desse pedido também vai
     * ter código 5 (pagamento é a classe dependente)
     *
     * order é o nome da variável Order lá na classe Payment
     */
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    public Order() {
    }

    public Order(Long id, Instant moment, OrderStatus status, User client) {
        this.id = id;
        this.moment = moment;
        this.status = status;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }
}
