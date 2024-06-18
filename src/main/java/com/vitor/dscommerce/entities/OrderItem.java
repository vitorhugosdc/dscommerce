package com.vitor.dscommerce.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "tb_order_item")
public class OrderItem {

    /*
     * Por não ser um Id Long como as outras classes e por ser composto, a gente
     * coloca a annotation @EmbeddedId (a classe OrderItemPK tem que ter a
     * annotation @Embeddable)
     */
    @EmbeddedId
    /*
     * Sempre que utilizar uma classe auxiliar como Id composto, tem que instânciar
     * ela, se não ela vai valer nullo e pode dar erro
     */
    private OrderItemPK id = new OrderItemPK();

    private Integer quantity;
    private Double price;

    public OrderItem() {
    }

    /* O construtor também recebe um Order e um Product */
    public OrderItem(Order order, Product product, Integer quantity, Double price) {
        /*
         * A gente seta o Order e Product nesse OrderItem com os sets da classe auxiliar
         * criada
         */
        id.setOrder(order);
        id.setProduct(product);
        this.quantity = quantity;
        this.price = price;
    }

    /*
     * Criamos os getters e setters manualmente para o Order e Product associados a
     * esse OrderItem, para não retornar o objeto composto OrderItemPK, retornaremos
     * cada um deles certinho
     */
    public Order getOrder() {
        return id.getOrder();
    }

    /* Recebe um pedido e atribuí ele lá dentro da chave primária correspondente */
    public void setOrder(Order order) {
        id.setOrder(order);
    }

    public Product getProduct() {
        return id.getProduct();
    }

    /* Recebe um produto e atribuí ele lá dentro da chave primária correspondente */
    public void setProduct(Product product) {
        id.setProduct(product);
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
