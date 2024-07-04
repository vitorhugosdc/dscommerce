package com.vitor.dscommerce.repositories;

import com.vitor.dscommerce.entities.Product;
import com.vitor.dscommerce.projections.ProductMinProjection;
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
    Page<ProductProjection> searchByName(String name, Pageable pageable);

    /*Como seria se eu usasse a JPQL ao invés do método acima*/
    /*A consulta JPQL não precisa da projection, podemos retornar o DTO direto
     *
     * Pra fazer projeção, ao invés de só colocar SELECT obj, tem que colocar o caminho completo do DTO depois de um new*/
    @Query("SELECT obj FROM Product obj " +
            "WHERE UPPER(obj.name) LIKE CONCAT('%', UPPER(:name), '%')")
    Page<Product> searchByNameJPQL(String name, Pageable pageable);

    @Query(nativeQuery = true,
    value = "SELECT tb_product.id, tb_product.name, tb_product.price, tb_product.img_url AS imgUrl " +
            "FROM tb_product",
    countQuery = "SELECT count(*) FROM tb_product")
    Page<ProductMinProjection> searchAllWithoutDescription(Pageable pageable);

    /*ABAIXO SÃO COMENTÁRIOS DA AULA LAZY EXPLICANDO MAIS SOBRE SPRING DATA JPA E SEU FUNCIONAMENTO PARA 2 CLASSES QUE NÃO ESTÃO NO PROJETO ATUAL, MAS É BOA EXPLICAÇÃO
    *

     */
    /*A cláusula JOIN FETCH não funciona em buscas paginadas do Sring*/
    /*Na JPQL a gente tem que dar apelido aos OBJETOS (obj)
     * Ao invés do nome da tabela, a gente informa o nome da classe (Employee)
     * Ao invés da classe ou tabela da associação, colocamos o nome do atributo (obj.department)
     *
     * Estamos na consulta buscando todos Employee e já buscando ao mesmo tempo o departamento dele, tudo na mesma consulta
     *
     * Por que foi necessária essa consulta sendo que Department é para-um e quando se consulta ele por ser 1 já vem direto?
     * Pois quando fazemos uma consulta buscando uma coleção de objetos, mesmo que eles tenham relacionamento para-um, a busca desses objetos vai ser lazy
     * então, ele busca todos os funcionários (pois viu que era coleção), ai quando viu que precisava dos departamentos, ele foi buscando 1 por 1
     * Ele não faz buscas repetidas, então por ex, como temos 3 departamentos, ele além da consulta de todos funcionários, faz +3 consultas, uma pra cada departamento
     * ele não busca 14 vezes (número de funcionários) pois ele tem CACHE, então se ele já buscou 1 departamento, não vai buscar ele de novo, só um novo
     * ele associa o mesmo departamento a todos os funcionários sem precisar de novas buscas (na mesma sesão JPA, oq significa que é do inicio ao fim da requisição,
     * spring.jpa.open-in-view=false é só até retornar do serviço, então ela dura na conversa entre service/repository) e é finalizado antes de chegar no controller*/
    //@Query("SELECT obj FROM Employee obj JOIN FETCH obj.department")
    //List<Employee> findEmployeesWithDepartments();

    /*Isso é um Query method, ele monta a consulta somente com o nome do método
     *
     * Mais opções: https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
     *
     * Ideal é usar JPQL ou SQL nativo mesmo, pois essa simples consulta abaixo acessa 4 vezes o banco de dados quando poderia ser só 1 vez*/
    //List<Employee> findByNameContainingIgnoreCase(String name);*/
}
