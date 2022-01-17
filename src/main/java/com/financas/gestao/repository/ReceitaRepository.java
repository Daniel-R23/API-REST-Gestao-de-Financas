package com.financas.gestao.repository;

import com.financas.gestao.model.Despesa;
import com.financas.gestao.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {
}
