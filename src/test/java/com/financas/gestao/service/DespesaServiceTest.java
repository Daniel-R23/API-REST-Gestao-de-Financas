package com.financas.gestao.service;

import com.financas.gestao.builder.DespesaDetalhesBuilder;
import com.financas.gestao.dto.DespesaDTO;
import com.financas.gestao.dto.DespesaDetalhes;
import com.financas.gestao.exception.DespesaJaCadastradaException;
import com.financas.gestao.exception.DespesaNotFoundException;
import com.financas.gestao.model.Despesa;
import com.financas.gestao.repository.DespesaRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
    
@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DespesaServiceTest {

    @Mock
    private DespesaRepository repository;

    @InjectMocks
    private DespesaService despesaService;

    private final DespesaDetalhesBuilder despesa = DespesaDetalhesBuilder.builder().build();

    @Test
    @Order(1)
    public void deveriaRetornarDespesaAoCadastrar() throws Exception {
        DespesaDetalhes despesaDetalhes = despesa.toDespesaDetalhes();
        Despesa despMock = despesa.toDespesa();
        when(repository.findByDataContaining(anyLong())).thenReturn(List.of());
        when(repository.save(despMock)).thenReturn(despMock);

        Despesa result = despesaService.cadastrar(despesaDetalhes);

        assertEquals(despMock.getDescricao(), result.getDescricao());
        assertEquals(despMock.getValor(), result.getValor());
        assertEquals(despMock.getData(), result.getData());
        assertEquals(despMock.getCategoria(), result.getCategoria());
    }

    @Test
    @Order(2)
    public void deveriaRetornarUmaDespesaComACategoriaOutrasAoCadastrar() throws DespesaJaCadastradaException {
        DespesaDetalhes despesaDetalhes = new DespesaDetalhes("Descricao", 10D, LocalDate.of(2022, 2, 9));

        Despesa retorno = despesaService.cadastrar(despesaDetalhes);

        assertEquals("OUTRAS", retorno.getCategoria().toString());
    }

    @Test
    @Order(3)
    public void deveriaLancarUmaDespesaJaCadastradaExceptionAoCadastrar(){
        DespesaDetalhes despesaDetalhes = despesa.toDespesaDetalhes();
        Despesa despMock = despesa.toDespesa();
        when(repository.findByDataContaining(anyLong())).thenReturn(List.of(despMock));

        DespesaJaCadastradaException e = assertThrows(DespesaJaCadastradaException.class, ()->despesaService.cadastrar(despesaDetalhes));
        assertEquals("Já existe uma despesa com essa descrição cadastrada no mesmo mês.", e.getDefaultMessage());
    }

    @Test
    @Order(4)
    public void deveriaRetornarUmaListaDeDespesasAoListar(){
        List<Despesa> retorno = List.of(despesa.toDespesa());
        when(repository.findByDescricaoContainingIgnoreCase(despesa.getDescricao())).thenReturn(retorno);

        List<DespesaDTO> result = despesaService.listar(despesa.getDescricao());

        assertEquals(retorno.size(), result.size());
        assertEquals(retorno.get(0).getDescricao(), result.get(0).getDescricao());
        assertEquals(retorno.get(0).getData(), result.get(0).getData());
        assertEquals(retorno.get(0).getValor(), result.get(0).getValor());
        assertNotNull(result);
    }

    @Test
    @Order(5)
    public void deveriaRetornarTodasAsDespesasAoListar(){
        List<Despesa> retorno = List.of(despesa.toDespesa());
        when(repository.findAll()).thenReturn(retorno);

        List<DespesaDTO> result = despesaService.listar(null);

        assertEquals(retorno.size(), result.size());
        assertEquals(retorno.get(0).getDescricao(), result.get(0).getDescricao());
        assertEquals(retorno.get(0).getData(), result.get(0).getData());
        assertEquals(retorno.get(0).getValor(), result.get(0).getValor());
        assertNotNull(result);
    }

    @Test
    @Order(6)
    public void deveriaRetornarVazioAoListar() {
        List<DespesaDTO> result = despesaService.listar("123");
        List<DespesaDTO> listaVazia = List.of();

        assertEquals(0, result.size());
        assertEquals(listaVazia, result);
    }

    @Test
    @Order(7)
    public void deveriaRetornarUmaDespesaDetalhesAoDetalhar() throws Exception{
        Long despesaId = despesa.toDespesa().getId();
        when(repository.findById(despesaId)).thenReturn(Optional.of(despesa.toDespesa()));
        DespesaDetalhes result = despesaService.detalhar(despesaId);

        assertEquals(despesa.toDespesaDetalhes().getDescricao(),result.getDescricao());
        assertEquals(despesa.toDespesaDetalhes().getValor(),result.getValor());
        assertEquals(despesa.toDespesaDetalhes().getCategoria(),result.getCategoria());
        assertEquals(despesa.toDespesaDetalhes().getData(),result.getData());
    }

    @Test
    @Order(8)
    public void deveriaLancarUmaDepesaNotFoundExceptionAoDetalhar(){
        Long despesaId = despesa.toDespesa().getId();
        when(repository.findById(despesaId)).thenReturn(Optional.empty());

        DespesaNotFoundException e = assertThrows(DespesaNotFoundException.class, () -> despesaService.detalhar(despesaId));

        assertEquals("Não foi encontrada nenhuma depesa com o id: 1",e.getMessage());

    }

    @Test
    @Order(9)
    public void deveriaRetornarUmaListaDeDespesasAoListarPorMeseAno() throws Exception{
        Long ano = (long)despesa.getData().getYear();
        Long mes = (long)despesa.getData().getMonthValue();
        List<Despesa> retorno = List.of(despesa.toDespesa());
        when(repository.findByDataContaining(ano)).thenReturn(retorno);

        List<Despesa> result = despesaService.listarPorMes(ano, mes);

        assertEquals(retorno.size(), result.size());
        assertEquals(retorno.get(0).getDescricao(), result.get(0).getDescricao());
        assertEquals(retorno.get(0).getCategoria(), result.get(0).getCategoria());
        assertEquals(retorno.get(0).getValor(), result.get(0).getValor());
        assertEquals(retorno.get(0).getData(), result.get(0).getData());

    }

    @Test
    @Order(10)
    public void deveriaLancarUmaDespesaNotFoundExceptionAoListarPorMesEAno(){
        Long ano = (long)despesa.getData().getYear();
        Long mes = (long)despesa.getData().getMonthValue();
        when(repository.findByDataContaining(ano)).thenReturn(List.of());

        DespesaNotFoundException e = assertThrows(DespesaNotFoundException.class, () -> despesaService.listarPorMes(ano, mes));

        assertEquals("Nenhuma despesa foi encontrada", e.getMessage());
    }

    @Test
    @Order(11)
    public void deveriaRetornarUmaDespesaAoAtualizar() throws DespesaNotFoundException {
        Despesa retorno = despesa.toDespesa();
        Long despesaId = retorno.getId();
        when(repository.findById(despesaId)).thenReturn(Optional.of(retorno));

        DespesaDetalhes result = despesaService.atualizar(despesaId, despesa.toDespesaDetalhes());

        assertNotNull(result);
        assertEquals(retorno.getData(), result.getData());
        assertEquals(retorno.getDescricao(), result.getDescricao());
        assertEquals(retorno.getValor(), result.getValor());
        assertEquals(retorno.getCategoria(), result.getCategoria());

    }

    @Test
    @Order(12)
    public void deveriaLancarUmaDespesaNotFoundExceptionAoAtualizar(){
        Long despesaIdErrado = 999L;
        DespesaDetalhes retorno = despesa.toDespesaDetalhes();

        DespesaNotFoundException e = assertThrows(DespesaNotFoundException.class, () -> despesaService.atualizar(despesaIdErrado, retorno));

        assertEquals("Não foi encontrada nenhuma depesa com o id: 999",e.getMessage());

    }

    @Test
    @Order(13)
    public void deveriaRetornarVazioAoDeletar() throws DespesaNotFoundException {
        Long despesaId = 1L;
        Despesa retorno = despesa.toDespesa();
        when(repository.findById(despesaId)).thenReturn(Optional.of(retorno));

        despesaService.deletar(despesaId);

        verify(repository, times(1)).findById(despesaId);
        verify(repository, times(1)).deleteById(despesaId);
    }

    @Test
    @Order(14)
    public void deveriaLancarUmaDespesaNotFoundExceptionAoDeletar(){
        Long despesaIdErrado = 999L;
        when(repository.findById(999L)).thenReturn(Optional.empty());

        DespesaNotFoundException e = assertThrows(DespesaNotFoundException.class, () -> despesaService.deletar(despesaIdErrado));

        assertEquals("Não foi encontrada nenhuma depesa com o id: 999" ,e.getMessage());
    }
}