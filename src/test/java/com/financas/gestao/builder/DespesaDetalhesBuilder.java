package com.financas.gestao.builder;

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
    private String descricao = "Despesa feita através do Builder";

    @Builder.Default
    private Double valor = 50D;

    @Builder.Default
    private LocalDate data = LocalDate.now();

    @Builder.Default
    private Categoria categoria = Categoria.ALIMENTACAO;

    public Despesa toDespesa(){
        return new Despesa(this.descricao, this.valor, this.data, this.categoria);
    }
}
