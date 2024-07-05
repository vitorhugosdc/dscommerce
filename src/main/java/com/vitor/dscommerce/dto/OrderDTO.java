package com.vitor.dscommerce.dto;

import com.vitor.dscommerce.entities.Order;
import com.vitor.dscommerce.entities.OrderItem;
import com.vitor.dscommerce.entities.OrderStatus;
import jakarta.validation.constraints.NotEmpty;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {

    private Long id;
    private Instant moment;
    private OrderStatus status;
    private UserMinDTO client;
    private PaymentMinDTO payment;
    @NotEmpty(message = "Order must have at least one item")
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
        /*Isso aqui tá gerando uma nova consulta na JPA para buscar os itens de pedido do pedido
         * Poderia ser melhorado utilizando um OrderProjection.
         * Um exemplo de como seria está na classe OrderProjection, criada apenas para demonstrar
         * Mas o getSubTotal() e getTotal() estaria no OrderDTO mesmo (aqui)*/
        /*Por que UserMinDTO e PaymentMinDTO não triggam uma nova consulta? pois como estamos buscando 1 (um) pedido, é aquele padrão,
         * se consulta buscando 1 só, ele já busca EAGER os "para-um", ou seja, como Order tem só 1 cliente e só 1 pagamento, ele já busca direto
         * Agora, o "para-muitos" dos Itens de pedido, ele só busca quando for requisitado
         * Agora, se fosse uma lista de Orders, aí, sim, ele buscaria todos pedidos e só depois todos clientes e pagamentos de cada pedido, gerando
         * uma "chuva" de buscas no banco de dados, aí seria ideal usar um Page com Projections (como no repositório de Product)
         * mais explicação sobre pode ser vista no ProductService e no ProductRepository
         * Inclusive, isso aqui daria exceção se não tivesse o @Transacional no findById com o spring.jpa.open-in-view=false*/
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

    /*Retorna no JSON também por ter o get no nome*/
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
