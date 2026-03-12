package com.alan.bffagendadortarefas.business;

import com.alan.bffagendadortarefas.business.dto.in.TarefasDTORequest;
import com.alan.bffagendadortarefas.business.dto.out.TarefasDTOResponse;
import com.alan.bffagendadortarefas.business.enums.StatusNotificacaoEnum;
import com.alan.bffagendadortarefas.infrastructure.client.EmailClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailClient client;


    public void enviaEmail(TarefasDTOResponse dto) {
        client.enviarEmail(dto);
    }

    public List<TarefasDTOResponse> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFinal, String token) {
        return client.buscaListaDeTarefasPorPeriodo(dataInicio, dataFinal, token);
    }

    public List<TarefasDTOResponse> buscarTarefasPorEmail(String token) {
        return client.buscaTarefasPorEmail(token);
    }

    public void deletaTarefaPorID(String id, String token) {
        client.deletaPorId(id, token);
    }

    public TarefasDTOResponse alteraStatus(StatusNotificacaoEnum status, String id, String token) {
        return client.alteraStatusDeNotificacao(status, id, token);
    }

    public TarefasDTOResponse updateTarefas(TarefasDTORequest dto, String id, String token) {
        return client.updateTarefas(dto, id, token);
    }

}
