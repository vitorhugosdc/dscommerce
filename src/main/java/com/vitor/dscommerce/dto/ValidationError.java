package com.vitor.dscommerce.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/*Vai ter todos os campos padrões de resposta de CustomError e mais uma lista de erros de validação
 * Classe usada exclusivamente para retornar uma resposta customizada quando ocorrerem erros de validação*/
public class ValidationError extends CustomError {

    private List<FieldMessage> errors = new ArrayList<>();

    public ValidationError(Instant timestamp, Integer status, String error, String path) {
        super(timestamp, status, error, path);
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void addError(String fieldName, String message) {
        /*Se um campo tiver mais de um erro, seria adicionado na lista, mas queremos que seja tratado 1 erro de cada vez no front-end,
         * então, removemos os erros se já estiver algum para o mesmo CAMPO*/
        errors.removeIf(x -> x.getFieldName().equals(fieldName));
        errors.add(new FieldMessage(fieldName, message));
    }
}
