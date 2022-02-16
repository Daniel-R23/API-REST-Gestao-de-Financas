package com.financas.gestao.exception;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UsuarioJaCadastradoException extends Exception implements MessageSourceResolvable {
    public UsuarioJaCadastradoException(){
        super("Ja existe um usuario com esse email cadastrado.");
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
        return "Ja existe um usuario com esse email cadastrado.";
    }
}
