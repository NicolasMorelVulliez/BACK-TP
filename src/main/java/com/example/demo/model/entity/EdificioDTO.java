package com.example.demo.model.entity;

public class EdificioDTO {

	private String calle;
	private int altura;
	private String nombreImagen;
	private int pinSeguridad;

	public EdificioDTO(String calle, int altura, String nombreImagen, int pinSeguridad) {
		this.calle = calle;
		this.altura = altura;
		this.nombreImagen = nombreImagen;
		this.pinSeguridad = pinSeguridad;

	}

	public String getNombreImagen() {
		return nombreImagen;
	}

	public void setNombreImagen(String nombreImagen) {
		this.nombreImagen = nombreImagen;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public int getAltura() {
		return altura;
	}

	public void setAltura(int altura) {
		this.altura = altura;
	}

	public int getPinSeguridad() {
		return pinSeguridad;
	}

	public void setPinSeguridad(int pinSeguridad) {
		this.pinSeguridad = pinSeguridad;
	}

}
