package com.financas.gestao.dto;

import com.sun.istack.NotNull;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;


@Setter
public class ReceitaDTO {

    @NotNull @NotEmpty
    private String descricao;

    @NotNull @NotEmpty
    private Double valor;

    @NotNull @NotEmpty
    private LocalDate data;

}
