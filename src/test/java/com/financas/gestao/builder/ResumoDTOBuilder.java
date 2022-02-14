package com.financas.gestao.builder;

import com.financas.gestao.dto.ResumoDTO;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Builder
@Data
public class ResumoDTOBuilder {

    @Builder.Default
    private Long totalReceitas = 100L;

    @Builder.Default
    private Long totalDespesas = 50L;

    @Builder.Default
    private Long saldoFinal = 50L;

    @Builder.Default
    private Map<String, Double> totalDespesasPorCategoria = new HashMap<>(){{put("ALIMENTACAO", 50d);}};

    public ResumoDTO toResumoDTO() {
        return new ResumoDTO(this.totalReceitas, this.totalDespesas, this.saldoFinal, this.totalDespesasPorCategoria);
    }
}
