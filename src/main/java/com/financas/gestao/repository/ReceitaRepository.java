package com.financas.gestao.repository;

import com.financas.gestao.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {
    Optional<Receita> findByDescricaoAndData(String desc, LocalDate data);
}
