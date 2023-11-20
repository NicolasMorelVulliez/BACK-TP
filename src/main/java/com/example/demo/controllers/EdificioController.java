package com.example.demo.controllers;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.entity.Edificio;
import com.example.demo.model.entity.EdificioDTO;
import com.example.demo.model.entity.Role;
import com.example.demo.model.entity.Usuario;
import com.example.demo.services.EdificioService;


@RestController
@CrossOrigin
@RequestMapping("/system")
public class EdificioController {
   @Autowired
   EdificioService EdificioService;

   @GetMapping("/edificios")
   public ArrayList<Edificio> obtenerEdificios() {
       return EdificioService.obtenerEdificios();
   }

   @GetMapping(path = "edificios/{id}")
   public Optional<Edificio> obtenerEdificioId(@PathVariable("id") Long id) {
       return this.EdificioService.findById(id);
   }

   
   @PostMapping("/edificios")
   public ResponseEntity<EdificioDTO> guardarEdificio(@RequestBody EdificioDTO edificioDTO) {
	    Edificio edificio = convertToEntity(edificioDTO);
	    
	    String contra = generarContrasenaAleatoria(6);

	    // Guardar el edificio
	    EdificioService.guardarEspacioComun(edificio);

	    // Crear un usuario de mantenimiento
	    Usuario mantenimiento = crearUsuarioMantenimiento(edificio, contra);

	    // Agregar el usuario de mantenimiento al edificio
	    edificio.addUsuarios(mantenimiento);
	    EdificioService.guardarEspacioComun(edificio);
	    System.out.println("Contrasena Mantenimiento para Edificio " + edificio.getPinSeguridad()+ " = " + contra);

	    EdificioDTO nuevoEdificioDTO = convertToDTO(edificio);

	    return new ResponseEntity<>(nuevoEdificioDTO, HttpStatus.CREATED);
	}

	private Usuario crearUsuarioMantenimiento(Edificio edificio, String contra) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    Usuario usuarioMantenimiento = new Usuario();
	    usuarioMantenimiento.setNombre("Mantenimiento");
	    usuarioMantenimiento.setRol(Role.ROLE_MANTENIMIENTO);
	    usuarioMantenimiento.setContrasena(passwordEncoder.encode(contra));  // Implementa la lógica para generar una contraseña aleatoria
	    usuarioMantenimiento.setUsername("mantenimiento" + edificio.getPinSeguridad());
	    usuarioMantenimiento.setEdificio(edificio);

	    return usuarioMantenimiento;
	}


   @DeleteMapping(path = "/edificios/{id}")
   public ResponseEntity<?> eliminarEdificio(@PathVariable("id") Long id) {
       try {
           EdificioService.eliminarEdificio(id);
           return new ResponseEntity<>("Edificio eliminado correctamente", HttpStatus.NO_CONTENT);
       } catch (Exception e) {
           // Log the exception and return an appropriate HTTP status code
           return new ResponseEntity<>("Error al eliminar el edificio", HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }

   @PutMapping("/edificios/{id}")
   public ResponseEntity<Edificio> actualizarEdificio(@PathVariable("id") Long id, @RequestBody Edificio nuevoEdificio) {
       try {
           Optional<Edificio> existingEdificio = EdificioService.findById(id);

           if (!existingEdificio.isPresent()) {
               return new ResponseEntity<>(HttpStatus.NOT_FOUND);
           }

           Edificio edificioExistente = existingEdificio.get();

           if (nuevoEdificio.getCalle() != null) {
               edificioExistente.setCalle(nuevoEdificio.getCalle());
           }
           
           if (nuevoEdificio.getNombreImagen() != null) {
               edificioExistente.setCalle(nuevoEdificio.getNombreImagen());
           }
           
           if (nuevoEdificio.getAltura() != 0) {
               edificioExistente.setAltura(nuevoEdificio.getAltura());
           }
           
           if (nuevoEdificio.getPinSeguridad() != 0) {
        	   edificioExistente.setPinSeguridad(nuevoEdificio.getPinSeguridad());
           }

           EdificioService.guardarEspacioComun(edificioExistente);

           // Return el edificio existente actualizado
           return new ResponseEntity<>(edificioExistente, HttpStatus.OK);
       } catch (Exception e) {
           // Log the exception and return an appropriate HTTP status code
           return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
       }
   }


   private Edificio convertToEntity(EdificioDTO edificioDTO) {
       Edificio edificio = new Edificio();
       edificio.setAltura(edificioDTO.getAltura());
       edificio.setCalle(edificioDTO.getCalle());
       edificio.setNombreImagen(edificioDTO.getNombreImagen());
       edificio.setPinSeguridad(edificioDTO.getPinSeguridad());
       return edificio;
   }

   private EdificioDTO convertToDTO(Edificio edificio) {
       EdificioDTO edificioDTO = new EdificioDTO(edificio.getCalle(), edificio.getAltura(), edificio.getNombreImagen(), edificio.getPinSeguridad());
       return edificioDTO;
   }
   
   private static final String CARACTERES_PERMITIDOS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";

   public static String generarContrasenaAleatoria(int longitud) {
       SecureRandom random = new SecureRandom();
       StringBuilder contrasena = new StringBuilder();

       for (int i = 0; i < longitud; i++) {
           int index = random.nextInt(CARACTERES_PERMITIDOS.length());
           contrasena.append(CARACTERES_PERMITIDOS.charAt(index));
       }

       return contrasena.toString();
   }
   
}
