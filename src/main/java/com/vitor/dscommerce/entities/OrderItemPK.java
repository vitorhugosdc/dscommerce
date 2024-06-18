package com.vitor.dscommerce.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/*No paradigma Orientado a Objetos, eu não tenho o conceito de chave primária composta

O atributo identificador do objeto é 1 só, então tem que criar uma classe auxiliar para representar o par Produto e Pedido (Product e Order). 

O par Produto (Product) e Pedido (Order) é que vai identificar o Item de Pedido (OrderItem)

O objeto OrderItem não tem uma chave primária própria como o @Id autoincrementável das outras classes*/

/*Como essa classe é uma classe auxiliar de chave primária composta, a gente coloca a annotation @Embeddable*/
@Embeddable
public class OrderItemPK {

    /*A tabela do meio (essa aqui) tem dois muitos pra um, pois tem a chave estrangeira de cada tabela*/
    /*
     * Um Order pode ter muitos OrderItem e, cada OrderItem está associado a apenas
     * um Order
     *
     * Um pedido pode ter muitos itens de pedido e, cada item de pedido está
     * associado a apenas um pedido
     */
    @ManyToOne
    /*
     * order_id vai ser o nome da chave estrangeira na tabela OrderItem que armazena
     * o id da Order que está associado a esse OrderItem
     */
    @JoinColumn(name = "order_id")
    private Order order;

    /*
     * Um Product pode estar associado a muitos OrderItem e, cada OrderItem está
     * associado a apenas um Product
     *
     * Um produto pode estar associado a muitos itens de pedido e, cada item de pedido está
     * associado a apenas um produto
     */
    @ManyToOne
    /*
     * product_id vai ser o nome da chave estrangeira na tabela OrderItem que
     * armazena o id do Product que está associado a esse OrderItem
     */
    @JoinColumn(name = "product_id")
    private Product product;

    public OrderItemPK() {
    }

    public OrderItemPK(Order order, Product product) {
        this.order = order;
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
