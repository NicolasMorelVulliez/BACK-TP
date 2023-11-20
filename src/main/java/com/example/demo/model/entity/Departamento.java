package com.example.demo.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "Departamento")
public class Departamento{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@PrimaryKeyJoinColumn
	@Column(name="id", nullable=false)
	private Long id;

	private int piso;
	private String unidad;
	private boolean alquiler;

	@ManyToOne
	@JoinColumn(name = "usuario_id", referencedColumnName = "id")
	private Usuario propietario;

	@ManyToOne
	@JoinColumn(name = "edificio_id", referencedColumnName = "id")
	@JsonIgnore
    private Edificio edificio;

	@ManyToMany(mappedBy = "propiedades")
	private List<Usuario> propietarios = new ArrayList<>();

	@ManyToMany(mappedBy = "alquileres")
	private List<Usuario> inquilinos = new ArrayList<>();

	@OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL)
	private List<Reclamo> reclamos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getPiso() {
		return piso;
	}

	public void setPiso(int piso) {
		this.piso = piso;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public boolean isAlquiler() {
		return alquiler;
	}

	public void setAlquiler(boolean alquiler) {
		this.alquiler = alquiler;
	}

	public Usuario getPropietario() {
		return propietario;
	}

	public void setPropietario(Usuario propietario) {
		this.propietario = propietario;
	}

	public Edificio getEdificio() {
		return edificio;
	}

	public void setEdificio(Edificio edificio) {
		this.edificio = edificio;
	}

	public List<Usuario> getInquilinos() {
		return inquilinos;
	}

	public void setInquilinos(List<Usuario> inquilinos) {
		this.inquilinos = null;
	}

	public List<Reclamo> getReclamos() {
		return reclamos;
	}

	public void setReclamos(List<Reclamo> reclamos) {
		this.reclamos = reclamos;
	}

	public void agregarInquilino(Usuario inquilino) {
        if (inquilino != null && !inquilinos.contains(inquilino)) {
            inquilinos.add(inquilino);
            inquilino.getAlquileres().add(this);
        }
    }

	public void agregarReclamo(Reclamo reclamo) {
        reclamos.add(reclamo);
    }

	@Override
	public String toString() {
		return "Departamento [id=" + id + ", piso=" + piso + ", unidad=" + unidad + ", alquiler=" + alquiler
				+ ", propietario=" + propietario + ", edificio=" + edificio + ", inquilinos=" + inquilinos
				+ ", reclamos=" + reclamos + "]";
	}

}