package com.financas.gestao.builder;

import com.financas.gestao.dto.DespesaDTO;
import com.financas.gestao.dto.DespesaDetalhes;
import com.financas.gestao.enums.Categoria;
import com.financas.gestao.model.Despesa;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class DespesaDetalhesBuilder {

    @Builder.Default
    private String descricao = "Despesa feita atraves do Builder";

    @Builder.Default
    private Double valor = 50D;

    @Builder.Default
    private LocalDate data = LocalDate.now();

    @Builder.Default
    private Categoria categoria = Categoria.ALIMENTACAO;

    public Despesa toDespesa(){
        return new Despesa(1L, this.descricao, this.valor, this.data, this.categoria);
    }

    public DespesaDetalhes toDespesaDetalhes() {
        return new DespesaDetalhes(this.toDespesa());
    }

    public DespesaDTO toDespesaDTO(){
        return new DespesaDTO(this.toDespesa());
    }
}