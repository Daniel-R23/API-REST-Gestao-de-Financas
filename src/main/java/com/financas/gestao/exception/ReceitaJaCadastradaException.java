package com.financas.gestao.exception;

import org.springframework.context.MessageSourceResolvable;

public class ReceitaJaCadastradaException extends Exception implements MessageSourceResolvable {
    public ReceitaJaCadastradaException() {
        super("Já existe uma receita com essa descrição cadastrada no mesmo mês.");
    }

    @Override
    public String[] getCodes() {
        return new String[0];
    }

    @Override
    public Object[] getArguments() {
        return MessageSourceResolvable.super.getArguments();
    }

    @Override
    public String getDefaultMessage() {
        return "Já existe uma receita com essa descrição cadastrada no mesmo mês.";
    }
}
