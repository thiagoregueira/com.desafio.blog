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

import com.desafio.blog.dtos.PostDto;
import com.desafio.blog.model.Comentario;
import com.desafio.blog.model.Post;
import com.desafio.blog.repository.ComentarioRepository;
import com.desafio.blog.repository.PostRepository;
import com.desafio.blog.repository.UsuarioRepository;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @PostMapping("")
    public ResponseEntity<Post> createPost(@RequestBody PostDto novoPostDto) {
        Post novoPost = new Post();
        novoPost.setTitulo(novoPostDto.getTitulo());
        novoPost.setConteudo(novoPostDto.getConteudo());
        novoPost.setAutor(novoPostDto.getAutor());
        Post post = postRepository.save(novoPost);
        return new ResponseEntity<Post>(post, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            List<PostDto> postsDto = new ArrayList<>();
            for (Post post : posts) {
                PostDto postDto = new PostDto();
                postDto.setTitulo(post.getTitulo());
                postDto.setConteudo(post.getConteudo());
                postDto.setAutor(usuarioRepository.findById(post.getAutor().getIdUsuario()).orElse(null));
                postDto.setComentarios(comentarioRepository.findByPost(post));
                postsDto.add(postDto);
            }
            return new ResponseEntity<>(postsDto, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            PostDto postDto = new PostDto();
            postDto.setTitulo(post.getTitulo());
            postDto.setConteudo(post.getConteudo());
            postDto.setAutor(usuarioRepository.findById(post.getAutor().getIdUsuario()).orElse(null));
            postDto.setComentarios(comentarioRepository.findByPost(post));
            return new ResponseEntity<>(postDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody PostDto postDto) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.setTitulo(postDto.getTitulo());
            post.setConteudo(postDto.getConteudo());
            post.setAutor(postDto.getAutor());
            return ResponseEntity.ok(postRepository.save(post));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            // Pegar todos os comentarios associados ao post
            List<Comentario> comentarios = comentarioRepository.findByPost(post);
            // Deletar todos os comentarios associados ao post
            for (Comentario comentario : comentarios) {
                comentarioRepository.delete(comentario);
            }
            // Deletar o post
            postRepository.delete(post);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
