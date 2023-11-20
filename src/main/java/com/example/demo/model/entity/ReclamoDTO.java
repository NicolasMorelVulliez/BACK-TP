package com.example.demo.model.entity;

public class ReclamoDTO {

	private Long usuario;
	private Long edificio;
	private Long departamento;
	private Long espacioComun;
	private String descripcion;
	private String foto;
	private Long respuesta;


	public ReclamoDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getUsuario() {
		return usuario;
	}

	public void setUsuario(Long usuario) {
		this.usuario = usuario;
	}

	public Long getEdificio() {
		return edificio;
	}

	public void setEdificio(Long edificio) {
		this.edificio = edificio;
	}

	public Long getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Long departamento) {
		this.departamento = departamento;
	}

	public Long getEspacioComun() {
		return espacioComun;
	}

	public void setEspacioComun(Long espacioComun) {
		this.espacioComun = espacioComun;
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

	public Long getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(Long respuesta) {
		this.respuesta = respuesta;
	}



}
