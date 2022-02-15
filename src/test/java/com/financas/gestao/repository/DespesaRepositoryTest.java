package com.financas.gestao.repository;

import com.financas.gestao.enums.Categoria;
import com.financas.gestao.model.Despesa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DataJpaTest
public class DespesaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DespesaRepository repository;

    @Test
    public void deveriaRetornarAsDespesasQueContenhamADescricaoInformada() {
        Despesa desp1 = entityManager.persist(
                new Despesa("Primeira descrição para testes", 19D, LocalDate.of(2022,2,14), Categoria.ALIMENTACAO));
        Despesa desp2 = entityManager.persist(
                new Despesa("Segunda descrição para testes", 20D, LocalDate.of(2021,3,25), Categoria.OUTRAS));

        List<Despesa> despesas = repository.findByDescricaoContainingIgnoreCase("descrição");

        Despesa despesaEncontrada1 = despesas.stream().filter((despesa -> despesa.equals(desp1))).findFirst().orElse(new Despesa());
        Despesa despesaEncontrada2 = despesas.stream().filter((despesa -> despesa.equals(desp2))).findFirst().orElse(new Despesa());

        assertEquals(2, despesas.size());

        assertEquals(desp1.getId(), despesaEncontrada1.getId());
        assertEquals(desp1.getDescricao(), despesaEncontrada1.getDescricao());
        assertEquals(desp1.getValor(), despesaEncontrada1.getValor());
        assertEquals(desp1.getData(), despesaEncontrada1.getData());
        assertEquals(desp1.getCategoria(), despesaEncontrada1.getCategoria());

        assertEquals(desp2.getId(), despesaEncontrada2.getId());
        assertEquals(desp2.getDescricao(), despesaEncontrada2.getDescricao());
        assertEquals(desp2.getValor(), despesaEncontrada2.getValor());
        assertEquals(desp2.getData(), despesaEncontrada2.getData());
        assertEquals(desp2.getCategoria(), despesaEncontrada2.getCategoria());
    }

   @Test
    public void deveriaRetornarAsDespesasQueContenhamADataInformada() {
        Despesa desp1 = entityManager.persist(
               new Despesa("Primeira descrição para testes", 19D, LocalDate.of(2023,2,14), Categoria.ALIMENTACAO));
        List<Despesa> despesas = repository.findByDataContaining(2023L);

        Despesa despesaEncontrada = despesas.stream().filter((despesa -> despesa.equals(desp1))).findFirst().orElse(new Despesa());

        assertEquals(1, despesas.size());

        assertEquals(desp1.getId(), despesaEncontrada.getId());
        assertEquals(desp1.getDescricao(), despesaEncontrada.getDescricao());
        assertEquals(desp1.getValor(), despesaEncontrada.getValor());
        assertEquals(desp1.getData(), despesaEncontrada.getData());
        assertEquals(desp1.getCategoria(), despesaEncontrada.getCategoria());


    }
}