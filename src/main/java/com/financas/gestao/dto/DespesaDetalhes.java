package com.financas.gestao.dto;

import com.financas.gestao.enums.Categoria;
import com.financas.gestao.model.Despesa;
import com.financas.gestao.repository.DespesaRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DespesaDetalhes {

    @NotNull
    @NotEmpty
    private String descricao;

    @NotNull
    private Double valor;

    @NotNull
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    private Categoria categoria = Categoria.OUTRAS;

    public DespesaDetalhes(Despesa despesa) {
        this.descricao = despesa.getDescricao();
        this.valor = despesa.getValor();
        this.data = despesa.getData();
        this.categoria = despesa.getCategoria();
    }

    public Despesa converter() {
        return new Despesa(descricao,valor,data, categoria);
    }

    public Despesa atualizar(Long id, DespesaRepository repository) {
        Despesa despesa = repository.findById(id).get();
        despesa.setDescricao(this.descricao);
        despesa.setValor(this.valor);
        despesa.setData(this.data);
        despesa.setCategoria(this.categoria);

        return despesa;
    }
}
