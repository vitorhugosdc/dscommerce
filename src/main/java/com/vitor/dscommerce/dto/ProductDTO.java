package com.vitor.dscommerce.dto;

import com.vitor.dscommerce.entities.Category;
import com.vitor.dscommerce.entities.Product;
import com.vitor.dscommerce.projections.ProductProjection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

/*Não deve ter nada de JPA no DTO*/
/*A classe DTO é uma cópia da entidade original, nesse caso, de Product, sem Setters e equals/hashCode*/
/*VALIDAÇÕES são feitas na DTO, pois é o DTO que vai tráfegar via web e corresponde ao JSON que está sendo enviado/recebido via web
O framework serializar ou desserializar o JSON e verifica se os dados deles estão corretos*/
public class ProductDTO {

    private Long id;
    /*Não aceita nome nulo, vazio ou só com espaços em branco*/
    @NotBlank(message = "Required field")
    @Size(min = 3, max = 80, message = "Name must be between 3 and 80 characters")
    private String name;
    @NotBlank(message = "Required field")
    @Size(min = 10, message = "Description must be at least 10 characters")
    private String description;
    @Positive(message = "Price must be positive")
    private Double price;
    private String imgUrl;

    /*DTO só se associa com DTO e não com entidade, por isso usamos um DTO de Category e não a entidade Category
     * Também podemos receber como lista ao invés do Set*/
    @NotEmpty(message = "Product must have at least one category")
    private List<CategoryDTO> categories = new ArrayList<>();

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
        /*Entity tem uma Category e não CategoryDTO, temos então que converter para o DTO
         * Isso aqui tá gerando uma nova consulta no JPA, então daria para fazer um método searchById já com as categorias para evitar isso*/
        for (Category cat : entity.getCategories()) {
            categories.add(new CategoryDTO(cat));
        }
    }

    /*Construtor recebendo a projeção da consulta customizada*/
    public ProductDTO(ProductProjection projection) {
        id = projection.getId();
        name = projection.getName();
        description = projection.getDescription();
        price = projection.getPrice();
        imgUrl = projection.getImgUrl();
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

    public List<CategoryDTO> getCategories() {
        return categories;
    }
}
