package com.example.demo.services;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.entity.Usuario;
import com.example.demo.model.repos.DAOUsuarioCRUD;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class UsuarioService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private DAOUsuarioCRUD daoRepository;

	public ArrayList<Usuario> obtenerUsuarios() {
		return (ArrayList<Usuario>) daoRepository.findAll();
	}


	public void guardarEspacioComun(Usuario usuario) {

        	daoRepository.save(usuario);
            System.out.println("###### USUARIO [ADMIN] CREADO Y LISTO PARA OPERAR ######");

	}

	public Optional<Usuario> obtenerPorId(Long id) {
		if (id == null) {
			// Manejo del caso en que el ID es nulo, puedes lanzar una excepción o manejarlo
			// según tus necesidades.
			throw new IllegalArgumentException("El ID no puede ser nulo");
		}
		Optional<Usuario> optionalUsuario = daoRepository.findById(id);
		return optionalUsuario;
	}

	@Transactional(readOnly = true)
	public Usuario encontrarUsuarioPorCredenciales(String usuario, String contrasena) {
	    if (usuario == null || contrasena == null) {
	        throw new IllegalArgumentException("Usuario y contraseña no pueden ser nulos");
	    }

	    Optional<Usuario> usuarioOptional = daoRepository.findByUsername(usuario);

	    if (usuarioOptional.isPresent()) {
	        Usuario usuarioEncontrado = usuarioOptional.get();
	        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	        if (passwordEncoder.matches(contrasena, usuarioEncontrado.getContrasena())) {
	            return usuarioEncontrado;
	        }
	    }

	    throw new NoSuchElementException("Credenciales inválidas");
	}

	private boolean checkPassword(String password, String passwordDB) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		boolean isPasswordMatch = passwordEncoder.matches(password, passwordDB);

		return isPasswordMatch;
	}

	public boolean eliminarUsuario(Long id) {
		try {
			daoRepository.deleteById(id);
			return true;
		} catch (Exception err) {
			return false;
		}
	}
	
	public boolean existeUsuario(String username) {
        Optional<Usuario> usuarioExistente = daoRepository.findByUsername(username);
        return usuarioExistente.isPresent();
    }
	
}