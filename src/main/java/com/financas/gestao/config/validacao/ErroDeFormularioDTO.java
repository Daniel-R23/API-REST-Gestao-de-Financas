package com.financas.gestao.config.validacao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErroDeFormularioDTO {

    private String campo;
    private String erro;
}
