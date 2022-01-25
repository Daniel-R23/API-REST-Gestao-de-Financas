package com.financas.gestao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReceitaNotFoundException extends Throwable {
    public ReceitaNotFoundException(Long id) {
        super("Não foi encontrada nenhuma receita com o id: " + id);
    }
    public ReceitaNotFoundException(){super("Nenhuma receita foi encontrada.");}
}
