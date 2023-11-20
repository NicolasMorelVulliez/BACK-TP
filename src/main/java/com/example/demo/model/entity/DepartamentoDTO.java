package com.example.demo.model.entity;

import java.util.List;

public class DepartamentoDTO {

	private int piso;
	private boolean alquiler;
	private String unidad;
	private Long propietario;
	private Long edificio;
	private List<UsuarioDTO> inquilinos = null;


	public List<UsuarioDTO> getInquilinos() {
		return inquilinos;
	}
	public void setInquilinos(List<UsuarioDTO> inquilinos) {
		this.inquilinos = null;
	}
	public int getPiso() {
		return piso;
	}
	public void setPiso(int piso) {
		this.piso = piso;
	}
	public boolean isAlquiler() {
		return alquiler;
	}
	public void setAlquiler(boolean alquiler) {
		this.alquiler = alquiler;
	}
	public String getUnidad() {
		return unidad;
	}
	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}
	public Long getPropietario() {
		return propietario;
	}
	public void setPropietario(Long propietario) {
		this.propietario = propietario;
	}
	public Long getEdificio() {
		return edificio;
	}
	public void setEdificio(Long edificio) {
		this.edificio = edificio;
	}

}
