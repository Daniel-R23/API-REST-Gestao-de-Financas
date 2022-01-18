package com.financas.gestao.dto;

import com.financas.gestao.model.Receita;
import com.financas.gestao.repository.ReceitaRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

    public static List<ReceitaDTO> converterLista(List<Receita> receitas) {
        return receitas.stream().map(ReceitaDTO::new).collect(Collectors.toList());
    }

    public Receita converter() {
        return new Receita(descricao,valor,data);
    }

    public Receita atualizar(Long id, ReceitaRepository repository) {
        Receita receita = repository.findById(id).get();
        receita.setDescricao(this.descricao);
        receita.setValor(this.valor);
        receita.setData(this.data);

        return receita;
    }
}
