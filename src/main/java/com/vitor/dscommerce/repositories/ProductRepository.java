package com.vitor.dscommerce.repositories;

import com.vitor.dscommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
/*Só isso aqui já é o suficiente pra instanciar um objeto repository com várias operações para trabalhar com o Produto por padrão
 * Não é necessário implementar essa interface, porque o Spring Data JPA já possui uma implementação para essa interface, por isso o extends*/
/*Product é a minha entidade e Long é o tipo do id da entidade*/
/*Como o ProductRepository herda do JpaRepository,
 * não precisa colocar @Repository pra registrar ele, pois ele já é registrado na classe pai*/
public interface ProductRepository extends JpaRepository<Product, Long> {
}
