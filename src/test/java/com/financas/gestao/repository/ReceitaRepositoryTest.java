package com.financas.gestao.repository;

import com.financas.gestao.enums.Categoria;
import com.financas.gestao.model.Despesa;
import com.financas.gestao.model.Receita;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DataJpaTest
public class ReceitaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReceitaRepository repository;

    @Test
    public void deveriaRetornarAsDespesasQueContenhamADescricaoInformada(){
        Receita rec1 = entityManager.persist(
                new Receita("Primeira descrição para testes", 19D, LocalDate.of(2022,2,14)));
        Receita rec2 = entityManager.persist(
                new Receita("Segunda descrição para testes", 20D, LocalDate.of(2021,3,25)));

        List<Receita> receitas = repository.findByDescricaoContainingIgnoreCase("descrição");

        Receita receitaEncontrada1 = receitas.stream().filter((despesa -> despesa.equals(rec1))).findFirst().orElse(new Receita());
        Receita receitaEncontrada2 = receitas.stream().filter((despesa -> despesa.equals(rec2))).findFirst().orElse(new Receita());

        assertEquals(2, receitas.size());

        assertEquals(rec1.getId(), receitaEncontrada1.getId());
        assertEquals(rec1.getDescricao(), receitaEncontrada1.getDescricao());
        assertEquals(rec1.getValor(), receitaEncontrada1.getValor());
        assertEquals(rec1.getData(), receitaEncontrada1.getData());

        assertEquals(rec2.getId(), receitaEncontrada2.getId());
        assertEquals(rec2.getDescricao(), receitaEncontrada2.getDescricao());
        assertEquals(rec2.getValor(), receitaEncontrada2.getValor());
        assertEquals(rec2.getData(), receitaEncontrada2.getData());
    }

    @Test
    public void deveriaRetornarAsDespesasQueContenhamADataInformada() {
        Receita rec1 = entityManager.persist(
                new Receita("Primeira descrição para testes", 19D, LocalDate.of(2023,2,14)));
        List<Receita> receitas = repository.findByDataContaining(2023L);

        Receita receitaEncontrada = receitas.stream().filter((despesa -> despesa.equals(rec1))).findFirst().orElse(new Receita());

        assertEquals(1, receitas.size());

        assertEquals(rec1.getId(), receitaEncontrada.getId());
        assertEquals(rec1.getDescricao(), receitaEncontrada.getDescricao());
        assertEquals(rec1.getValor(), receitaEncontrada.getValor());
        assertEquals(rec1.getData(), receitaEncontrada.getData());
    }
}