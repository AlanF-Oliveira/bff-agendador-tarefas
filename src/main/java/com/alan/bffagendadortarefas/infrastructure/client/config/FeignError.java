package com.alan.bffagendadortarefas.infrastructure.client.config;

import com.alan.bffagendadortarefas.infrastructure.exceptions.*;
import com.alan.bffagendadortarefas.infrastructure.exceptions.IllegalArgumentException;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class FeignError implements ErrorDecoder {
    private static final String ERRO_PREFIXO = "Erro: ";
    @Override
    public Exception decode(String s, Response response) {

        String mensagemErro = mensagemErro(response);
        switch (response.status()) {
            case 409:
                return new ConflictException(ERRO_PREFIXO + mensagemErro);
            case 403:
                return new ResourceNotFoundException(ERRO_PREFIXO + mensagemErro);
            case 401:
                return new UnauthorizedException(ERRO_PREFIXO + mensagemErro);
            case 400:
                return new IllegalArgumentException(ERRO_PREFIXO + mensagemErro);
            default:
                return new BusinessException(ERRO_PREFIXO + mensagemErro);
        }


    }

    private String mensagemErro(Response response) {
        try {
            if(Objects.isNull(response.body())){
                return "";
            }
            return new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ExternalServiceException("Erro ao ler resposta do serviço externo", e);
        }
    }
}
