package com.financas.gestao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financas.gestao.builder.DespesaDetalhesBuilder;
import com.financas.gestao.dto.DespesaDetalhes;
import com.financas.gestao.exception.DespesaNotFoundException;
import com.financas.gestao.service.DespesaService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DespesaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DespesaService mockService;

    private final DespesaDetalhesBuilder despesa = DespesaDetalhesBuilder.builder().build();
    private final String urlGeral = "/despesas";
    private final String urlId = "/despesas/{id}";
    private final String urlAnoEMes = "/despesas/{ano}/{mes}";

    @Test
    @Order(1)
    public void deveriaRetornar201ComADespesaNoCorpo() throws Exception {
        when(mockService.cadastrar(any(DespesaDetalhes.class))).thenReturn(despesa.toDespesa());

        mockMvc.perform(MockMvcRequestBuilders
            .post(urlGeral)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(despesa)))
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.descricao").value(despesa.getDescricao()))
            .andExpect(jsonPath("$.valor").value(despesa.getValor()))
            .andExpect(jsonPath("$.data").value(despesa.getData().toString()));

        verify(mockService, times(1)).cadastrar(any(DespesaDetalhes.class));
    }

    @Test
    @Order(2)
    public void deveriaRetornar400ENaoChamarOCadastrarSeHouverUmCampoNuloOuVazio() throws Exception {
        despesa.setDescricao("");
        mockMvc.perform(MockMvcRequestBuilders
            .post(urlGeral)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(despesa.toDespesaDetalhes())))
            .andExpect(status().is(400))
            .andExpect(jsonPath("$[0].campo").value("descricao"))
            .andExpect(jsonPath("$[0].erro").value("must not be empty"));

        despesa.setDescricao(null);
        mockMvc.perform(MockMvcRequestBuilders
            .post(urlGeral)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(despesa.toDespesaDetalhes())))
            .andExpect(status().is(400))
            .andExpect(jsonPath("$[1].campo").value("descricao"))
            .andExpect(jsonPath("$[1].erro").value("must not be null"));

        verify(mockService, times(0)).cadastrar(any());
    }

    @Test
    @Order(3)
    public void deveriaRetornar200EChamarOListarUmaVez() throws Exception {
        when(mockService.listar(null)).thenReturn(List.of(despesa.toDespesaDTO(), despesa.toDespesaDTO(), despesa.toDespesaDTO()));

        mockMvc.perform(MockMvcRequestBuilders
            .get(urlGeral))
            .andExpect(status().is(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(3)));

        verify(mockService, times(1)).listar(null);
    }

    @Test
    @Order(4)
    public void deveriaRetornar200EChamarOListar1VezPassandoADescricao() throws Exception {
        when(mockService.listar("desc")).thenReturn(List.of(despesa.toDespesaDTO()));

        mockMvc.perform(MockMvcRequestBuilders
            .get(urlGeral)
            .queryParam("descricao", "desc"))
            .andExpect(status().is(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)));

        verify(mockService, times(1)).listar("desc");
    }

    @Test
    @Order(5)
    public void deveriaRetornar200ComOCorpoVazioEChamarOListarPassandoAdescricao() throws Exception {
        when(mockService.listar("mlk")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
            .get(urlGeral)
            .queryParam("descricao", "mlk"))
            .andExpect(status().is(200))
            .andExpect(jsonPath("$").doesNotHaveJsonPath());

        verify(mockService, times(1)).listar("mlk");
    }

    @Test
    @Order(6)
    public void deveriaRetornar200EChamarODetalharUmaVez() throws Exception {
        DespesaDetalhes despesaRetornada = despesa.toDespesaDetalhes();
        when(mockService.detalhar(1L)).thenReturn(despesaRetornada);

        mockMvc.perform(MockMvcRequestBuilders
            .get(urlId, 1))
            .andExpect(status().is(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.descricao").value(despesaRetornada.getDescricao()))
            .andExpect(jsonPath("$.valor").value(despesaRetornada.getValor()))
            .andExpect(jsonPath("$.data").value(despesaRetornada.getData().toString()))
            .andExpect(jsonPath("$.categoria").value(despesaRetornada.getCategoria().toString()));

        verify(mockService, times(1)).detalhar(1L);
    }

    @Test
    @Order(7)
    public void deveriaRetornar200ComOCorpoVazioEChamarODetalharUmaVezPassandoOID() throws Exception {
        when(mockService.detalhar(99L)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
            .get(urlId, 99L))
            .andExpect(status().is(200))
            .andExpect(jsonPath("$").doesNotHaveJsonPath());

        verify(mockService, times(1)).detalhar(99L);
    }

    @Test
    @Order(8)
    public void deveriaRetornar200EChamarOListarPorMesUmaVez() throws Exception {
        when(mockService.listarPorMes(2021L, 2L)).thenReturn(List.of(despesa.toDespesa(), despesa.toDespesa()));

        mockMvc.perform(MockMvcRequestBuilders
            .get(urlAnoEMes, 2021, 2))
            .andExpect(status().is(200))
            .andExpect(jsonPath("$").hasJsonPath())
            .andExpect(jsonPath("$", hasSize(2)));

        verify(mockService, times(1)).listarPorMes(2021L, 2L);
    }

    @Test
    @Order(9)
    public void deveriaRetornar404ELancarUmaExcecao() throws Exception {
        when(mockService.listarPorMes(2000L, 0L)).thenThrow(DespesaNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
            .get(urlAnoEMes, 2000, 0))
            .andExpect(status().is(404));

        verify(mockService, times(1)).listarPorMes(2000L,0L);
    }

    @Test
    @Order(10)
    public void deveriaRetornar200CasoOIdInformadoSejaValidoQuandoAtualizar() throws Exception {
        DespesaDetalhes despesaAtualizada = despesa.toDespesaDetalhes();
        despesaAtualizada.setDescricao("Nova descricao nos testes");
        when(mockService.atualizar(anyLong(), any(DespesaDetalhes.class))).thenReturn(despesaAtualizada);

        mockMvc.perform(MockMvcRequestBuilders
            .put(urlId, 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(despesaAtualizada)))
            .andExpect(status().is(200))
            .andExpect(jsonPath("$.descricao").value(despesaAtualizada.getDescricao()))
            .andExpect(jsonPath("$.valor").value(despesa.getValor()))
            .andExpect(jsonPath("$.data").value(despesa.getData().toString()));

        verify(mockService, times(1)).atualizar(anyLong(), any(DespesaDetalhes.class));
    }

    @Test
    @Order(11)
    public void deveriaRetornar404CasoOIdInformadoSejaInvalidoQuandoAtualizar() throws Exception{
        when(mockService.atualizar(anyLong(), any(DespesaDetalhes.class))).thenThrow(DespesaNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
            .put(urlId,99L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(despesa.toDespesaDetalhes())))
            .andExpect(status().is(404))
            .andExpect(jsonPath("$").doesNotHaveJsonPath());

        verify(mockService, times(1)).atualizar(anyLong(), any(DespesaDetalhes.class));
    }

    @Test
    @Order(12)
    public void deveriaRetornar204EChamarODeletarUmaVez() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .delete(urlId, 1L))
            .andExpect(status().is(204))
            .andExpect(jsonPath("$").doesNotHaveJsonPath());

        verify(mockService, times(1)).deletar(1L);
    }

    @Test
    @Order(13)
    public void deveriaRetornar404EChamarODeletarUmaVez() throws Exception{
        doThrow(new DespesaNotFoundException(99L)).when(mockService).deletar(99L);

        mockMvc.perform(MockMvcRequestBuilders
            .delete(urlId, 99))
            .andExpect(status().is(404));

        verify(mockService, times(1)).deletar(anyLong());
    }
}