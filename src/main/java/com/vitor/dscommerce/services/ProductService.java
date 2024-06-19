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
        Product product = repository.findById(id).get();
        return new ProductDTO(product);
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
}
