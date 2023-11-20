package com.example.demo.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.entity.EspacioComun;
import com.example.demo.model.repos.DAOEspacioComunCRUD;

@Service
public class EspacioComunService {

	@Autowired
	private DAOEspacioComunCRUD daoRepository;

	public ArrayList<EspacioComun> obtenerEspacioComunes() {
		return (ArrayList<EspacioComun>) daoRepository.findAll();
	}

	public void guardarEspacioComun(EspacioComun EspacioComun) {
		daoRepository.save(EspacioComun);
	}

	public Optional<EspacioComun> obtenerPorId(Long id) {
		return daoRepository.findById(id);
	}

	public boolean eliminarEspacioComun(Long id) {
		try {
			daoRepository.deleteById(id);
			return true;
		} catch (Exception err) {
			return false;
		}
	}
}