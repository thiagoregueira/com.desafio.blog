package com.desafio.blog.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.blog.dtos.UsuarioDto;
import com.desafio.blog.model.Usuario;
import com.desafio.blog.repository.PostRepository;
import com.desafio.blog.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PostRepository postRepository;

    @PostMapping("")
    public ResponseEntity<Usuario> createUsuario(@RequestBody UsuarioDto novoUsuarioDto) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(novoUsuarioDto.getNome());
        novoUsuario.setSobrenome(novoUsuarioDto.getSobrenome());
        novoUsuario.setEmail(novoUsuarioDto.getEmail());
        novoUsuario.setSenha(novoUsuarioDto.getSenha());
        Usuario usuario = usuarioRepository.save(novoUsuario);
        return new ResponseEntity<Usuario>(usuario, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<UsuarioDto>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<UsuarioDto> usuariosDto = new ArrayList<>();
            for (Usuario usuario : usuarios) {
                UsuarioDto usuarioDto = new UsuarioDto();
                usuarioDto.setNome(usuario.getNome());
                usuarioDto.setSobrenome(usuario.getSobrenome());
                usuarioDto.setEmail(usuario.getEmail());
                usuarioDto.setSenha(usuario.getSenha());
                usuarioDto.setPosts(postRepository.findByAutor(usuario));
                usuariosDto.add(usuarioDto);
            }
            return new ResponseEntity<>(usuariosDto, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> getUsuario(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            UsuarioDto usuarioDto = new UsuarioDto();
            usuarioDto.setNome(usuario.getNome());
            usuarioDto.setSobrenome(usuario.getSobrenome());
            usuarioDto.setEmail(usuario.getEmail());
            usuarioDto.setSenha(usuario.getSenha());
            usuarioDto.setPosts(postRepository.findByAutor(usuario));
            return new ResponseEntity<>(usuarioDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody UsuarioDto usuarioDto) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.setNome(usuarioDto.getNome());
            usuario.setSobrenome(usuarioDto.getSobrenome());
            usuario.setEmail(usuarioDto.getEmail());
            usuario.setSenha(usuarioDto.getSenha());
            return ResponseEntity.ok(usuarioRepository.save(usuario));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            usuarioRepository.delete(usuarioOptional.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
