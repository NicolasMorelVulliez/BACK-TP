package com.example.demo.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.entity.RespuestaReclamo;
import com.example.demo.model.repos.DAORespuestaReclamoCRUD;

@Service
public class RespuestaReclamoService {

	@Autowired
	private DAORespuestaReclamoCRUD daoRepository;

	public ArrayList<RespuestaReclamo> obtenerRespuestaReclamos() {
		return (ArrayList<RespuestaReclamo>) daoRepository.findAll();
	}

	public void guardarRespuestaReclamo(RespuestaReclamo RespuestaReclamo) {
		daoRepository.save(RespuestaReclamo);
	}

	public Optional<RespuestaReclamo> obtenerPorId(Long id) {
		return daoRepository.findById(id);
	}

	public RespuestaReclamo eliminarRespuestaReclamo(Long id) {
		   try {
		       RespuestaReclamo respuestaReclamo = daoRepository.findById(id).orElseThrow(() -> new Exception("Respuesta no encontrada"));
		       daoRepository.delete(respuestaReclamo);
		       return respuestaReclamo;
		   } catch (Exception err) {
		       return null;
		   }
		}

}