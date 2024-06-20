package com.vitor.dscommerce.controllers;

import com.vitor.dscommerce.dto.ProductDTO;
import com.vitor.dscommerce.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/*@RestController - Essa anotação indica que a classe é um controlador (Controller) que lida com requisições web.
 * Ela combina a funcionalidade de @Controller e @ResponseBody, ou seja,
 * ele retorna diretamente o objeto e Spring o converte para JSON automaticamente.*/
@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductService service;

    /* value = "/{id} indica que dentro da requisição vai ser aceito um id na URL, então o caminho seria /products/id */
    /* @PathVariable pro Spring identificar que é o id recebido como parâmetro */
    /* ResponseEntity é um tipo específico do spring para retornar respostas para
     * requisições web. Ele é um Generics, e o tipo da resposta dele está entre <>,
     * utilizar ele é uma boa prática
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        ProductDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
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
    public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
        Page<ProductDTO> dto = service.findAll(pageable);
        return ResponseEntity.ok(dto);
    }

    /*@RequestBody porque o meu endpoint vai receber um OBJETO do tipo ProductDTO no body*/
    /*@Valid é pro controllador passar pela verificação de validação dos dados que implementamos no DTO*/
    @PostMapping
    public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto) {
        dto = service.insert(dto);
        /*
         * Por que disso? porque quando é inserido um recurso, é mais adequado retornar
         * o código de resposta 201, e não 200 padrão, pois o 201 é o código específico
         * HTTP que significa que foi criado um novo recurso, então, retornamos um
         * ResponseEntity.created(uri), só que o created espera um objeto do tipo URI,
         * por quê? Porque o padrão HTTP, quando vamos retornar um 201, é esperado que a
         * resposta contenha um CABEÇALHO chamado Location, contendo o endereço do novo
         * recurso INSERIDO
         *
         * o path recebe um padrão para montar a URL, no caso, o recurso inserido vai
         * ter o caminho /products (padrão) /id novo inserido, ou seja: /products/id
         *
         * o método buildAndExpand espera que eu informe o id inserido, no caso, o id do
         * novo recurso inserido vai estar em obj, por isso obj.getId()
         */
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    /* @Valid vai no corpo, não em Id*/
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        dto = service.update(id, dto);
        return ResponseEntity.ok(dto);
    }

    /*
     * Void porque a resposta da requisição não vai retornar nenhum corpo, será
     * apenas deletado o usuário e pronto
     *
     * .noContent() retorna resposta vazia com o código de resposta vazia (204) sem
     * conteúdo 204, HTTP
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
