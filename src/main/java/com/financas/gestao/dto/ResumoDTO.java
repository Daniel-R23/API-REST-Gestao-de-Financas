package com.financas.gestao.dto;

import java.util.List;

public class ResumoDTO {

    private Long totalReceitas;
    private Long totalDespesas;
    private Long saldoFinal = totalDespesas - totalReceitas;
    private List<?> totalDespesasPorCategoria;

    public void calcularTotalReceitas(List<ReceitaDTO> receitas) {
        receitas.forEach(receita -> totalReceitas+=receita.getValor().longValue());
    }

    public void calcularTotalDespesas(List<DespesaDTO> despesas) {
        despesas.forEach(despesa -> totalDespesas+=despesa.getValor().longValue());
    }
}
