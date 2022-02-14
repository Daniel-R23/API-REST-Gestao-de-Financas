package com.financas.gestao.service;

import com.financas.gestao.builder.DespesaDetalhesBuilder;
import com.financas.gestao.builder.ReceitaDTOBuilder;
import com.financas.gestao.builder.ResumoDTOBuilder;
import com.financas.gestao.dto.ReceitaDTO;
import com.financas.gestao.dto.ResumoDTO;
import com.financas.gestao.exception.DespesaNotFoundException;
import com.financas.gestao.exception.ReceitaNotFoundException;
import com.financas.gestao.model.Despesa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ResumoServiceTest {

    @Mock
    private DespesaService despesaService;

    @Mock
    private ReceitaService receitaService;

    @InjectMocks
    private ResumoService resumoService;

    private final ResumoDTOBuilder resumo = ResumoDTOBuilder.builder().build();
    private final Despesa despesa = DespesaDetalhesBuilder.builder().build().toDespesa();
    private final ReceitaDTO receita = ReceitaDTOBuilder.builder().build().toReceitaDTO();

    @Test
    public void deveriaRetornarUmResumoDTO() throws DespesaNotFoundException, ReceitaNotFoundException {
        Long ano = 2022L;
        Long mes = 1L;
        when(despesaService.listarPorMes(ano, mes)).thenReturn(List.of(despesa));
        when(receitaService.listarPorMes(ano, mes)).thenReturn(List.of(receita, receita));

        ResumoDTO result = resumoService.resumir(ano, mes);

        assertEquals(resumo.getSaldoFinal(), result.getSaldoFinal());
        assertEquals(resumo.getTotalDespesas(), result.getTotalDespesas());
        assertEquals(resumo.getTotalReceitas(), result.getTotalReceitas());
        assertEquals(resumo.getTotalDespesasPorCategoria(), result.getTotalDespesasPorCategoria());

    }


}