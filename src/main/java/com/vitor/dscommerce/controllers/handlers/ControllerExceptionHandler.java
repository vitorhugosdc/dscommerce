package com.vitor.dscommerce.controllers.handlers;

import com.vitor.dscommerce.dto.CustomError;
import com.vitor.dscommerce.dto.ValidationError;
import com.vitor.dscommerce.services.exceptions.DataBaseException;
import com.vitor.dscommerce.services.exceptions.ForbiddenException;
import com.vitor.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

/*É nessa classe que iremos dar o tratamento manual para nossos erros da camada controllers
 * Mesmo que a exceção seja acionada na camada de serviços, ela é propagada para a camada de controladores, por isso tratamos no controllador, pois ele que recebe e trata esse erro com essa classe aqui*/
/*@ControllerAdvice é para interceptar as exceções que acontecerem, para que esse objeto possa executar um possível tratamento
 *
 * Essa classe é responsável por capturar e tratar exceções*/
@ControllerAdvice
public class ControllerExceptionHandler {

    /*
     * @ExceptionHandler(ResourceNotFoundException.class) é para anotar que esse
     * método abaixo vai interceptar qualquer exceção desse tipo
     * ResourceNotFoundException que for lançada, ou seja, quando o serviço
     * ProductService por exemplo lançar essa exceção, será capturada e tratada aqui
     *
     * Basicamente: o ProductService por exemplo vai lançar uma
     * ResourceNotFoundException (a minha personalizada), como no método findById por exemplo, ai, o método
     * resourceNotFound abaixo vai interceptar qualquer exceção daquela classe,
     * então vai cair nele, ai, a gente instância um erro padrão, só que manualmente
     * e com nossos dados mais detalhados, tudo é instanciado no resourceNotFound
     * abaixo, só a mensagem do atributo mensagem da classe StandardError que vai
     * ser obtida através da ResourceNotFoundException original
     */
    /*
     * Retorno é um ResponseEntity com o objeto da classe Standard Error que criamos
     *
     * Ele recebe uma exceção do nosso tipo personalizado ResourceNotFoundException
     * e um HttpServletRequest, que representa a solicitação HTTP que foi feita ao
     * servidor
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        CustomError error = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<CustomError> database(DataBaseException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomError error = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<CustomError> forbbiden(ForbiddenException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        CustomError error = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    /*MethodArgumentNotValidException é a exceção lançada quando da erro na validação dos campos*/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> methodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        /*Isso aqui funciona, mesmo o método retornando um CustomError porque CustomError é um supertipo de ValidationError, upcasting*/
        ValidationError error = new ValidationError(Instant.now(), status.value(), "Invalid data", request.getRequestURI());

        /*MethodArgumentNotValidException e possui dentro dela as informações dos campos que deram erro conforme a validação definida no DTO*/
        /*O método e.getBindingResult().getFieldErrors() retorna uma lista do tipo FieldErro*/
        /*For que vai percorrer todos os erros da lista de FieldError dando um apelido para eles de f*/
        for (FieldError f : e.getBindingResult().getFieldErrors()) {
            /*Adicioamos o erro na lista de erros, getField retorna o nome do campo que deu erro e getDefaultMessage pega a mensagem de validação*/
            error.addError(f.getField(), f.getDefaultMessage());
        }

        return ResponseEntity.status(status).body(error);
    }
}
