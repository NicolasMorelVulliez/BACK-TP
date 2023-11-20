package com.example.demo.model.entity;

public class RespuestaReclamoDTO {

	public RespuestaReclamoDTO() {
		super();
	}

	private Long reclamo;
	private String descripcion;
	private String foto;

	public Long getReclamo() {
		return reclamo;
	}

	public void setReclamo(Long reclamo) {
		this.reclamo = reclamo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	
}
