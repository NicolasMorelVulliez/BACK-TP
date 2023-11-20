package com.example.demo.model.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "Usuario", uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }) })
public class Usuario implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@PrimaryKeyJoinColumn
	@Column(name = "id", nullable = false)
	private Long id;
	@Basic
	@Column(nullable = false)
	private String username;
	private String nombre;
	private String apellido;
	private int dni;
	private String contrasena;
	private int pinEdificio;
	@Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
	private Role rol;

	@ManyToMany
	@JoinTable(name = "usuario_departamento_propiedades", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "departamento_id"))
	@JsonIgnore
	private List<Departamento> propiedades = new ArrayList<>();

	@ManyToMany
	@JoinTable(name = "usuario_departamento_alquileres", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "departamento_id"))
	@JsonIgnore
	private List<Departamento> alquileres = new ArrayList<>();

	public List<Departamento> getAlquileres() {
		return alquileres;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setAlquileres(List<Departamento> alquileres) {
		this.alquileres = alquileres;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Reclamo> reclamos = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "edificio_id", referencedColumnName = "id")
	@JsonIgnore
	private Edificio edificio;

	public Usuario() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Role getRol() {
		return rol;
	}

	public void setRol(Role rol) {
		this.rol = rol;
	}

	public Long getIdUsuario() {
		return id;
	}

	public List<Departamento> getPropiedades() {
		return propiedades;
	}

	public void setPropiedades(List<Departamento> propiedades) {
		this.propiedades = propiedades;
	}

	public List<Reclamo> getReclamos() {
		return reclamos;
	}

	public void setReclamos(List<Reclamo> reclamos) {
		this.reclamos = reclamos;
	}

	public Edificio getEdificio() {
		return edificio;
	}

	public void setEdificio(Edificio edificio) {
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

	public Long getId() {
		return id;
	}

	public void agregarPropiedad(Departamento dept) {
		propiedades.add(dept);
	}

	public void agregarReclamo(Reclamo reclamo) {
		reclamos.add(reclamo);
	}

	public void setIdUsuario(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", dni=" + dni + ", contrasena="
				+ contrasena + ", propiedades=" + propiedades + ", alquileres=" + alquileres + ", reclamos=" + reclamos
				+ ", edificio=" + edificio + "]";
	}

	public Usuario(Long id, String nombre, String apellido, int dni, String contrasena, List<Departamento> propiedades,
			List<Departamento> alquileres, List<Reclamo> reclamos, Edificio edificio) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.contrasena = contrasena;
		this.propiedades = propiedades;
		this.alquileres = alquileres;
		this.reclamos = reclamos;
		this.edificio = edificio;
	}

	public int getPinEdificio() {
		return pinEdificio;
	}

	public void setPinEdificio(int pinEdificio) {
		this.pinEdificio = pinEdificio;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(rol.getAuthority()));
    }
	
	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;

	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;

	}

	@Override
	public boolean isEnabled() {
		return true;

	}

}
