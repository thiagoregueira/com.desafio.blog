package com.desafio.blog.dtos;

import java.util.List;

import com.desafio.blog.model.Comentario;
import com.desafio.blog.model.Usuario;

import lombok.Data;

@Data
public class PostDto {

    private String titulo;
    private String conteudo;

    private List<Comentario> comentarios;
    private Usuario autor;

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }
}
