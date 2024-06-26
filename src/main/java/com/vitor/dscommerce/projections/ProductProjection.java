package com.vitor.dscommerce.projections;

/*Uma forma de acessar o banco de dados somente nos campos que precisamos é definir uma projeção
 * Projeção é uma interface que vai ter somente aqueles campos que você precisa
 * É muito útil quando precisamos de somente certos campos, melhorando desempenho, simplificando código, segurança, etc.
 * Os métodos padrões retornam todos os campos, como o findById, oq algumas vezes pode não ser necessário*/
public interface ProductProjection {

    /*Colocamos o nome dos campos que quisermos nas formas dos métodos GET.
     * Aqui, como queremos todos os dados dos produtos (coluna id,name,description,price e imgurl no BD e atributo id,name,description,price e imgurl da classe)*/
    Long getId();

    String getName();

    String getDescription();

    Double getPrice();

    String getImgUrl();
}
