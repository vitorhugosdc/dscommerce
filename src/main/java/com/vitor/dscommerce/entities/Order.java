package com.vitor.dscommerce.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    /*
     * Um pedido para vários itens de pedido
     *
     * Por que id.order? Porque no OrderItem eu tenho o id, e esse id por sua vez é
     * quem possui uma referência ao pedido e o possui. Ou seja, o order que possui
     * o mapeamento tá lá na OrderItemPK. O OrderItemPK é um atributo do OrderItem,
     * então acessamos id.order.
     *
     * Basicamente, eu preciso referenciar o nome do atributo que referência Order lá na classe OrderItem,
     * mas a classe OrderItem não tem um Order explicito, por isso a gente acessa o id e referencia Order lá
     *
     * Set pois o pedido não pode ter items de pedido repetidos (o item de pedido
     * possui sua quantidade, então se for mais de 1, por exemplo, isso é
     * transmitido pela quantidade dele, não por adicionar mais um igual na lista de
     * items do pedido, então, se tem mais de um item de pedido igual, ele é
     * refletido no atributo quantity da classe OrderItem e, não repetição dele nos
     * itens de pedido.
     */
    @OneToMany(mappedBy = "id.order")
    private Set<OrderItem> items = new HashSet<>();

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

    public Payment getPayment() {
        return payment;
    }

    /*Retorna os items de pedido de determinado pedido*/
    public Set<OrderItem> getItems() {
        return items;
    }

    /*Método para acessarmos todos os produtos do pedido (tá no diagrama)*/
    public List<Product> getProducts() {
        /*map<entrada: OrderItem,saida: Product>*/
        return items.stream().map(OrderItem::getProduct).toList();
        /*return items.stream().map(x -> x.getProduct()).toList();*/
        /*return items.stream().map(x -> x.getProduct()).collect(Collectors.toList();*/

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
