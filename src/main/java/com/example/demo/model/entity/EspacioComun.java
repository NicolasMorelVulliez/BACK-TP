package com.example.demo.model.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "EspacioComun")
public class EspacioComun{

	public Long getIdEspacioComun() {
		return idEspacioComun;
	}

	public void setIdEspacioComun(Long idEspacioComun) {
		this.idEspacioComun = idEspacioComun;
	}

	public List<Reclamo> getReclamos() {
		return reclamos;
	}

	public void setReclamos(List<Reclamo> reclamos) {
		this.reclamos = reclamos;
	}

	public void setEdificio(Edificio edificio) {
		this.edificio = edificio;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@PrimaryKeyJoinColumn
	@Column(name="id", nullable=false)
	private Long idEspacioComun;

	@ManyToOne
	@JoinColumn(name = "edificio_id")
	@JsonIgnore
	private Edificio edificio;

	private int piso;
	private String nombre;
	private String descripcion;

	@OneToMany(mappedBy = "espacioComun", cascade = CascadeType.ALL)
	private List<Reclamo> reclamos;

	public EspacioComun( int piso, String nombre, String descripcion) {
		this.setPiso(piso);
		this.nombre = nombre;
		this.descripcion = descripcion;
	}

	public EspacioComun() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EspacioComun(Long id, String desc, String nombre, int piso, Edificio edificio2) {
		// TODO Auto-generated constructor stub
		this.idEspacioComun=id;
		this.descripcion=desc;
		this.nombre=nombre;
		this.piso=piso;
		this.edificio=edificio2;
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

	public int getPiso() {
		return piso;
	}

	public void setPiso(int piso) {
		this.piso = piso;
	}

	public Long getId() {
		return idEspacioComun;
	}

	public void agregarReclamo(Reclamo reclamo) {
        reclamos.add(reclamo);
    }

	public Edificio getEdificio() {
		// TODO Auto-generated method stub
		return this.edificio;
	}


}
