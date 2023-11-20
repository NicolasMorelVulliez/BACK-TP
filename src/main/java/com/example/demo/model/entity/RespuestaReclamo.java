package com.example.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "RespuestaReclamo")
public class RespuestaReclamo {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@PrimaryKeyJoinColumn
	@Column(name="id", nullable=false)
	private Long idRespuesta;

	@OneToOne
	@JoinColumn(name = "reclamo_id", referencedColumnName = "id")
	@JsonIgnore
	private Reclamo reclamo;

	private String descripcion;
	
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] foto;

	public RespuestaReclamo(Long idRespuesta, Reclamo reclamo, String descripcion, byte[] foto) {
		super();
		this.idRespuesta = idRespuesta;
		this.reclamo = reclamo;
		this.descripcion = descripcion;
		this.foto = foto;
	}

	public RespuestaReclamo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getIdRespuesta() {
		return idRespuesta;
	}
	public void setIdRespuesta(Long idRespuesta) {
		this.idRespuesta = idRespuesta;
	}
	public Reclamo getReclamo() {
		return reclamo;
	}
	public void setReclamo(Reclamo reclamo) {
		this.reclamo = reclamo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

}
