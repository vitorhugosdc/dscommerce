package com.vitor.dscommerce.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*Sempre que for criar uma entidade em Java (Spring Boot pelo menos) tem que criar nesta exata ordem

Basic entity checklist:

- Basic attributes

- Associations (instantiate collections)

- Constructors

- Getters & Setters (collections: only get)

- hashCode & equals

- Serializable*/

/*
 * @Entity pra identificar que essa é uma entidade de domínio que vai
 * corresponder a uma tabela no banco de dados
 *
 * Por padrão, O JPA cria uma tabela com o mesmo nome da classe e, colunas com
 * os mesmos nomes dos atributos
 */
@Entity
/*
 * Aqui a gente define o nome da tabela dessa entidade User, se não tiver a
 * linha abaixo o JPA usa o nome da própria classe, User, porém, a palavra User
 * é uma palavra reservada no banco de dados H2, por isso foi posto tb_user
 */
@Table(name = "tb_user")
public class User {

    /* Informando ao JPA qual a chave primária da tabela no banco de dados */
    @Id
    /* Essa daqui é pra dizer que ela é autoincrementável no banco de dados */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String password;
    /*Por enquanto não implementado, será implementado quando for implementado a segurança*/
    /*private String[] roles;*/

    /* 1 cliente para muitos pedidos */
    /*
     * A associação OneToMany aparentemente é opcional, só é usado caso eu queira
     * acessar um objeto do tipo usuário e acessar automaticamente os pedidos feitos
     * por esse usuário
     */
    /*
     * mappedBy = "client" é o o EXATO nome do ATRIBUTO lá na classe Order que
     * refere-se ao usuário (cliente), ou seja: Esse muito-para-um, lá do outro lado
     * (Order) ele está mapeado por (mappedBy) "client" (atributo client da classe
     * order)
     *
     * A gente coloca o @JoinColumn no lado "One/Um", ou seja, no Order, porque o
     * pedido "One" tem só 1 usuário, então salva o id do usuário lá na tabela de
     * pedidos (Order)
     */
    @OneToMany(mappedBy = "client")
    private List<Order> orders = new ArrayList<>();

    public User() {
    }

    public User(Long id, String name, String email, String phone, LocalDate birthDate, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Order> getOrders() {
        return orders;
    }
}