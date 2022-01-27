package com.financas.gestao.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.financas.gestao.builder.DespesaDetalhesBuilder;
import com.financas.gestao.dto.DespesaDetalhes;
import com.financas.gestao.model.Despesa;
import com.financas.gestao.repository.DespesaRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DespesaServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DespesaRepository repository;


    private final Despesa despesa = DespesaDetalhesBuilder.builder().build().toDespesa();
    private final String urlGeral = "/despesas";
    private final String urlId = "/despesas/{id}";
    private final String urlAnoEMes = "/despesas/{ano}/{mes}";

    @Test
    @Order(1)
    public void deveriaRetornar200CasoOsCamposEstejamPreenchidosCorretamente() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post(urlGeral)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(despesa)))
                .andExpect(status().is(201));
    }

    @Test
    @Order(2)
    public void deveriaRetornar400CasoHajaUmaDespesaComAMesmaDescricaoNoMesmoMesDoMesmoAno() throws Exception {
        despesa.setDescricao("desp1");
        despesa.setData(LocalDate.of(2022,1,1));
        mockMvc.perform(MockMvcRequestBuilders
                .post(urlGeral)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(despesa)))
                .andExpect(status().is(400));
    }

    @Test
    @Order(3)
    public void deveriaRetornar400CasoAlgumCampoObrigatorioDaDespesaSejaVazio() throws Exception {
        despesa.setDescricao("");

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlGeral)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(despesa)))
                .andExpect(status().is(400));
    }

    @Test
    @Order(4)
    public void deveriaRetornar400CasoAlgumCampoObrigatorioDaDespesaSejaNulo() throws Exception {
        despesa.setDescricao(null);

        mockMvc.perform(MockMvcRequestBuilders
                .post(urlGeral)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(despesa)))
                .andExpect(status().is(400));
    }

    @Test
    @Order(5)
    public void deveriaCriarUmDespesaComACategoriaOutrasCasoEsseCampoNaoSejaInformado() throws Exception {
        Despesa despDetalhes = new DespesaDetalhes("desc1", 10D, LocalDate.of(2022,6,25)).converter();

        mockMvc.perform(MockMvcRequestBuilders
                        .post(urlGeral)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(despDetalhes)))
                .andExpect(status().is(201));

        repository.save(despDetalhes);
        assertEquals("OUTRAS", despDetalhes.getCategoria().toString());
    }

    @Test
    @Order(6)
    public void deveriaRetornar200ComAListaDeTodasAsDespesasCadastradasCasoADescricaoNaoForInformada() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(urlGeral))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(7)
    public void deveriaRetornar200ComAListaDeTodasAsDepesasCadastradasQueContenhamADescricaoInformada() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(urlGeral)
                .queryParam("descricao", "desc"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(8)
    public void deveriaRetornarVazioCasoNaoHajaNenhumaDespesaCadastradaComADescricaoInformada() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(urlGeral)
                        .queryParam("descricao", "mlk"))
                .andExpect(status().is(200))
                .andReturn();
        String content = result.getResponse().getContentAsString();

        assertEquals("[]", content);

    }

    @Test
    @Order(9)
    public void deveriaRetornar200ComUmaDespesaCasoOIdInformadoSejaValido() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(urlId,1))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(10)
    public void deveriaRetornar404CasoOIdInformadoSejaInvalido() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(urlId, 99))
                .andExpect(status().is(404));
    }

    @Test
    @Order(11)
    public void deveriaRetornar400CasoOIdInformadoNaoSejaUmNumero() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(urlId, "abc"))
                .andExpect(status().is(400));
    }

    @Test
    @Order(12)
    public void deveriaRetornar200ComAsDespesasReferentesAoMesEAnoInformados() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(urlAnoEMes, 2021, 2))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(13)
    public void deveriaRetornar404CasoOMesEAnoSejaInvalido() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(urlAnoEMes, 99,99))
                .andExpect(status().is(404));
    }

    @Test
    @Order(14)
    public void deveriaRetornar400CasoOMesEAnoNaoSejamNumeros() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .get(urlAnoEMes, "abc", "abc"))
                .andExpect(status().is(400));
    }

    @Test
    @Order(15)
    public void deveriaRetornar200CasoOIdInformadoSejaValidoQuandoAtualizar() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .put(urlId, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(despesa)))
                .andExpect(status().is(200));
    }

    @Test
    @Order(16)
    public void deveriaRetornar404CasoOIdInformadoSejaInvalidoQuandoAtualizar() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .put(urlId,99)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(despesa)))
                .andExpect(status().is(404));

    }

    @Test
    @Order(17)
    public void deveriaRetornar400CasoOIdInformadoNaoSejaUmNumeroQuandoAtualizar() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .put(urlId, "abc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(despesa)))
                .andExpect(status().is(400));
    }

    @Test
    @Order(18)
    public void deveriaRetornar200CasoOIdInformadoSejaValido() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete(urlId, 1))
                .andExpect(status().is(200));
    }

    @Test
    @Order(19)
    public void deveriaRetornar404CasoOIdInformadoSejaInvalidoQuandoDeletar() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .delete(urlId, 99))
                .andExpect(status().is(404));
    }

    @Test
    @Order(20)
    public void deveriaRetornar400CasoOIdInformadoNaoSejaUmNumeroQuandoDeletar()throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .delete(urlId, "abc"))
                .andExpect(status().is(400));
    }

    private String asJsonString(Object o) {
        try{
            return new ObjectMapper().writeValueAsString(o);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
