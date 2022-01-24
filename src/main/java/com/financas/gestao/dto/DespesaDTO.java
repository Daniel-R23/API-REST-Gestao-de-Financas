package com.financas.gestao.dto;

import com.financas.gestao.model.Despesa;
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
public class DespesaDTO {

    @NotNull
    @NotEmpty
    private String descricao;

    @NotNull
    private Double valor;

    @NotNull
    private LocalDate data;

    public DespesaDTO(Despesa despesa) {
        this.descricao = despesa.getDescricao();
        this.valor = despesa.getValor();
        this.data = despesa.getData();
    }

    public static List<DespesaDTO> converterLista(List<Despesa> despesas) {
        return despesas.stream().map(DespesaDTO::new).collect(Collectors.toList());
    }
}
