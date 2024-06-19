package com.vitor.dscommerce.controllers;

import com.vitor.dscommerce.dto.ProductDTO;
import com.vitor.dscommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*@RestController - Essa anotação indica que a classe é um controlador (Controller) que lida com requisições web.
 * Ela combina a funcionalidade de @Controller e @ResponseBody, ou seja,
 * ele retorna diretamente o objeto e Spring o converte para JSON automaticamente.*/
@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductService service;

    /* Isso indica que dentro da requisição vai ser aceito um id na URL, então o caminho seria /products/id */
    @GetMapping(value = "/{id}")
    /* @PathVariable pro Spring identificar que é o id recebido como parâmetro */
    public ProductDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    /*Pageable do Spring.data.domain*/
    /*O método retorna uma página de produtos dto Page<ProductDTO>, que é basicamente uma lista de produtos que representa uma fração dos dados completos de produtos*/
    /*Dentro da Página a gente tem uma lista de produtos que representa essa fração*/
    /*Por padrão, se não for informado nada no Pageable, vão ser 20 elementos por página*/
    /*Para personalizar o número de elementos por página, utiliza um QueueParam direto na requisição, ex:
     * /products?size=12 - QueueParam e estabelece 12 elementos por página
     * /products?size=12&page=1 - QueueParam 12 por página e retornando a segunda página (a contagem começa em 0)
     * /products?size=12&page=1&sort=name - QueueParam 12 por página e retornando a segunda página (a contagem começa em 0) e ordenada por nome (pode ser qualquer atributo da classe)
     * /products?size=12&page=1&sort=name,desc - QueueParam 12 por página e retornando a segunda página (a contagem começa em 0) e ordenada por nome decrescente*/
    @GetMapping
    public Page<ProductDTO> findAll(Pageable pageable) {
        return service.findAll(pageable);
    }

    /*@RequestBody porque o meu endpoint vai receber um OBJETO do tipo ProductDTO no body*/
    @PostMapping
    public ProductDTO insert(@RequestBody ProductDTO productDto) {
        return service.insert(productDto);
    }
}
