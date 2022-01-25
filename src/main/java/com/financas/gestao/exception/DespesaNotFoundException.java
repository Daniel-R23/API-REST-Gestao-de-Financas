package com.financas.gestao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DespesaNotFoundException extends Exception {
    public DespesaNotFoundException(Long id) {
        super("Não foi encontrada nenhuma depesa com o id: " + id);
    }

    public DespesaNotFoundException() {
        super("Nenhuma despesa foi encontrada");
    }
}
