package com.financas.gestao.dto;

import com.financas.gestao.model.Despesa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResumoDTO {

    private Long totalReceitas = 0L;
    private Long totalDespesas = 0L;
    private Long saldoFinal = 0L;
    private Map<String, Double> totalDespesasPorCategoria = new HashMap<>();

    public void calcularTotalReceitas(List<ReceitaDTO> receitas) {
        receitas.forEach(receita -> totalReceitas += receita.getValor().longValue());
    }

    public void calcularTotalDespesas(List<Despesa> despesas) {
        despesas.forEach(despesa -> totalDespesas += despesa.getValor().longValue());
    }

    public void calcularSaldoFinal(){
        this.saldoFinal = this.totalReceitas - this.totalDespesas;
    }

    public void calcularTotalDespesasPorCategoria(List<Despesa> despesas){
        despesas.forEach(despesa -> {
            String chave = despesa.getCategoria().toString();
            Double valor = despesa.getValor();
            totalDespesasPorCategoria.merge(chave, valor, Double::sum);
        });
    }
}
