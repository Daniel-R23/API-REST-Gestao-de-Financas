package com.financas.gestao.service;

import com.financas.gestao.builder.ReceitaDTOBuilder;
import com.financas.gestao.dto.ReceitaDTO;
import com.financas.gestao.exception.ReceitaJaCadastradaException;
import com.financas.gestao.exception.ReceitaNotFoundException;
import com.financas.gestao.model.Receita;
import com.financas.gestao.repository.ReceitaRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReceitaServiceTest {

    @Mock
    private ReceitaRepository repository;

    @InjectMocks
    private ReceitaService receitaService;

    private final ReceitaDTOBuilder receita = ReceitaDTOBuilder.builder().build();

    @Test
    @Order(1)
    public void deveriaRetornarReceitaAoCadastrar() throws Exception {
        ReceitaDTO receitaDTO = receita.toReceitaDTO();
        Receita recMock = receita.toReceita();
        when(repository.findByDataContaining(anyLong())).thenReturn(List.of());
        when(repository.save(recMock)).thenReturn(recMock);

        Receita result = receitaService.cadastrar(receitaDTO);

        assertEquals(recMock.getDescricao(), result.getDescricao());
        assertEquals(recMock.getValor(), result.getValor());
        assertEquals(recMock.getData(), result.getData());
    }

    @Test
    @Order(2)
    public void deveriaLancarUmaReceitaJaCadastradaExceptionAoCadastrar(){
        ReceitaDTO receitaDTO = receita.toReceitaDTO();
        Receita recMock = receita.toReceita();
        when(repository.findByDataContaining(anyLong())).thenReturn(List.of(recMock));

        ReceitaJaCadastradaException e = assertThrows(ReceitaJaCadastradaException.class, ()-> receitaService.cadastrar(receitaDTO));
        assertEquals("Já existe uma receita com essa descrição cadastrada no mesmo mês.", e.getDefaultMessage());
    }

    @Test
    @Order(3)
    public void deveriaRetornarUmaListaDeReceitasAoListar(){
        List<Receita> retorno = List.of(receita.toReceita());
        when(repository.findByDescricaoContainingIgnoreCase(receita.getDescricao())).thenReturn(retorno);

        List<ReceitaDTO> result = receitaService.listar(receita.getDescricao());

        assertEquals(retorno.size(), result.size());
        assertEquals(retorno.get(0).getDescricao(), result.get(0).getDescricao());
        assertEquals(retorno.get(0).getData(), result.get(0).getData());
        assertEquals(retorno.get(0).getValor(), result.get(0).getValor());
        assertNotNull(result);
    }

    @Test
    @Order(4)
    public void deveriaRetornarTodasAsReceitasAoListar(){
        List<Receita> retorno = List.of(receita.toReceita());
        when(repository.findAll()).thenReturn(retorno);

        List<ReceitaDTO> result = receitaService.listar(null);

        assertEquals(retorno.size(), result.size());
        assertEquals(retorno.get(0).getDescricao(), result.get(0).getDescricao());
        assertEquals(retorno.get(0).getData(), result.get(0).getData());
        assertEquals(retorno.get(0).getValor(), result.get(0).getValor());
        assertNotNull(result);
    }

    @Test
    @Order(5)
    public void deveriaRetornarVazioAoListar() {
        List<ReceitaDTO> result = receitaService.listar("123");
        List<ReceitaDTO> listaVazia = List.of();

        assertEquals(0, result.size());
        assertEquals(listaVazia, result);
    }

    @Test
    @Order(6)
    public void deveriaRetornarUmaReceitaDetalhesAoDetalhar() throws Exception{
        Long despesaId = receita.toReceita().getId();
        when(repository.findById(despesaId)).thenReturn(Optional.of(receita.toReceita()));
        ReceitaDTO result = receitaService.detalhar(despesaId);

        assertEquals(receita.toReceitaDTO().getDescricao(),result.getDescricao());
        assertEquals(receita.toReceitaDTO().getValor(),result.getValor());
        assertEquals(receita.toReceitaDTO().getData(),result.getData());
    }

    @Test
    @Order(7)
    public void deveriaLancarUmaReceitaNotFoundExceptionAoDetalhar(){
        Long despesaId = receita.toReceita().getId();
        when(repository.findById(despesaId)).thenReturn(Optional.empty());

        ReceitaNotFoundException e = assertThrows(ReceitaNotFoundException.class, () -> receitaService.detalhar(despesaId));

        assertEquals("Não foi encontrada nenhuma receita com o id: 1",e.getMessage());

    }

    @Test
    @Order(8)
    public void deveriaRetornarUmaListaDeReceitasAoListarPorMesEAno() throws Exception{
        Long ano = (long) receita.getData().getYear();
        Long mes = (long) receita.getData().getMonthValue();
        List<Receita> retorno = List.of(receita.toReceita());
        when(repository.findByDataContaining(ano)).thenReturn(retorno);

        List<ReceitaDTO> result = receitaService.listarPorMes(ano, mes);

        assertEquals(retorno.size(), result.size());
        assertEquals(retorno.get(0).getDescricao(), result.get(0).getDescricao());
        assertEquals(retorno.get(0).getValor(), result.get(0).getValor());
        assertEquals(retorno.get(0).getData(), result.get(0).getData());

    }

    @Test
    @Order(9)
    public void deveriaLancarUmaReceitaNotFoundExceptionAoListarPorMesEAno(){
        Long ano = (long) receita.getData().getYear();
        Long mes = (long) receita.getData().getMonthValue();
        when(repository.findByDataContaining(ano)).thenReturn(List.of());

        ReceitaNotFoundException e = assertThrows(ReceitaNotFoundException.class, () -> receitaService.listarPorMes(ano, mes));

        assertEquals("Nenhuma receita foi encontrada.", e.getMessage());
    }

    @Test
    @Order(10)
    public void deveriaRetornarUmaReceitaAoAtualizar() throws ReceitaNotFoundException {
        Receita retorno = receita.toReceita();
        Long despesaId = retorno.getId();
        when(repository.findById(despesaId)).thenReturn(Optional.of(retorno));

        Receita result = receitaService.atualizar(despesaId, receita.toReceitaDTO());

        assertNotNull(result);
        assertEquals(retorno.getData(), result.getData());
        assertEquals(retorno.getDescricao(), result.getDescricao());
        assertEquals(retorno.getValor(), result.getValor());

    }

    @Test
    @Order(11)
    public void deveriaLancarUmaReceitaNotFoundExceptionAoAtualizar(){
        Long receitaIdErrado = 999L;
        ReceitaDTO retorno = receita.toReceitaDTO();

        ReceitaNotFoundException e = assertThrows(ReceitaNotFoundException.class, () -> receitaService.atualizar(receitaIdErrado, retorno));

        assertEquals("Não foi encontrada nenhuma receita com o id: 999",e.getMessage());

    }

    @Test
    @Order(12)
    public void deveriaRetornarVazioAoDeletar() throws ReceitaNotFoundException {
        Long receitaId = 1L;
        Receita retorno = receita.toReceita();
        when(repository.findById(receitaId)).thenReturn(Optional.of(retorno));

        receitaService.deletar(receitaId);

        verify(repository, times(1)).findById(receitaId);
        verify(repository, times(1)).deleteById(receitaId);
    }

    @Test
    @Order(13)
    public void deveriaLancarUmaReceitaNotFoundExceptionAoDeletar(){
        Long receitaIdErrado = 999L;
        when(repository.findById(999L)).thenReturn(Optional.empty());

        ReceitaNotFoundException e = assertThrows(ReceitaNotFoundException.class, () -> receitaService.deletar(receitaIdErrado));

        assertEquals("Não foi encontrada nenhuma receita com o id: 999" ,e.getMessage());
    }
}