package com.alan.bffagendadortarefas.business;

import com.alan.bffagendadortarefas.business.dto.in.EnderecoDTORequest;
import com.alan.bffagendadortarefas.business.dto.in.LoginRequestDTO;
import com.alan.bffagendadortarefas.business.dto.in.TelefoneDTORequest;
import com.alan.bffagendadortarefas.business.dto.in.UsuarioDTORequest;
import com.alan.bffagendadortarefas.business.dto.out.EnderecoDTOResponse;
import com.alan.bffagendadortarefas.business.dto.out.TelefoneDTOResponse;
import com.alan.bffagendadortarefas.business.dto.out.UsuarioDTOResponse;
import com.alan.bffagendadortarefas.business.dto.out.ViaCepDTOResponse;
import com.alan.bffagendadortarefas.infrastructure.client.UsuarioClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioClient client;

    public UsuarioDTOResponse salvaUsuario(UsuarioDTORequest usuarioDTO) {
        return client.salvaUsuario(usuarioDTO);
    }

    public String loginUsuario(LoginRequestDTO dto) {
        return client.login(dto);
    }

    public UsuarioDTOResponse buscaUsuarioPorEmail(String email, String token) {
        return client.buscaUsuarioPorEmail(email, token);
    }


    public void deletaUsuarioPorEmail(String email, String token) {
        client.deletaUsuarioPorEmail(email, token);
    }

    public UsuarioDTOResponse atualizaDadosUsuario(String token, UsuarioDTORequest dto) {
        return client.atualizarDadosUsuario(dto, token);
    }

    public EnderecoDTOResponse atualizaEndereco(Long idEndereco, EnderecoDTORequest enderecoDTO, String token) {
        return client.atualizarEndereco(enderecoDTO, idEndereco, token);
    }

    public TelefoneDTOResponse atualizaTelefone(Long idTelefone, TelefoneDTORequest telefoneDTO, String token) {
        return client.atualizarTelefone(telefoneDTO, idTelefone, token);
    }

    public EnderecoDTOResponse cadastraEndereco(String token, EnderecoDTORequest enderecoDTO) {
        return client.cadastrarEndereco(enderecoDTO, token);
    }

    public TelefoneDTOResponse cadastrarTelefone(String token, TelefoneDTORequest telefoneDTO) {
        return client.cadastrarTelefone(telefoneDTO, token);
    }

    public ViaCepDTOResponse buscarEnderecoPorCep(@PathVariable("cep") String cep){
        return client.buscarDadosCep(cep);
    }
}

