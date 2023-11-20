package com.example.demo.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Edificio;
import com.example.demo.model.entity.Usuario;
import com.example.demo.model.repos.DAOEdificioCRUD;

@Service
public class EdificioService {

	@Autowired
	private DAOEdificioCRUD daoRepository;

	public ArrayList<Edificio> obtenerEdificios() {
		return (ArrayList<Edificio>) daoRepository.findAll();
	}

	public void guardarEspacioComun(Edificio Edificio) {
		daoRepository.save(Edificio);
	}

	public Optional<Edificio> findById(Long id) {
	    Optional<Edificio> optionalEdificio = daoRepository.findById(id);
	    return optionalEdificio; // Devuelve null si no se encuentra el edificio
	}

	public boolean eliminarEdificio(Long id) {
		try {
			daoRepository.deleteById(id);
			return true;
		} catch (Exception err) {
			return false;
		}
	}

	public boolean usuarioEnEdificio(Long idEdificio, Long idUsuario) {
		Optional<Edificio> optionalEdificio = daoRepository.findById(idEdificio);
		if (optionalEdificio.isPresent()) {
			Edificio edificio = optionalEdificio.get();
			for (Usuario usuario : edificio.getUsuarios()) {
				if (usuario.getId().equals(idUsuario)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public Edificio buscarPorPinSeguridad(int pinSeguridad) {
        Optional<Edificio> edificioOptional = daoRepository.findByPinSeguridad(pinSeguridad);
        return edificioOptional.orElse(null);
    }

}