package com.example.demo.model.entity;

public class EspacioComunDTO {

	private int piso;
	private String nombre;
	private String descripcion;
	private Long edificio;

	public int getPiso() {
		return piso;
	}
	public void setPiso(int piso) {
		this.piso = piso;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Long getEdificio() {
		return edificio;
	}
	public void setEdificio(Long edificio) {
		this.edificio = edificio;
	}


}
