package com.alan.bffagendadortarefas.infrastructure.client;

import com.alan.bffagendadortarefas.business.dto.out.TarefasDTOResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificacao", url = "${notificacao.url}")
public interface EmailClient {
    void enviarEmail(@RequestBody TarefasDTOResponse dto);
}
