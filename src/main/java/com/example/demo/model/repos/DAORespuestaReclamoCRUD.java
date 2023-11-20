package com.example.demo.model.repos;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.entity.RespuestaReclamo;

public interface DAORespuestaReclamoCRUD extends CrudRepository<RespuestaReclamo, Long> {

}