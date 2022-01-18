package com.financas.gestao.repository;

import com.financas.gestao.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {
    Optional<Despesa> findByDescricaoAndData(String descricao, LocalDate data);
}
