package com.example.demo.model.repos;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Edificio;

@Repository
public interface DAOEdificioCRUD extends CrudRepository<Edificio, Long> {

	
    Optional<Edificio> findByPinSeguridad(int pinSeguridad);

}