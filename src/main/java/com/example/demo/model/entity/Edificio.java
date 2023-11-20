package com.example.demo.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "Edificio")
public class Edificio {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@PrimaryKeyJoinColumn
	@Column(name="id", nullable=false)
	private Long id;

	private String calle;
	private String nombreImagen;
	private int altura;
	private int pinSeguridad;

	public String getNombreImagen() {
		return nombreImagen;
	}

	public void setNombreImagen(String nombreImagen) {
		this.nombreImagen = nombreImagen;
	}

	@OneToMany(mappedBy = "edificio", cascade = CascadeType.ALL)
	private List<EspacioComun> espaciosComunes;

	@OneToMany(mappedBy = "edificio", cascade = CascadeType.ALL)
	private List<Departamento> departamentos;

	@OneToMany(mappedBy = "edificio", cascade = CascadeType.ALL)
	private List<Usuario> usuarios = new ArrayList<>();


	public Edificio() {
		super();
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}


	public void setDepartamentos(List<Departamento> departamentos) {
		this.departamentos = departamentos;
	}

	public void agregarDepartamento(Departamento departamento) {
        this.departamentos.add(departamento);
        departamento.setEdificio(this);
    }

	public Edificio(String calle, int altura) {
		super();
		this.calle = calle;
		this.altura = altura;
		espaciosComunes = new ArrayList<>();
		departamentos = new ArrayList<>();

	}

	public Long getIdEdificio() {
		return id;
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
	public List<EspacioComun> getEspaciosComunes() {
		return espaciosComunes;
	}
	public void setEspaciosComunes(List<EspacioComun> espaciosComunes) {
		this.espaciosComunes = espaciosComunes;
	}
	@Override
	public String toString() {
		return "Edificio [id = " + id + ", Calle = " + calle + ", Altura = " + altura + ", Departamentos = "
				+ getUnidadesString() + ", Altura = " + altura + "]";
	}
	private ArrayList<String> getUnidadesString() {
		ArrayList<String> unidades = new ArrayList<>();
		for (Departamento u : departamentos) {
			unidades.add(u.getPiso() + " " + u.getUnidad());
		}
		return unidades;
	}

	public List<Departamento> getDepartamentos(){
		return this.departamentos;
	}

	public void addUsuarios(Usuario usuario) {
		this.usuarios.add(usuario);
	}
	public void removeUsuarios(Usuario usuario) {
		this.usuarios.remove(usuario);
	}

	public void setIdEdificio(Long int1) {
		// TODO Auto-generated method stub
		this.id=int1;

	}

	public int getPinSeguridad() {
		return pinSeguridad;
	}

	public void setPinSeguridad(int pinSeguridad) {
		this.pinSeguridad = pinSeguridad;
	}
	
	




}
