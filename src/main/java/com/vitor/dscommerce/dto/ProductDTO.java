package com.vitor.dscommerce.dto;

import com.vitor.dscommerce.entities.Product;

/*Não deve ter nada de JPA no DTO*/
/*A classe DTO é uma cópia da entidade original, nesse caso, de Product, sem Setters e equals/hashCode*/
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imgUrl;

    public ProductDTO(Long id, String name, String description, Double price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public ProductDTO(Product entity) {
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        price = entity.getPrice();
        imgUrl = entity.getImgUrl();
    }

    /*DTOs não precisam nem ter SETs, pois não vou querer alterar esses dados, eles vão ser copiados da ENTIDADE e pronto
    * inclusive, é por isso que não há construtor vazio na classe*/
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
