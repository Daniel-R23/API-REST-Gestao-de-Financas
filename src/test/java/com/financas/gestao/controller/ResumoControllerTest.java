package com.financas.gestao.controller;

import com.financas.gestao.builder.ResumoDTOBuilder;
import com.financas.gestao.service.ResumoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class ResumoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResumoService mockService;

    private final ResumoDTOBuilder resumo = ResumoDTOBuilder.builder().build();

    @Test
    public void deveriaRetornar200ComOResumoDTONoCorpo() throws Exception {
        when(mockService.resumir(anyLong(), anyLong())).thenReturn(resumo.toResumoDTO());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/resumo/{ano}/{mes}", 2022L, 1L))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(mockService, times(1)).resumir(2022L, 1L);
    }

}