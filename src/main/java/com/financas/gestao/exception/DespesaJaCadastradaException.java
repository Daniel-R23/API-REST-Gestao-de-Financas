package com.financas.gestao.exception;

import org.springframework.context.MessageSourceResolvable;

public class DespesaJaCadastradaException extends Exception implements MessageSourceResolvable{
    public DespesaJaCadastradaException() {
        super("Já existe uma despesa com essa descrição cadastrada no mesmo mês.");
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
        return "Já existe uma despesa com essa descrição cadastrada no mesmo mês.";
    }

}
