package com.example.demo.model.entity;

import java.util.List;

public class UsuarioDTO {

	private String nombre;
	private String apellido;
	private String username;
	private Role Rol;
	private int dni;
	private String contrasena;
	private List<DepartamentoDTO> propiedades;
	private List<ReclamoDTO> reclamos;
	private Long edificio;
	private int pinEdificio;

	@Override
	public String toString() {
		return "UsuarioDTO [nombre=" + nombre + ", apellido=" + apellido + ", username=" + username + ", dni=" + dni
				+ ", contrasena=" + contrasena + ", propiedades=" + propiedades + ", reclamos=" + reclamos
				+ ", edificio=" + edificio + "]";
	}

	public Role getRol() {
		return Rol;
	}

	public void setRol(Role rol) {
		Rol = rol;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEdificio(Long edificio) {
		this.edificio = edificio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public int getDni() {
		return dni;
	}

	public void setDni(int dni) {
		this.dni = dni;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public List<DepartamentoDTO> getPropiedades() {
		return propiedades;
	}

	public void setPropiedades(List<DepartamentoDTO> propiedades) {
		this.propiedades = propiedades;
	}

	public List<ReclamoDTO> getReclamos() {
		return reclamos;
	}

	public void setReclamos(List<ReclamoDTO> reclamos) {
		this.reclamos = reclamos;
	}

	public Long getEdificio() {
		return edificio;
	}

	public int getPinEdificio() {
		return pinEdificio;
	}

	public void setPinEdificio(int pinEdificio) {
		this.pinEdificio = pinEdificio;
	}

}