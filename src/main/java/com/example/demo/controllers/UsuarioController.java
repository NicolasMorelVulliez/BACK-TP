package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.entity.Departamento;
import com.example.demo.model.entity.DepartamentoDTO;
import com.example.demo.model.entity.Edificio;
import com.example.demo.model.entity.Usuario;
import com.example.demo.model.entity.UsuarioDTO;
import com.example.demo.services.DepartamentoService;
import com.example.demo.services.EdificioService;
import com.example.demo.services.UsuarioService;

@CrossOrigin("*")
@RestController
@RequestMapping("/system")
public class UsuarioController {
	@Autowired
	DepartamentoService departamentoService;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	EdificioService edificioService;

	@GetMapping("/usuarios")
	public ArrayList<Usuario> obtenerUsuarios() {
		return usuarioService.obtenerUsuarios();
	}

	@GetMapping(path = "usuarios/{id}")
	public Optional<Usuario> obtenerUsuarioId(@PathVariable("id") Long id) {
		return this.usuarioService.obtenerPorId(id);
	}

	@PostMapping("/usuarios")
	public ResponseEntity<?> guardarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
	    try {
	        if (usuarioDTO.getEdificio() != null) {
	            Optional<Edificio> edificioOptional = edificioService.findById(usuarioDTO.getEdificio());

	            if (edificioOptional.isPresent()) {
	                Usuario usuario = convertToEntity(usuarioDTO);
	                usuario.setEdificio(edificioOptional.get());

	                usuarioService.guardarEspacioComun(usuario);

	                UsuarioDTO nuevoUsuarioDTO = convertToDTO(usuario);

	                return new ResponseEntity<>(nuevoUsuarioDTO, HttpStatus.CREATED);
	            } else {
	                // Devolver un error si el edificio no existe
	                return new ResponseEntity<>("El edificio no existe", HttpStatus.BAD_REQUEST);
	            }
	        } else {
	            // Devolver un error si el ID del edificio es nulo
	            return new ResponseEntity<>("El ID del edificio es nulo", HttpStatus.BAD_REQUEST);
	        }
	    } catch (Exception e) {
	        // Manejar otros posibles errores
	        e.printStackTrace();
	        return new ResponseEntity<>("Error al procesar la solicitud", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	@DeleteMapping(path = "/usuarios/{id}")
	public ResponseEntity<Void> eliminarUsuario(@PathVariable("id") Long id) {
	    try {
	        usuarioService.eliminarUsuario(id);
	        return new ResponseEntity<>(HttpStatus.OK);
	    } catch (Exception e) {
	        // Log the exception and return an appropriate HTTP status code
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@PutMapping("/usuarios/{id}")
	public ResponseEntity<Usuario> actualizarUsuario(@PathVariable("id") Long id, @RequestBody Usuario nuevoUsuario) {
	    try {
	        // Fetch the existing usuario object
	        Optional<Usuario> existingUsuario = usuarioService.obtenerPorId(id);

	        // If the usuario object doesn't exist, return an appropriate HTTP status code
	        if (!existingUsuario.isPresent()) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }

	        // Check if the edificio in the new usuario object exists
	        if (nuevoUsuario.getEdificio() != null) {
	            Optional<Edificio> edificioOptional = edificioService.findById(nuevoUsuario.getEdificio().getIdEdificio());

	            // If the edificio doesn't exist, return an appropriate HTTP status code
	            if (!edificioOptional.isPresent()) {
	                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	            }
	        }

	        // Update only the changeable fields
	        existingUsuario.ifPresent(usuario -> {
	            usuario.setNombre(nuevoUsuario.getNombre());
	            usuario.setUsername(nuevoUsuario.getUsername());
	            usuario.setApellido(nuevoUsuario.getApellido());
	            usuario.setDni(nuevoUsuario.getDni());
	            usuario.setContrasena(nuevoUsuario.getContrasena());
	            usuario.setRol(nuevoUsuario.getRol());
	        });

	        // Save the updated usuario object to the database
	        usuarioService.guardarEspacioComun(existingUsuario.get());

	        // Return the updated usuario object
	        return new ResponseEntity<>(existingUsuario.get(), HttpStatus.OK);
	    } catch (Exception e) {
	        // Log the exception and return an appropriate HTTP status code
	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	public Usuario convertToEntity(UsuarioDTO usuarioDTO) {
		Usuario usuario = new Usuario();
		usuario.setNombre(usuarioDTO.getNombre());
		usuario.setApellido(usuarioDTO.getApellido());
		usuario.setDni(usuarioDTO.getDni());
		usuario.setContrasena(usuarioDTO.getContrasena());
		usuario.setUsername(usuarioDTO.getUsername());
		usuario.setRol(usuarioDTO.getRol());
		usuario.setPinEdificio(usuarioDTO.getPinEdificio());
		Long edificioId = usuarioDTO.getEdificio();
		Optional<Edificio> edificio = edificioService.findById(edificioId);
		edificio.ifPresent(usuario::setEdificio);

		return usuario;
	}

	public UsuarioDTO convertToDTO(Usuario usuario) {
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setNombre(usuario.getNombre());
		usuarioDTO.setApellido(usuario.getApellido());
		usuarioDTO.setDni(usuario.getDni());
		usuarioDTO.setContrasena(usuario.getContrasena());
		usuarioDTO.setUsername(usuario.getUsername());
		usuarioDTO.setRol(usuario.getRol());
		usuarioDTO.setPinEdificio(usuario.getPinEdificio());

		// Convertir la lista de Departamento a una lista de DepartamentoDTO
		List<DepartamentoDTO> propiedadesDTO = convertDepartamentoListToDTO(usuario.getPropiedades());
		usuarioDTO.setPropiedades(propiedadesDTO);

		// Omitir la parte de Reclamos

		// Asignar el edificio
		usuarioDTO.setEdificio(usuario.getEdificio().getIdEdificio());

		return usuarioDTO;
	}

	private List<DepartamentoDTO> convertDepartamentoListToDTO(List<Departamento> propiedades) {
		return propiedades.stream().map(this::convertDeptoToDTO).collect(Collectors.toList());
	}

	public DepartamentoDTO convertDeptoToDTO(Departamento departamento) {
		DepartamentoDTO departamentoDTO = new DepartamentoDTO();
		departamentoDTO.setPiso(departamento.getPiso());
		departamentoDTO.setAlquiler(departamento.isAlquiler());
		departamentoDTO.setUnidad(departamento.getUnidad());
		departamentoDTO.setPropietario(departamento.getPropietario().getId());
		departamentoDTO.setEdificio(departamento.getEdificio().getIdEdificio());
		return departamentoDTO;
	}

	public Departamento convertToEntity(DepartamentoDTO departamentoDTO) {
		Departamento departamento = new Departamento();
		departamento.setPiso(departamentoDTO.getPiso());
		departamento.setAlquiler(departamentoDTO.isAlquiler());
		departamento.setUnidad(departamentoDTO.getUnidad());

		Long propietarioId = departamentoDTO.getPropietario();

		Optional<Usuario> propietario = usuarioService.obtenerPorId(propietarioId);

		departamento.setPropietario(propietario.get());

		Long edificioId = departamentoDTO.getEdificio();

		Optional<Edificio> edificio = edificioService.findById(edificioId);

		departamento.setEdificio(edificio.get());
		return departamento;
	}
}
