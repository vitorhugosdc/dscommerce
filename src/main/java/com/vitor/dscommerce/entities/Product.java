package com.vitor.dscommerce.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    /*Essa descrição do produto pode ser um texto muito grande, então para configurar para a JPA que ao criar a coluna do banco de dados
     * vai ser um texto grande, e não só um VARCHAR que vai até 255 caractéres, colocamos a anotação abaixo para mapear a coluna que será um TEXT no banco de dados */
    @Column(columnDefinition = "TEXT")
    private String description;
    private Double price;
    private String imgUrl;

    /*
     * Quando temos associação MUITOS-PARA-MUITOS (N..N), colocamos a
     * annotation @ManyToMany e, em ALGUMA DAS entidades que possuem o MUITOS (N)
     * colocamos a anotação abaixo dele, o @JoinTable, o que ele faz?.
     *
     * @JoinTable cria uma tabela extra, a chamada tabela de associações, que é como
     * solucionamos em banco de dados problemas de muitos-para-muitos (N..N),
     * portanto, é criado uma tabela a mais que armazena somente as chaves
     * product_id e category_id.
     *
     * @JoinTable(name = "tb_product_category",... define o nome dessa tabela extra
     * como "tb_product_category".
     *
     * joinColumns = @JoinColumn(name = "product_id")... define o nome da chave
     * estrangeira referente a tabela de produtos (produtos vem primeiro pois
     * estamos fazendo isso na classe Product, não Category) lá na tabela de
     * associação.
     *
     * inverseJoinColumns = @JoinColumn(name = "category_id") define o nome da chave
     * estrangeira da outra entidade lá na tabela de associação. Como estou na
     * entidade Product, a "outra entidade" é a Category.
     *
     * A tabela de associação armazena as chaves estrangeiras das duas tabelas
     * associadas, Product e Category
     */
    /*
     * A entidade que define a @JoinTable é aquela que tem o controle sobre a
     * definição das colunas da tabela de junção, ou seja, ela é a "dona" da relação
     *
     * A manipulação dos dados é feita do ponto de vista da entidade que define a
     * relação. Por exemplo, se a @JoinTable estiver na entidade Product, a gente
     * normalmente vai adicionar ou remover categorias a partir de um produto. Se
     * estiver na entidade Category, você adicionaria ou removeria produtos a partir
     * de uma categoria.
     */
    /* Muitos-para-muitos comum (sem atributos no meio) a gente tem uma tabela extra contendo os IDs das duas entidades (Category e Product)
    Esses 2 IDs não podem se repetir nessa terceira tabela, POR ISSO, para indicar isso ao JPA, iremos usar o tipo Set e não o tipo List*/
    @ManyToMany
    @JoinTable(name = "tb_product_category", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    public Product() {
    }

    public Product(Long id, String name, String description, Double price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Category> getCategories() {
        return categories;
    }
}
