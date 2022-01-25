package com.financas.gestao.repository;

import com.financas.gestao.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {
    Optional<Despesa> findByDescricaoAndData(String descricao, LocalDate data);

    List<Despesa> findByDescricaoContainingIgnoreCase(String descricao);

    @Query(value = "Select * from Despesa where data like %:ano%", nativeQuery = true)
    List<Despesa> findByDataContaining(Long ano);
}
