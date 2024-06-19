package com.vitor.dscommerce.controllers;

import com.vitor.dscommerce.dto.ProductDTO;
import com.vitor.dscommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*@RestController - Essa anotação indica que a classe é um controlador (Controller) que lida com requisições web.
 * Ela combina a funcionalidade de @Controller e @ResponseBody, ou seja,
 * ele retorna diretamente o objeto e Spring o converte para JSON automaticamente.*/
@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired/**/
    private ProductService service;

    /* Isso indica que dentro da requisição vai ser aceito um id na URL, então o caminho seria /products/id */
    @GetMapping(value = "/{id}")
    /* @PathVariable pro Spring identificar que é o id recebido como parâmetro */
    public ProductDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }
}
