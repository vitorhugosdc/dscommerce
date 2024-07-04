package com.vitor.dscommerce.dto;

import com.vitor.dscommerce.entities.Product;
import com.vitor.dscommerce.projections.ProductMinProjection;

public class ProductMinDTO {

    /*Não precisa das validações de ProductDTO pois não estamos recebendo ele no insert, estamos enviando como resposta (tá saindo, não entrando)*/
    private Long id;
    private String name;
    private Double price;
    private String imgUrl;

    public ProductMinDTO(Long id, String name, String description, Double price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public ProductMinDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.price = entity.getPrice();
        this.imgUrl = entity.getImgUrl();
    }

    public ProductMinDTO(ProductMinProjection projection) {
        id = projection.getId();
        name = projection.getName();
        price = projection.getPrice();
        imgUrl = projection.getImgUrl();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
