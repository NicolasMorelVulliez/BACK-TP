package com.example.demo.model.repos;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.entity.Reclamo;

public interface DAOReclamoCRUD extends CrudRepository<Reclamo, Long> {

}