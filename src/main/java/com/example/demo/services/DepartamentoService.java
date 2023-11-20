package com.example.demo.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Departamento;
import com.example.demo.model.repos.DAODeptoCRUD;

@Service
public class DepartamentoService {

	@Autowired
	private DAODeptoCRUD daoRepository;

	public ArrayList<Departamento> obtenerDepartamentos() {
		return (ArrayList<Departamento>) daoRepository.findAll();
	}

	public void guardarEspacioComun(Departamento Departamento) {
		daoRepository.save(Departamento);
	}

	public Optional<Departamento> obtenerPorId(Long id) {
		return daoRepository.findById(id);
	}

	public boolean eliminarDepartamento(Long id) {
		try {
			daoRepository.deleteById(id);
			return true;
		} catch (Exception err) {
			return false;
		}
	}
}