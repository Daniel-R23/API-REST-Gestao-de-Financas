package com.financas.gestao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financas.gestao.builder.ReceitaDTOBuilder;
import com.financas.gestao.dto.ReceitaDTO;
import com.financas.gestao.exception.ReceitaNotFoundException;
import com.financas.gestao.model.Receita;
import com.financas.gestao.service.ReceitaService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReceitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReceitaService mockService;


    private final ReceitaDTOBuilder receita = ReceitaDTOBuilder.builder().build();
    private final String urlGeral = "/receitas";
    private final String urlId = "/receitas/{id}";
    private final String urlAnoEMes = "/receitas/{ano}/{mes}";

    @Test
    @Order(1)
    public void deveriaRetornar201ComAReceitaNoCorpo() throws Exception {
        when(mockService.cadastrar(any(ReceitaDTO.class))).thenReturn(receita.toReceita());

        mockMvc.perform(MockMvcRequestBuilders
                        .post(urlGeral)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(receita)))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.descricao").value(receita.getDescricao()))
                .andExpect(jsonPath("$.valor").value(receita.getValor()))
                .andExpect(jsonPath("$.data").value(receita.getData().toString()));

        verify(mockService, times(1)).cadastrar(any(ReceitaDTO.class));
    }

    @Test
    @Order(2)
    public void deveriaRetornar400ENaoChamarOCadastrarSeHouverUmCampoNuloOuVazio() throws Exception {
        receita.setDescricao("");
        mockMvc.perform(MockMvcRequestBuilders
                        .post(urlGeral)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(receita)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$[0].campo").value("descricao"))
                .andExpect(jsonPath("$[0].erro").value("must not be empty"));

        receita.setDescricao(null);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(urlGeral)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(receita)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$[1].campo").value("descricao"))
                .andExpect(jsonPath("$[1].erro").value("must not be empty"));

        verify(mockService, times(0)).cadastrar(any());
    }

    @Test
    @Order(3)
    public void deveriaRetornar200EChamarOListarUmaVez() throws Exception {
        when(mockService.listar(null)).thenReturn(List.of(receita.toReceitaDTO(), receita.toReceitaDTO(), receita.toReceitaDTO()));

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
        when(mockService.listar("desc")).thenReturn(List.of(receita.toReceitaDTO()));

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
        ReceitaDTO receitaRetornada = receita.toReceitaDTO();
        when(mockService.detalhar(1L)).thenReturn(receitaRetornada);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(urlId, 1))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.descricao").value(receitaRetornada.getDescricao()))
                .andExpect(jsonPath("$.valor").value(receitaRetornada.getValor()))
                .andExpect(jsonPath("$.data").value(receitaRetornada.getData().toString()));

        verify(mockService, times(1)).detalhar(1L);
    }

    @Test
    @Order(7)
    public void deveriaRetornar200ComOCorpoVazioEChamarODetalharUmaVezPassandoOID() throws Exception{
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
        when(mockService.listarPorMes(2021L, 2L)).thenReturn(List.of(receita.toReceitaDTO(), receita.toReceitaDTO()));

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
        when(mockService.listarPorMes(2000L, 0L)).thenThrow(ReceitaNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(urlAnoEMes, 2000, 0))
                .andExpect(status().is(404));

        verify(mockService, times(1)).listarPorMes(2000L,0L);
    }

    @Test
    @Order(10)
    public void deveriaRetornar200CasoOIdInformadoSejaValidoQuandoAtualizar() throws Exception {
        Receita receitaAtualizada = receita.toReceita();
        receitaAtualizada.setDescricao("Nova descricao nos testes");
        when(mockService.atualizar(anyLong(), any(ReceitaDTO.class))).thenReturn(receitaAtualizada);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(urlId, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(receitaAtualizada)))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.descricao").value(receitaAtualizada.getDescricao()))
                .andExpect(jsonPath("$.valor").value(receita.getValor()))
                .andExpect(jsonPath("$.data").value(receita.getData().toString()));

        verify(mockService, times(1)).atualizar(anyLong(), any(ReceitaDTO.class));
    }

    @Test
    @Order(11)
    public void deveriaRetornar404CasoOIdInformadoSejaInvalidoQuandoAtualizar() throws Exception{
        when(mockService.atualizar(anyLong(), any(ReceitaDTO.class))).thenThrow(ReceitaNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(urlId,99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(receita)))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$").doesNotHaveJsonPath());

        verify(mockService, times(1)).atualizar(anyLong(), any(ReceitaDTO.class));
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
       doThrow(new ReceitaNotFoundException(99L)).when(mockService).deletar(99L);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(urlId, 99))
                .andExpect(status().is(404));

        verify(mockService, times(1)).deletar(anyLong());
    }
}