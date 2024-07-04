package com.vitor.dscommerce.services;

import com.vitor.dscommerce.dto.ProductDTO;
import com.vitor.dscommerce.dto.ProductMinDTO;
import com.vitor.dscommerce.entities.Product;
import com.vitor.dscommerce.projections.ProductMinProjection;
import com.vitor.dscommerce.projections.ProductProjection;
import com.vitor.dscommerce.repositories.ProductRepository;
import com.vitor.dscommerce.services.exceptions.DataBaseException;
import com.vitor.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/*Quando um componente vai poder ser injetado, ele precisa ser ser registrado,
 * no caso como o ProductService é um Service, registra como @Service
 * (tem a @Component que é mais genérica, mas usa as especificas)
 * A exceção é os repository, o motivo é explicado no Product repository*/
@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    /*@Transactional do Spring, não do Jakarta
     * Quando colocamos @Transactional sobre um método, estamos garantindo a resolução da transação no banco de dados, ou seja, o que aprendemos em banco de dados,
     * como fazer tudo ou não fazer nada, garantir integridade dos dados, etc.
     * Também estamos garantindo a resolução de todas pendências LAZY, ou seja, se eu buscasse primero um departamento e, depois buscasse todos os funcionários desse departamento,
     * ao buscar todos funcionários daria erro sem @Transactional, pois ao tentar buscar os funcionários, a sessão JPA já teria finalizado*/

    /*readOnly = true é para não dar lock de escrita no banco de dados, como não estou alterando nada, apenas lendo,
    a gente usa para ficar mais rápido, otimizando a transação*/
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
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

    @Transactional(readOnly = true)
    public Page<ProductMinDTO> searchAllWithoutDescription(Pageable pageable) {
        Page<ProductMinProjection> result = repository.searchAllWithoutDescription(pageable);
        return result.map(x -> new ProductMinDTO(x));
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
        try {
            Product entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource not found");
        }


    }

    /*propagation = Propagation.SUPPORTS é para só executar a transação se esse MÉTODO estiver no contexto de outra transação*/
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found");
        }
        try {
            repository.deleteById(id);
            /*
             * DataIntegrityViolationException
             *
             * A gente captura a exceção de quando tenta deletar um usuário que está sendo
             * referenciado em outras tabelas do banco de dados e lança a nossa exceção de
             * violação de integridade de dados.
             */
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Referential integrity failure");
        }
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> searchByName(String name, Pageable pageable) {
        Page<ProductProjection> result = repository.searchByName(name, pageable);
        return result.map(x -> new ProductDTO(x));
    }

    /*Como seria se eu usasse a JPQL ao invés do método acima
     * A principal diferença é que a JPQL já retorna um objeto Product gerenciado pela JPA, não precisando da projection*/
    @Transactional(readOnly = true)
    public Page<ProductDTO> searchByNameJPQL(String name, Pageable pageable) {
        Page<Product> result = repository.searchByNameJPQL(name, pageable);
        return result.map(x -> new ProductDTO(x));
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
    }
}
