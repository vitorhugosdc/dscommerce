package com.vitor.dscommerce.services.exceptions;

/*Exceção personalizada para a nossa camada de serviço,
 * a nossa camada de serviço tem que ser capaz de lançar exceções DELA, e não exceções diversas
 * Como é na camada de serviço que estão as regras de negócio, colocamos as exceções personalizadas nela*/
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String msg) {
        super(msg);
    }
}
