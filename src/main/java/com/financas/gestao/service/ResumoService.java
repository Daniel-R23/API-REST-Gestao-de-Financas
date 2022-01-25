package com.financas.gestao.service;

import com.financas.gestao.dto.ReceitaDTO;
import com.financas.gestao.dto.ResumoDTO;
import com.financas.gestao.exception.DespesaNotFoundException;
import com.financas.gestao.exception.ReceitaNotFoundException;
import com.financas.gestao.model.Despesa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumoService {

    @Autowired
    private DespesaService despesaService;
    @Autowired
    private ReceitaService receitaService;

    public ResumoDTO resumir(Long ano, Long mes) throws ReceitaNotFoundException, DespesaNotFoundException {
        List<ReceitaDTO> receitas = receitaService.listarPorMes(ano, mes);
        List<Despesa> despesas = despesaService.listarPorMes(ano, mes);
        ResumoDTO resumo = new ResumoDTO();
        resumo. calcularTotalReceitas(receitas);
        resumo.calcularTotalDespesas(despesas);
        resumo.calcularSaldoFinal();
        resumo.calcularTotalDespesasPorCategoria(despesas);
        return resumo;
    }
}
