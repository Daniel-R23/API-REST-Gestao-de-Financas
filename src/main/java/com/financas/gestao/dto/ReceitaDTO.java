package com.financas.gestao.dto;

import com.financas.gestao.model.Receita;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceitaDTO {

    @NotNull
    @NotEmpty
    private String descricao;


    @NotNull
    private Double valor;

    @NotNull
    private LocalDate data;

    public ReceitaDTO(Receita receita) {
        this.descricao = receita.getDescricao();
        this.valor = receita.getValor();
        this.data = receita.getData();
    }

    public static List<ReceitaDTO> converter(List<Receita> receitas) {
        return receitas.stream().map(ReceitaDTO::new).collect(Collectors.toList());
    }


    public Receita toReceita() {
        return new Receita(descricao,valor,data);
    }
}
