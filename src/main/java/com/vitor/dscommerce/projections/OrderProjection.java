package com.vitor.dscommerce.projections;

import com.vitor.dscommerce.entities.OrderStatus;

import java.time.Instant;

/*Exemplo de como seria uma projeção para um Order
* Projeções dentro de projeções são possíveis ao utilizar JPQL, como por exemplo
* os dados do produto como id, name, price, quantity seriam uma outra projection de Product*/
public interface OrderProjection {

    Long getId();
    Instant getMoment();
    OrderStatus getStatus();
    Long getClientId();
    String getClientName();
    Long getPaymentId();
    Instant getPaymentMoment();

    /*List<ProductProjection> getProducts() ou getItems() seria perfeitamente possível ao utilizar JPQL, assim como List<ClientMinProjection> getClient()...
    * utilizando uma projeção dentro de outra projeção
    * Como não é possível projeções dentro de projeções ao utilizar consultas NATIVAS, teria que fazer igual essa projection aqui, trazer tudo e ir instanciando cada cliente e produto
    * e depois utilizando um Map assim como foi feito no dscatalog na classe ProductService no método searchProductsWithCategories
    * Olhar também o repository, no qual teve o DISTINCT para retornar a contagem certa, pois aqui também daria problema, pois um pedido tem mais de 1 item (produto), então
    * contaria várias vezes um mesmo pedido, mas ai utilizariamos o DISTINCT para pegar somente os pedidos diferentes
    *
    * https://stackoverflow.com/questions/48995744/java-spring-projection-inside-projection*/

    Long getProductId();
    String getProductName();
    Double getProductPrice();
    Integer getProductQuantity();

    /*total() seria um método do próprio OrderDTO, calculado nele
    * Teria que ter OrderItemDTO recebendo os dados
    * subTotal() calculado nesse OrderItemDTO*/
}
