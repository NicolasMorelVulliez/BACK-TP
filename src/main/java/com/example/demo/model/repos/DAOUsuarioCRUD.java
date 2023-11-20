package com.example.demo.model.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Usuario;

@Repository
public interface DAOUsuarioCRUD extends CrudRepository<Usuario, Long> {

	@Query("SELECT u FROM Usuario u WHERE u.username = :username AND u.contrasena = :contrasena")
	Optional<Usuario> findByUsernameAndContrasena(@Param("username") String username, @Param("contrasena") String contrasena);
	
	@Query("SELECT u FROM Usuario u WHERE u.username = :username")
    Optional<Usuario> findByUsername(String username);

}