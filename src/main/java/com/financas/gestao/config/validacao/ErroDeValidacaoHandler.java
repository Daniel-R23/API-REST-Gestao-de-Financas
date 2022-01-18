package com.financas.gestao.config.validacao;

import com.financas.gestao.exception.ReceitaJaCadastradaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErroDeValidacaoHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErroDeFormularioDTO> handle(MethodArgumentNotValidException exception){
        List<ErroDeFormularioDTO> erros = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.forEach(e ->{
            String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            ErroDeFormularioDTO erro = new ErroDeFormularioDTO(e.getField(), mensagem);
            erros.add(erro);
        });
        return erros;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ReceitaJaCadastradaException.class)
    public List<ErroDeFormularioDTO> handle(ReceitaJaCadastradaException exception){
        List<ErroDeFormularioDTO> erros = new ArrayList<>();
        String mensagem = messageSource.getMessage(exception, LocaleContextHolder.getLocale());
        ErroDeFormularioDTO erro = new ErroDeFormularioDTO("Descrição", mensagem);
        erros.add(erro);
        return erros;
    }
}
