package com.alan.bffagendadortarefas.business;

import com.alan.bffagendadortarefas.business.dto.EnderecoDTO;
import com.alan.bffagendadortarefas.business.dto.TelefoneDTO;
import com.alan.bffagendadortarefas.business.dto.UsuarioDTO;
import com.alan.bffagendadortarefas.infrastructure.client.UsuarioClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioClient client;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        return client.salvaUsuario(usuarioDTO);
    }

    public String loginUsuario(UsuarioDTO dto) {
        return client.login(dto);
    }

    public UsuarioDTO buscaUsuarioPorEmail(String email, String token) {
        return client.buscaUsuarioPorEmail(email, token);
    }


    public void deletaUsuarioPorEmail(String email, String token) {
        client.deletaUsuarioPorEmail(email, token);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto) {
        return client.atualizarDadosUsuario(dto, token);
    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO, String token) {
        return client.atualizarEndereco(enderecoDTO, idEndereco, token);
    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO telefoneDTO, String token) {
        return client.atualizarTelefone(telefoneDTO, idTelefone, token);
    }

    public EnderecoDTO cadastraEndereco(String token, EnderecoDTO enderecoDTO) {
        return client.cadastrarEndereco(enderecoDTO, token);
    }

    public TelefoneDTO cadastrarTelefone(String token, TelefoneDTO telefoneDTO) {
        return client.cadastrarTelefone(telefoneDTO, token);
    }
}

