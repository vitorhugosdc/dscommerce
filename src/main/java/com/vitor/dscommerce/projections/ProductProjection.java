package com.vitor.dscommerce.projections;

/*Uma forma de acessar o banco de dados somente nos campos que precisamos é definir uma projeção
 * Projeção é uma interface que vai ter somente aqueles campos que você precisa
 * É muito útil quando precisamos de somente certos campos, melhorando desempenho, simplificando código, segurança, etc.
 * Os métodos padrões retornam todos os campos, como o findById, oq algumas vezes pode não ser necessário*/
public interface ProductProjection {

    /*Colocamos o nome dos campos que quisermos nas formas dos métodos GET.
     * Aqui, como queremos todos os dados dos produtos (coluna id,name,description,price e imgurl no BD e atributo id,name,description,price e imgurl da classe)
     *
     * SELECT * FROM tb_product significa que temos que colocar todos atributos, lá no banco vai ser
     * tb_product.id, tb_product.name, tb_product.description... por isso o get tem que corresponder, ou seja
     * tb_product.price tem que por getPrice(), pois é o nome do atributo no banco de dados
     * Agora, se quisermos personalizar, usa o alias, ou seja, tb_product.price AS product_price por exemplo, ai usaria getProductPrice()*/
    Long getId();

    String getName();

    String getDescription();

    Double getPrice();

    String getImgUrl();
}
