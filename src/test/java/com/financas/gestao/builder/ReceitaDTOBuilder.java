package com.financas.gestao.builder;

import com.financas.gestao.dto.ReceitaDTO;
import com.financas.gestao.model.Receita;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class ReceitaDTOBuilder {

    @Builder.Default
    private String descricao = "Receita feita atraves do Builder";

    @Builder.Default
    private Double valor = 50D;

    @Builder.Default
    private LocalDate data = LocalDate.now();

    public Receita toReceita() {
        return  new Receita(1L, descricao, valor, data);
    }

    public ReceitaDTO toReceitaDTO() {
        return  new ReceitaDTO(descricao, valor, data);
    }
}