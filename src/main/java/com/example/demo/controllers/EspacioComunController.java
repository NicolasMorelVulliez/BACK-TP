package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

import com.example.demo.model.entity.Edificio;
import com.example.demo.model.entity.EdificioDTO;
import com.example.demo.model.entity.EspacioComun;
import com.example.demo.model.entity.EspacioComunDTO;
import com.example.demo.model.entity.Usuario;
import com.example.demo.model.entity.UsuarioDTO;
import com.example.demo.services.EdificioService;
import com.example.demo.services.EspacioComunService;
import com.example.demo.services.UsuarioService;

@RestController
@CrossOrigin("*")
@RequestMapping("/system")
public class EspacioComunController {
	@Autowired
	EspacioComunService EspacioComunService;

	@Autowired
	EdificioService edificioService;

	@Autowired
	UsuarioService usuarioService;

	@GetMapping("/espacioscomunes")
	public ArrayList<EspacioComun> obtenerEspacioComunes() {
		return EspacioComunService.obtenerEspacioComunes();
	}

	@GetMapping(path = "espacioscomunes/{id}")
	public Optional<EspacioComun> obtenerEspacioComunId(@PathVariable("id") Long id) {
		return this.EspacioComunService.obtenerPorId(id);
	}

	@PostMapping("/espacioscomunes")
	public ResponseEntity<?> guardarEspacioComun(@RequestBody EspacioComunDTO espaciocomunDTO) {

		if (espaciocomunDTO.getEdificio() != null) {
			Optional<Edificio> edificioOptional = edificioService.findById(espaciocomunDTO.getEdificio());

			if (edificioOptional.isPresent()){
				EspacioComun EspacioComun = convertToEntity(espaciocomunDTO);
				EspacioComun.setEdificio(edificioOptional.get());
				EspacioComunService.guardarEspacioComun(EspacioComun);
				EspacioComunDTO nuevoEspacioComunDTO = convertToDTO(EspacioComun);

				return new ResponseEntity<>(nuevoEspacioComunDTO, HttpStatus.CREATED);
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

	@DeleteMapping(path = "/espacioscomunes/{id}/delete")
	public ResponseEntity<String> eliminarEspacioComun(@PathVariable("id") Long id) {
	    try {
	        EspacioComunService.eliminarEspacioComun(id);
	        return new ResponseEntity<>("Espacio común eliminado correctamente", HttpStatus.NO_CONTENT);
	    } catch (EmptyResultDataAccessException e) {
	        // Si no se encuentra el espacio común
	        return new ResponseEntity<>("Espacio común no encontrado", HttpStatus.NOT_FOUND);
	    } catch (Exception e) {
	        // Log the exception y retorna un código de estado HTTP apropiado
	        return new ResponseEntity<>("Error al eliminar el espacio común", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	@PutMapping("/espacioscomunes/{id}")
	public ResponseEntity<EspacioComun> actualizarEspacioComun(@PathVariable("id") Long id,
	        @RequestBody EspacioComun nuevoEspacioComun) {
	    try {
	        // Fetch the existing EspacioComun object
	        Optional<EspacioComun> existingEspacioComunOptional = EspacioComunService.obtenerPorId(id);

	        // If the EspacioComun object doesn't exist, return an appropriate HTTP status code
	        if (!existingEspacioComunOptional.isPresent()) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }

	        // Get the existing EspacioComun object
	        EspacioComun existingEspacioComun = existingEspacioComunOptional.get();

	        // Update the fields only if they are present in the request JSON
	        if (nuevoEspacioComun.getNombre() != null) {
	            existingEspacioComun.setNombre(nuevoEspacioComun.getNombre());
	        }

	        // Save the updated EspacioComun object to the database
	        EspacioComunService.guardarEspacioComun(existingEspacioComun);

	        // Return the updated EspacioComun object
	        return new ResponseEntity<>(existingEspacioComun, HttpStatus.OK);
	    } catch (Exception e) {
	        // Log the exception and return an appropriate HTTP status code
	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	public EspacioComun convertToEntity(EspacioComunDTO espaciocomunDTO) {
		EspacioComun EspacioComun = new EspacioComun();
		EspacioComun.setPiso(espaciocomunDTO.getPiso());
		EspacioComun.setNombre(espaciocomunDTO.getNombre());
		EspacioComun.setDescripcion(espaciocomunDTO.getDescripcion());

		Long edificioId = espaciocomunDTO.getEdificio();

		Optional<Edificio> edificio = edificioService.findById(edificioId);

		EspacioComun.setEdificio(edificio.get());
		return EspacioComun;
	}

	public Usuario convertUsuarioDTOToEntity(UsuarioDTO usuarioDTO) {
		Usuario usuario = new Usuario();
		usuario.setNombre(usuarioDTO.getNombre());
		usuario.setApellido(usuarioDTO.getApellido());
		usuario.setDni(usuarioDTO.getDni());
		usuario.setContrasena(usuarioDTO.getContrasena());

		// Obtener el edificio y asignarlo al usuario
		Long edificioId = usuarioDTO.getEdificio();
		Optional<Edificio> edificio = edificioService.findById(edificioId);
		edificio.ifPresent(usuario::setEdificio);

		return usuario;
	}

	public EspacioComunDTO convertToDTO(EspacioComun EspacioComun) {
		EspacioComunDTO espaciocomunDTO = new EspacioComunDTO();
		espaciocomunDTO.setPiso(EspacioComun.getPiso());
		espaciocomunDTO.setDescripcion(EspacioComun.getDescripcion());
		espaciocomunDTO.setNombre(EspacioComun.getNombre());
		espaciocomunDTO.setEdificio(EspacioComun.getEdificio().getIdEdificio());
		return espaciocomunDTO;
	}
}