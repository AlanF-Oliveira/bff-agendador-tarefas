package com.alan.bffagendadortarefas.infrastructure.client;

import com.alan.bffagendadortarefas.business.dto.EnderecoDTO;
import com.alan.bffagendadortarefas.business.dto.TelefoneDTO;
import com.alan.bffagendadortarefas.business.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "usuario", url = "${usuario.url}")
public interface UsuarioClient {

    @GetMapping
    UsuarioDTO buscaUsuarioPorEmail(@RequestParam("email") String email,
                                    @RequestHeader("Authorization") String token);

    @PostMapping
    UsuarioDTO salvaUsuario(@RequestBody UsuarioDTO usuarioDTO);

    @PostMapping("/login")
    String login(@RequestBody UsuarioDTO usuarioDTO);

    @DeleteMapping("/{email}")
    void deletaUsuarioPorEmail(@PathVariable String email,
                               @RequestHeader("Authorization") String token);

    @PutMapping
    UsuarioDTO atualizarDadosUsuario(@RequestBody UsuarioDTO usuarioDTO,
                                     @RequestHeader("Authorization") String token);

    @PutMapping("/endereco")
    EnderecoDTO atualizarEndereco(@RequestBody EnderecoDTO dto,
                                  @RequestParam("id") Long id,
                                  @RequestHeader("Authorization") String token);

    @PutMapping("/telefone")
    TelefoneDTO atualizarTelefone(@RequestBody TelefoneDTO telefoneDTO,
                                  @RequestParam("id") Long id,
                                  @RequestHeader("Authorization") String token);

    @PostMapping("/endereco")
    EnderecoDTO cadastrarEndereco(@RequestBody EnderecoDTO enderecoDTO,
                                   @RequestHeader("Authorization") String token);

    @PostMapping("/telefone")
    TelefoneDTO cadastrarTelefone(@RequestBody TelefoneDTO telefoneDTO,
                                   @RequestHeader("Authorization") String token);

}
