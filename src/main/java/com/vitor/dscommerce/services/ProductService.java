package com.vitor.dscommerce.services;

import com.vitor.dscommerce.dto.ProductDTO;
import com.vitor.dscommerce.entities.Product;
import com.vitor.dscommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*Quando um componente vai poder ser injetado, ele precisa ser ser registrado,
 * no caso como o ProductService é um Service, registra como @Service
 * (tem a @Component que é mais genérica, mas usa as especificas)
 * A exceção é os repository, o motivo é explicado no Product repository*/
@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    /*@Transactional do Spring, não do Jakarta*/
    /*readOnly = true é para não dar lock no banco de dados, como não estou alterando nada, apenas lendo,
    a gente usa para ficar mais rápido, otimizando a transação*/
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product entity = repository.findById(id).get();
        return new ProductDTO(entity);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        /*Page do Spring.data.domain*/
        /*O findAll já tem um método que recebe um Pageable no repository e retorna um Page*/
        Page<Product> products = repository.findAll(pageable);
        /*Converte todos os produtos de Page para ProductDTO*/
        /*.map <entrada: Product, saida: ProductDTO>*/
        /*.map direto sem o stream, pois o Page já é um stream*/
        return products.map(x -> new ProductDTO(x));
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        /*O repository monitora Product, não ProductDTO, então para salvar é necessário converter o DTO recebido para Product,
         * Por isso utilizamos a função auxiliar copyDtoToEntity que recebe um ProductDTO e converte para Product
         * O repository.save retorna um Product (o que acabou de ser inserido/salvo), mas como vamos retornar um ProductDTO, convertemos novamente de Product para ProductDTO utilizando o construtor de ProductDTO (new ProductDTO(entity))         * Basicamente: recebemos um ProductDTO, convertemos para Product, salvamos, recebemos o Product salvo e convertemos para ProductDTO para ser retornadoo*/
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }


    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        /*
         * o getReferenceById instância um usuário, mas ele NÃO VAI no banco de dados
         * ainda, ele vai deixar um objeto monitorado. É melhor que usar o findById,
         * pois o findById vai no banco de dados buscar o objeto usuário. O
         * getReferenceById ele PREPARA o objeto monitorado pela JPA para ser mexido e DEPOIS
         * realizar uma operação no banco de dados, sendo bem melhor
         */
        Product entity = repository.getReferenceById(id);

        copyDtoToEntity(dto, entity);

        entity = repository.save(entity);

        return new ProductDTO(entity);

    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
    }
}
