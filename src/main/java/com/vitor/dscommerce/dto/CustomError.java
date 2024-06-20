package com.vitor.dscommerce.dto;

import java.time.Instant;

/*
 * Porque em DTO? pois será usada como resposta e não faz parte da lógica de negócio para estar em outro lugar, como entities por exemplo

 * Esses atributos são retornados por padrão do Spring em um objeto de erro,
 * como a gente quer fazer um tratamento manual das exceções e retornar um
 * objeto similar ao que o Spring retorna, criamos essa classe
 *
 * A classe é usada para padronizar a estrutura das mensagens de erro enviadas nas respostas das APIs
 *
 * Por que no pacote Controllers? porque o objeto retornado pelo Spring de erro é
 * um objeto que já está na resposta da requisição, e quem trabalha com
 * requisição e mexe com isso é a camada de Controlladores (Controller)
 *
 * Os erros na faixa de 400 são erros de Cliente, ou seja, quem trata é o front-end, apenas estamos formatamos para responder corretamente oq aconteceu
 */
public class CustomError {

    private Instant timestamp;
    private Integer status;
    private String error;
    private String path;

    public CustomError(Instant timestamp, Integer status, String error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }
}
