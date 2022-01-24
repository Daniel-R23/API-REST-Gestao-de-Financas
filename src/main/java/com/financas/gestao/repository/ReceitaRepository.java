package com.financas.gestao.repository;

import com.financas.gestao.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {
    Optional<Receita> findByDescricaoAndData(String desc, LocalDate data);

    List<Receita> findByDescricaoContainingIgnoreCase(String descricao);

    @Query(value = "Select * from Receita where data like %:ano%", nativeQuery = true)
    List<Receita> findByDataContaining(Long ano);
}
