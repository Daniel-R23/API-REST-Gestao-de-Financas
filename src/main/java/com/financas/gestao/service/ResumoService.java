package com.financas.gestao.service;

import com.financas.gestao.dto.DespesaDTO;
import com.financas.gestao.dto.ReceitaDTO;
import com.financas.gestao.dto.ResumoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumoService {

    @Autowired
    DespesaService despesaService;
    @Autowired
    ReceitaService receitaService;

    public ResponseEntity<ResumoDTO> resumir(Long ano, Long mes){
        List<ReceitaDTO> receitas = receitaService.listarPorMes(ano, mes);
        List<DespesaDTO> despesas = despesaService.listarPorMes(ano, mes);
        ResumoDTO resumo = new ResumoDTO();
        resumo.calcularTotalReceitas(receitas);
        resumo.calcularTotalDespesas(despesas);

    }
}
