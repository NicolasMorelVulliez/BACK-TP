package com.example.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "Reclamo")
public class Reclamo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@PrimaryKeyJoinColumn
	@Column(name="id", nullable=false)
	private Long idReclamo;

	@ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
	private Usuario usuario;

	@ManyToOne
    @JoinColumn(name = "edificio_id", referencedColumnName = "id")
	@JsonIgnore
	private Edificio edificio;

	@ManyToOne(optional = true)
	@JoinColumn(name = "id_departamento", referencedColumnName = "id")
	@JsonIgnore
	private Departamento departamento;

	@ManyToOne(optional = true)
	@JoinColumn(name = "idEspacioComun", referencedColumnName = "id")
	@JsonIgnore
	private EspacioComun espacioComun;

	private String descripcion;
	
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] foto;
	
	private String estadoReclamo;

	@OneToOne(mappedBy = "reclamo", cascade = CascadeType.ALL)
	private RespuestaReclamo respuesta;

	public Reclamo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Reclamo (Usuario usuario, Edificio edificio, Departamento departamento, EspacioComun esp,String descripcion, byte[] foto) {
		super();

		this.usuario = usuario;
		this.edificio = edificio;
		this.departamento = departamento;
		this.espacioComun = esp;
		this.descripcion = descripcion;
		this.foto = foto;
	}

	public Long getIdReclamo() {
		return idReclamo;
	}

	public void setIdReclamo(Long idReclamo) {
		this.idReclamo = idReclamo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Edificio getEdificio() {
		return edificio;
	}

	public void setEdificio(Edificio edificio) {
		this.edificio = edificio;
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
	
	public RespuestaReclamo getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(RespuestaReclamo respuesta) {
		this.respuesta = respuesta;
	}

	public String getEstadoReclamo() {
		return estadoReclamo;
	}

	public void setEstadoReclamo(String estadoReclamo) {
		this.estadoReclamo = estadoReclamo;
	}

	public Departamento getDepartamento() {
		return this.departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public EspacioComun getEspacioComun() {
		return this.espacioComun;
	}

	public void setEspacioComun(EspacioComun espacioComun) {
		this.espacioComun = espacioComun;
	}



}