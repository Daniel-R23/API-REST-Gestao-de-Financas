package com.financas.gestao.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDTO {

    private String token;
    private String tipo;


    public TokenDTO(String token, String tipo) {
        this.token = token;
        this.tipo = tipo;
    }
}
