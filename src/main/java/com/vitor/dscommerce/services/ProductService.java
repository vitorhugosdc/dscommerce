package com.vitor.dscommerce.services;

import com.vitor.dscommerce.dto.ProductDTO;
import com.vitor.dscommerce.entities.Product;
import com.vitor.dscommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        Product product = repository.findById(id).get();
        return new ProductDTO(product);
    }
}
