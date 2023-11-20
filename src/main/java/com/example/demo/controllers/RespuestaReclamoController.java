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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.entity.Reclamo;
import com.example.demo.model.entity.RespuestaReclamo;
import com.example.demo.model.entity.RespuestaReclamoDTO;
import com.example.demo.services.DepartamentoService;
import com.example.demo.services.EdificioService;
import com.example.demo.services.EspacioComunService;
import com.example.demo.services.ReclamoService;
import com.example.demo.services.RespuestaReclamoService;
import com.example.demo.services.UsuarioService;

@CrossOrigin("*")
@RestController
@RequestMapping("/system")
public class RespuestaReclamoController {

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

	@Autowired
	RespuestaReclamoService respuestareclamoService;

	@GetMapping("/respuestareclamos")
	public ArrayList<RespuestaReclamo> obtenerRespuestaReclamos() {
		return respuestareclamoService.obtenerRespuestaReclamos();
	}

	@GetMapping(path = "respuestareclamos/{id}")
	public Optional<RespuestaReclamo> obtenerRespuestaReclamoId(@PathVariable("id") Long id) {
		return this.respuestareclamoService.obtenerPorId(id);
	}

	@PostMapping("/respuestareclamos")
	public ResponseEntity<?> guardarRespuestaReclamo(@RequestBody RespuestaReclamoDTO respuestareclamoDTO) {
		// Verificar si el reclamo tiene un identificador válido
		if (respuestareclamoDTO.getReclamo() != null) {
			Optional<Reclamo> reclamoOptional = reclamoService.obtenerPorId(respuestareclamoDTO.getReclamo());

			// Verificar si el reclamo existe
			if (reclamoOptional.isPresent()) {
				RespuestaReclamo respuestareclamo = new RespuestaReclamo();
				respuestareclamo.setDescripcion(respuestareclamoDTO.getDescripcion());
				respuestareclamo.setReclamo(reclamoOptional.get());
				respuestaReclamoService.guardarRespuestaReclamo(respuestareclamo);
				reclamoOptional.get().setRespuesta(respuestareclamo);
				reclamoOptional.get().setEstadoReclamo("Respondido");
				reclamoService.guardarReclamo(reclamoOptional.get());

				RespuestaReclamoDTO nuevoRespuestaReclamoDTO = convertToDTO(respuestareclamo);

				return new ResponseEntity<>(nuevoRespuestaReclamoDTO, HttpStatus.CREATED);
			} else {
				// Mensaje de error si el reclamo no existe
				return new ResponseEntity<>("El reclamo no fue encontrado", HttpStatus.BAD_REQUEST);
			}
		} else {
			// Mensaje de error si el identificador del reclamo es nulo
			return new ResponseEntity<>("El identificador del reclamo no puede ser nulo", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/respuestareclamos/{id}/foto")
	public ResponseEntity<?> subirFoto(@PathVariable("id") Long id, @RequestParam("foto") MultipartFile foto) {
		Optional<RespuestaReclamo> respuestareclamoOptional = respuestareclamoService.obtenerPorId(id);
		if (respuestareclamoOptional.isPresent()) {
			RespuestaReclamo respuestareclamo = respuestareclamoOptional.get();
			try {
				respuestareclamo.setFoto(foto.getBytes());
				respuestareclamoService.guardarRespuestaReclamo(respuestareclamo);
				return new ResponseEntity<>(HttpStatus.OK);
			} catch (IOException e) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/respuestareclamos/{id}/foto")
	public ResponseEntity<?> obtenerFotoRespuesta(@PathVariable("id") Long id) {
		Optional<RespuestaReclamo> respuestareclamoOptional = respuestareclamoService.obtenerPorId(id);
		if (respuestareclamoOptional.isPresent()) {
			RespuestaReclamo respuestareclamo = respuestareclamoOptional.get();
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(respuestareclamo.getFoto());
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/respuestareclamos/{id}/foto")
	public ResponseEntity<?> borrarFotoRespuesta(@PathVariable("id") Long id) {
		Optional<RespuestaReclamo> respuestareclamoOptional = respuestareclamoService.obtenerPorId(id);
		if (respuestareclamoOptional.isPresent()) {
			RespuestaReclamo respuestareclamo = respuestareclamoOptional.get();
			respuestareclamo.setFoto(null);
			respuestareclamoService.guardarRespuestaReclamo(respuestareclamo);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	public RespuestaReclamoDTO convertToDTO(RespuestaReclamo respuestareclamo) {
		RespuestaReclamoDTO respuestareclamoDTO = new RespuestaReclamoDTO();
		respuestareclamoDTO.setDescripcion(respuestareclamo.getDescripcion());
		respuestareclamoDTO.setReclamo(respuestareclamo.getReclamo().getIdReclamo());

		return respuestareclamoDTO;
	}

}