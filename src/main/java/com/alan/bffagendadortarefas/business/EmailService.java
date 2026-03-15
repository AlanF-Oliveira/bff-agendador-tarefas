package com.alan.bffagendadortarefas.business;

import com.alan.bffagendadortarefas.business.dto.out.TarefasDTOResponse;
import com.alan.bffagendadortarefas.infrastructure.client.EmailClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailClient client;

    public void enviaEmail(TarefasDTOResponse dto) {
        client.enviarEmail(dto);
    }

}
