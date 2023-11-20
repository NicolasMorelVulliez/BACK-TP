package com.example.demo.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.entity.Departamento;
import com.example.demo.model.entity.Edificio;
import com.example.demo.model.entity.EspacioComun;
import com.example.demo.model.entity.Reclamo;
import com.example.demo.model.entity.ReclamoDTO;
import com.example.demo.model.entity.Usuario;
import com.example.demo.services.DepartamentoService;
import com.example.demo.services.EdificioService;
import com.example.demo.services.EspacioComunService;
import com.example.demo.services.ReclamoService;
import com.example.demo.services.RespuestaReclamoService;
import com.example.demo.services.UsuarioService;

@CrossOrigin("*")
@RestController
@RequestMapping("/system")
public class ReclamoController {

	@Autowired
	DepartamentoService departamentoService;

	@Autowired
	EdificioService edificioService;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	EspacioComunService espacioComunService;

	@Autowired
	RespuestaReclamoService respuestaReclamoService;

	@Autowired
	ReclamoService reclamoService;

	@GetMapping("/reclamos")
	public ArrayList<Reclamo> obtenerReclamos() {
		return reclamoService.obtenerReclamos();
	}

	@GetMapping(path = "reclamos/{id}")
	public Optional<Reclamo> obtenerReclamoId(@PathVariable("id") Long id) {
		return this.reclamoService.obtenerPorId(id);
	}

