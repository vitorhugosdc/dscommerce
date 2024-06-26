package com.vitor.dscommerce.repositories;

import com.vitor.dscommerce.entities.Product;
import com.vitor.dscommerce.projections.ProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/*Só isso aqui já é o suficiente pra instanciar um objeto repository com várias operações para trabalhar com o Produto por padrão
 * Não é necessário implementar essa interface, porque o Spring Data JPA já possui uma implementação para essa interface, por isso o extends*/
/*Product é a minha entidade e Long é o tipo do id da entidade*/
/*Como o ProductRepository herda do JpaRepository,
 * não precisa colocar @Repository pra registrar ele, pois ele já é registrado na classe pai*/
public interface ProductRepository extends JpaRepository<Product, Long> {

    /*nativeQuery = true é pra ser SQL raíz a consulta
     * :name é para usar a String name recebida :<nome_argumento>

     * Quando fazemos uma consulta no repository retornando a interface, o próprio Spring Data JPA cria um objeto CONCRETO com a estrutura da interface,
     * então poderiamos chamar o .getName() do objeto
     *
     * IMPORTANTE: quando a consulta é nativa, o Spring Data JPA não sabe como mepear os resultados da consulta nativa diretamente para o DTO.
     * A criação de objetos DTO é só suportada em JPQL*/

    /*Quando se faz um método customizado, recebendo como ÚLTIMO argumento um Pageable, ele retorna um resultado paginado integrado com o framework (se for consulta JPQL)
     * Por que do countQuery? Pois como é uma consulta nativa, é importante definir ela (com o mesmo código da consulta em value para que o JPA saiba como calcular o número total de itens por paginação
     * Ela conta o número total de registros que satisfazem os critérios da consulta sem aplicar a limitação de
     * Isso permite ao Spring Data JPA calcular o número total de páginas e os índices de paginação adequados.*/
    @Query(nativeQuery = true, value = "SELECT * FROM tb_product " +
            "WHERE UPPER(tb_product.name) " +
            "LIKE CONCAT('%', UPPER(:name), '%')",
            countQuery = "SELECT count(*) FROM tb_product " +
                    "WHERE UPPER(tb_product.name) LIKE CONCAT('%', UPPER(:name), '%')")
    public Page<ProductProjection> searchByName(String name, Pageable pageable);

    /*Como seria se eu usasse a JPQL ao invés do método acima*/
    /*A consulta JPQL não precisa da projection, podemos retornar o DTO direto
     *
     * Pra fazer projeção, ao invés de só colocar SELECT obj, tem que colocar o caminho completo do DTO depois de um new*/
    @Query("SELECT obj FROM Product obj " +
            "WHERE UPPER(obj.name) LIKE CONCAT('%', UPPER(:name), '%')")
    Page<Product> searchByNameJPQL(String name, Pageable pageable);
}
