package com.example.demo.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Reclamo;
import com.example.demo.model.repos.DAOReclamoCRUD;

@Service
public class ReclamoService {

	@Autowired
	private DAOReclamoCRUD daoRepository;

	public ArrayList<Reclamo> obtenerReclamos() {
		return (ArrayList<Reclamo>) daoRepository.findAll();
	}

	public void guardarReclamo(Reclamo reclamo) {
		if (reclamo.getDepartamento() == null && reclamo.getEspacioComun() == null) {
			throw new IllegalArgumentException("Debe proporcionar al menos un Departamento o un EspacioComun");
		}
		daoRepository.save(reclamo);
	}

	public Optional<Reclamo> obtenerPorId(Long id) {
		return daoRepository.findById(id);
	}

	public boolean eliminarReclamo(Long id) {
		try {
			daoRepository.deleteById(id);
			return true;
		} catch (Exception err) {
			return false;
		}
	}
}