	@PostMapping("/reclamos/deptos")
	public ResponseEntity<?> guardarReclamo(@RequestBody ReclamoDTO reclamoDTO) {
		// Verificar si el reclamo tiene un usuario, depto, y edificio
		if (reclamoDTO.getUsuario() != null && reclamoDTO.getDepartamento() != null
				&& reclamoDTO.getEdificio() != null) {

			Optional<Edificio> edificioOptional = edificioService.findById(reclamoDTO.getEdificio());
			Optional<Usuario> usuarioOptional = usuarioService.obtenerPorId(reclamoDTO.getUsuario());
			Optional<Departamento> deptoOptional = departamentoService.obtenerPorId(reclamoDTO.getDepartamento());

			// Verificación 1: El DEPTO pertenece al edificio y al usuario
			if (deptoOptional.isPresent() && edificioOptional.isPresent() && usuarioOptional.isPresent()) {
				Departamento departamento = deptoOptional.get();
				Edificio edificio = edificioOptional.get();
				Usuario usuario = usuarioOptional.get();

				if (departamento.getEdificio().getIdEdificio().equals(edificio.getIdEdificio())
	                    && (departamento.getPropietario().getId().equals(usuario.getId())
	                            || departamento.getInquilinos().contains(usuario))) {

					Reclamo reclamo = RDEPTOconvertToEntity(reclamoDTO);
					reclamo.setEdificio(edificio);
					reclamo.setUsuario(usuario);
					reclamo.setDepartamento(departamento);
					reclamoService.guardarReclamo(reclamo);
					usuario.agregarReclamo(reclamo);
					departamento.agregarReclamo(reclamo);
					departamentoService.guardarEspacioComun(departamento);
					usuarioService.guardarEspacioComun(usuario);
					ReclamoDTO nuevoReclamoDTO = RDEPTOconvertToDTO(reclamo);

					return new ResponseEntity<>(nuevoReclamoDTO, HttpStatus.CREATED);
				} else {
					return new ResponseEntity<>(
							"El edificio, el propietario o el usuario no pueden ser nulos o el usuario no está en el edificio",
							HttpStatus.BAD_REQUEST);
				}
			} else {
				// Agregar el retorno para este caso
				return new ResponseEntity<>(
						"Al menos uno de los elementos (usuario, departamento, espacio común) no fue encontrado",
						HttpStatus.BAD_REQUEST);
			}
		} else {
			// Manejar la situación de error si el usuario o el departamento es nulo
			return new ResponseEntity<>("El usuario y el departamento o espacio común deben ser proporcionados",
					HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/reclamos/espacioscomunes")
	public ResponseEntity<?> guardarReclamo2(@RequestBody ReclamoDTO reclamoDTO) {
		// Verificar si el reclamo tiene un usuario, depto, y edificio
		if (reclamoDTO.getEspacioComun() != null && reclamoDTO.getEdificio() != null) {

			Optional<Edificio> edificioOptional = edificioService.findById(reclamoDTO.getEdificio());
			Optional<EspacioComun> espacioOptional = espacioComunService.obtenerPorId((reclamoDTO.getEspacioComun()));
			Optional<Usuario> usuarioOptional = usuarioService.obtenerPorId(reclamoDTO.getUsuario());

			// Verificación 1: El DEPTO pertenece al edificio y al usuario
			if (espacioOptional.isPresent() && edificioOptional.isPresent() && usuarioOptional.isPresent()) {
				Edificio edificio = edificioOptional.get();
				EspacioComun espacio = espacioOptional.get();
				Usuario usuario = usuarioOptional.get();

				if (espacio.getEdificio().getIdEdificio().equals(edificio.getIdEdificio())
						&& usuario.getEdificio().getIdEdificio().equals(edificio.getIdEdificio())) {

					Reclamo reclamo = RESPconvertToEntity(reclamoDTO);
					reclamo.setEdificio(edificio);
					reclamo.setUsuario(usuario);
					reclamo.setEspacioComun(espacio);
					reclamoService.guardarReclamo(reclamo);
					usuario.agregarReclamo(reclamo);
					espacio.agregarReclamo(reclamo);
					espacioComunService.guardarEspacioComun(espacio);
					usuarioService.guardarEspacioComun(usuario);
					ReclamoDTO nuevoReclamoDTO = RESPconvertToDTO(reclamo);

					return new ResponseEntity<>(nuevoReclamoDTO, HttpStatus.CREATED);
				} else {
					return new ResponseEntity<>(
							"El edificio, el propietario o el usuario no pueden ser nulos o el usuario no está en el edificio",
							HttpStatus.BAD_REQUEST);
				}
			} else {
				// Agregar el retorno para este caso
				return new ResponseEntity<>(
						"Al menos uno de los elementos (usuario, departamento, espacio común) no fue encontrado",
						HttpStatus.BAD_REQUEST);
			}
		} else {
			// Manejar la situación de error si el usuario o el departamento es nulo
			return new ResponseEntity<>("El usuario y el departamento o espacio común deben ser proporcionados",
					HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping(path = "/reclamos/{id}/delete")
	public ResponseEntity<String> eliminarReclamo(@PathVariable("id") Long id) {
	    try {
	        reclamoService.eliminarReclamo(id);
	        String mensaje = "Reclamo eliminado exitosamente";
	        return ResponseEntity.ok(mensaje);
	    } catch (Exception e) {
	        // Manejar excepciones si es necesario y devolver un ResponseEntity con un mensaje de error
	        String mensajeError = "Error al eliminar el reclamo: " + e.getMessage();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensajeError);
	    }
	}
	@PutMapping("/reclamos/{id}")
	public ResponseEntity<Reclamo> actualizarReclamo(@PathVariable("id") Long id, @RequestBody Reclamo nuevoReclamo) {
	    try {
	        // Fetch the existing Reclamo object
	        Optional<Reclamo> existingReclamoOptional = reclamoService.obtenerPorId(id);

	        // If the Reclamo object doesn't exist, return an appropriate HTTP status code
	        if (!existingReclamoOptional.isPresent()) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }

	        // Use get() to retrieve the Reclamo object from the Optional
	        Reclamo existingReclamo = existingReclamoOptional.get();

	        // Update the existing Reclamo object with the new data (if not null in the JSON)
	        if (nuevoReclamo.getDescripcion() != null) {
	            existingReclamo.setDescripcion(nuevoReclamo.getDescripcion());
	        }
	        if (nuevoReclamo.getFoto() != null) {
	            existingReclamo.setFoto(nuevoReclamo.getFoto());
	        }
	        if (nuevoReclamo.getEstadoReclamo() != null) {
	            existingReclamo.setEstadoReclamo(nuevoReclamo.getEstadoReclamo());
	        }
	        // Update other fields as needed

	        // Save the updated Reclamo object to the database
	        reclamoService.guardarReclamo(existingReclamo);

	        // Return the updated Reclamo object
	        return new ResponseEntity<>(existingReclamo, HttpStatus.OK);
	    } catch (Exception e) {
	        // Log the exception and return an appropriate HTTP status code
	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@PostMapping("/reclamos/{id}/foto")
	public ResponseEntity<?> subirFoto(@PathVariable("id") Long id, @RequestParam("foto") MultipartFile foto) {
	   Optional<Reclamo> reclamoOptional = reclamoService.obtenerPorId(id);
	   if (reclamoOptional.isPresent()) {
	       Reclamo reclamo = reclamoOptional.get();
	       try {
	           reclamo.setFoto(foto.getBytes());
	           reclamoService.guardarReclamo(reclamo);
	           return new ResponseEntity<>(HttpStatus.OK);
	       } catch (IOException e) {
	           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	       }
	   } else {
	       return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	   }
	}
	
	@GetMapping("/reclamos/{id}/foto")
	public ResponseEntity<?> obtenerFoto(@PathVariable("id") Long id) {
	   Optional<Reclamo> reclamoOptional = reclamoService.obtenerPorId(id);
	   if (reclamoOptional.isPresent()) {
	       Reclamo reclamo = reclamoOptional.get();
	       return ResponseEntity.ok()
	               .contentType(MediaType.IMAGE_JPEG)
	               .body(reclamo.getFoto());
	   } else {
	       return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	   }
	}
	
	@DeleteMapping("/reclamos/{id}/foto")
	public ResponseEntity<?> borrarFoto(@PathVariable("id") Long id) {
	  Optional<Reclamo> reclamoOptional = reclamoService.obtenerPorId(id);
	  if (reclamoOptional.isPresent()) {
	      Reclamo reclamo = reclamoOptional.get();
	      reclamo.setFoto(null);
	      reclamoService.guardarReclamo(reclamo);
	      return new ResponseEntity<>(HttpStatus.OK);
	  } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	  }
	}

	public Reclamo RDEPTOconvertToEntity(ReclamoDTO reclamoDTO) {
		Reclamo reclamo = new Reclamo();
		reclamo.setDescripcion(reclamoDTO.getDescripcion());
		reclamo.setEstadoReclamo("Pendiente a responder");
		reclamo.setEspacioComun(null);
		reclamo.setRespuesta(null);

		Long idDepartamento = reclamoDTO.getDepartamento();

		Optional<Departamento> departamento = departamentoService.obtenerPorId(idDepartamento);

		reclamo.setDepartamento(departamento.get());

		Long usuarioId = reclamoDTO.getUsuario();

		Optional<Usuario> propietario = usuarioService.obtenerPorId(usuarioId);

		reclamo.setUsuario(propietario.get());

		Long edificioId = reclamoDTO.getEdificio();

		Optional<Edificio> edificio = edificioService.findById(edificioId);

		reclamo.setEdificio(edificio.get());
		
		return reclamo;
	}

	public Reclamo RESPconvertToEntity(ReclamoDTO reclamoDTO) {
		Reclamo reclamo = new Reclamo();
		reclamo.setDescripcion(reclamoDTO.getDescripcion());
		reclamo.setEstadoReclamo("Pendiente a responder");
		reclamo.setDepartamento(null);
		reclamo.setRespuesta(null);

		Long idEspacio = reclamoDTO.getEspacioComun();

		Optional<EspacioComun> espacio = espacioComunService.obtenerPorId(idEspacio);

		reclamo.setEspacioComun(espacio.get());

		Long usuarioId = reclamoDTO.getUsuario();

		Optional<Usuario> propietario = usuarioService.obtenerPorId(usuarioId);

		reclamo.setUsuario(propietario.get());

		Long edificioId = reclamoDTO.getEdificio();

		Optional<Edificio> edificio = edificioService.findById(edificioId);

		reclamo.setEdificio(edificio.get());

		return reclamo;
	}

	public ReclamoDTO RDEPTOconvertToDTO(Reclamo reclamo) {
		ReclamoDTO reclamoDTO = new ReclamoDTO();
		reclamoDTO.setDepartamento(reclamo.getDepartamento().getId());
		reclamoDTO.setDescripcion(reclamo.getDescripcion());
		reclamoDTO.setEdificio(reclamo.getEdificio().getIdEdificio());
		reclamoDTO.setEspacioComun(null);
		reclamoDTO.setRespuesta(null);
		reclamoDTO.setUsuario(reclamo.getUsuario().getId());

		return reclamoDTO;
	}

	public ReclamoDTO RESPconvertToDTO(Reclamo reclamo) {
		ReclamoDTO reclamoDTO = new ReclamoDTO();
		reclamoDTO.setDepartamento(null);
		reclamoDTO.setDescripcion(reclamo.getDescripcion());
		reclamoDTO.setEdificio(reclamo.getEdificio().getIdEdificio());
		reclamoDTO.setEspacioComun(reclamo.getEspacioComun().getId());
		reclamoDTO.setRespuesta(null);
		reclamoDTO.setUsuario(reclamo.getUsuario().getId());

		return reclamoDTO;
	}


}