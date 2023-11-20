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
public class DepartamentoController {
	@Autowired
	DepartamentoService DepartamentoService;

	@Autowired
	EdificioService edificioService;

	@Autowired
	UsuarioService usuarioService;

	@GetMapping("/departamentos")
	public ArrayList<Departamento> obtenerDepartamentos() {
		return DepartamentoService.obtenerDepartamentos();
	}

	@GetMapping(path = "departamentos/{id}")
	public Optional<Departamento> obtenerDepartamentoId(@PathVariable("id") Long id) {
		return this.DepartamentoService.obtenerPorId(id);
	}

	@PostMapping("/departamentos")
	public ResponseEntity<?> guardarDepartamento(@RequestBody DepartamentoDTO departamentoDTO) {

		if (departamentoDTO.getEdificio() != null && departamentoDTO.getPropietario() != null) {
			Optional<Edificio> edificioOptional = edificioService.findById(departamentoDTO.getEdificio());
			Optional<Usuario> usuarioOptional = usuarioService.obtenerPorId(departamentoDTO.getPropietario());

			if (edificioOptional.isPresent() && usuarioOptional.isPresent() && edificioService
					.usuarioEnEdificio(departamentoDTO.getEdificio(), departamentoDTO.getPropietario())) {
				Departamento departamento = convertToEntity(departamentoDTO);
				departamento.setEdificio(edificioOptional.get());
				departamento.setPropietario(usuarioOptional.get());
				DepartamentoService.guardarEspacioComun(departamento);
				usuarioOptional.get().agregarPropiedad(departamento);
				usuarioService.guardarEspacioComun(usuarioOptional.get());
				DepartamentoDTO nuevoDepartamentoDTO = convertToDTO(departamento);

				return new ResponseEntity<>(nuevoDepartamentoDTO, HttpStatus.CREATED);
			} else {
				// Devolver un error si el edificio o el propietario son nulos o si el usuario
				// no está en el edificio
				return new ResponseEntity<>(
						"El edificio, el propietario o el usuario no pueden ser nulos o el usuario no está en el edificio",
						HttpStatus.BAD_REQUEST);
			}
		} else {
			// Devolver un error si el edificio o el propietario son nulos
			return new ResponseEntity<>("El edificio o el propietario no pueden ser nulos", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("departamentos/{departamentoId}/agregar-inquilino/{usuarioId}")
	public ResponseEntity<?> agregarInquilino(@PathVariable Long departamentoId, @PathVariable Long usuarioId) {
		try {
			// Obtener el departamento y el usuario
			Optional<Departamento> departamentoOptional = DepartamentoService.obtenerPorId(departamentoId);
			Optional<Usuario> usuarioOptional = usuarioService.obtenerPorId(usuarioId);

			// Verificar que el departamento y el usuario existan
			if (!departamentoOptional.isPresent() || !usuarioOptional.isPresent()) {
				return new ResponseEntity<>("Departamento o usuario no encontrado", HttpStatus.NOT_FOUND);
			}

			Departamento departamento = departamentoOptional.get();
			Usuario usuario = usuarioOptional.get();

			// Verificar que el usuario no sea el propietario del departamento
			if (!departamento.getPropietario().getId().equals(usuario.getId())) {
				departamento.agregarInquilino(usuario);
				departamento.setAlquiler(true);
				DepartamentoService.guardarEspacioComun(departamento);

				return new ResponseEntity<>("Usuario agregado como inquilino exitosamente", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("El usuario ya es el propietario del departamento", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error al agregar inquilino: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(path = "/departamentos/{id}/delete")
	public ResponseEntity<String> eliminarDepartamento(@PathVariable("id") Long id) {
	    try {
	        DepartamentoService.eliminarDepartamento(id);
	        return new ResponseEntity<>("Departamento eliminado correctamente", HttpStatus.NO_CONTENT);
	    } catch (Exception e) {
	        // Log the exception and return an appropriate HTTP status code
	        return new ResponseEntity<>("Error al eliminar el departamento", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
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

		List<Usuario> inquilinos = convertUsuarioDTOListToEntity(departamentoDTO.getInquilinos());
		departamento.setInquilinos(inquilinos);

		return departamento;
	}

	private List<Usuario> convertUsuarioDTOListToEntity(List<UsuarioDTO> usuarioDTOList) {
		if (usuarioDTOList == null) {
			return null;
		}

		return usuarioDTOList.stream().map(this::convertUsuarioDTOToEntity).collect(Collectors.toList());
	}

	public Usuario convertUsuarioDTOToEntity(UsuarioDTO usuarioDTO) {
		Usuario usuario = new Usuario();
		usuario.setNombre(usuarioDTO.getNombre());
		usuario.setApellido(usuarioDTO.getApellido());
		usuario.setDni(usuarioDTO.getDni());
		usuario.setContrasena(usuarioDTO.getContrasena());

		Long edificioId = usuarioDTO.getEdificio();
		Optional<Edificio> edificio = edificioService.findById(edificioId);
		edificio.ifPresent(usuario::setEdificio);

		return usuario;
	}

	public DepartamentoDTO convertToDTO(Departamento departamento) {
		DepartamentoDTO departamentoDTO = new DepartamentoDTO();
		departamentoDTO.setPiso(departamento.getPiso());
		departamentoDTO.setAlquiler(departamento.isAlquiler());
		departamentoDTO.setUnidad(departamento.getUnidad());
		departamentoDTO.setPropietario(departamento.getPropietario().getId());
		departamentoDTO.setEdificio(departamento.getEdificio().getIdEdificio());
		return departamentoDTO;
	}
}