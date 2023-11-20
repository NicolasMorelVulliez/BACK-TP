package com.example.demo.model.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Departamento;

@Repository
public interface DAODeptoCRUD extends CrudRepository<Departamento, Long> {

}