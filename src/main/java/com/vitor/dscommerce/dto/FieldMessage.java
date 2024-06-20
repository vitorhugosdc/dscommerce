package com.vitor.dscommerce.dto;

/*Classe para representar a resposta customizada da nossa validação que está definida lá no DTO*/
public class FieldMessage {

    /*nome do campo que deu o erro de validação*/
    private String fieldName;
    /*mensagem do campo que deu o erro de validação*/
    private String message;

    public FieldMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }
}
