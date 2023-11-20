package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.model.entity.Role;
import com.example.demo.model.entity.Usuario;
import com.example.demo.services.UsuarioService;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public void run(String... args) throws Exception {
        String nombreUsuarioDios = "dios";
        
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Verificar si el usuario DIOS ya existe
        if (!usuarioService.existeUsuario(nombreUsuarioDios)) {
            Usuario dios = new Usuario();
            dios.setUsername(nombreUsuarioDios);
            dios.setContrasena(passwordEncoder.encode("PIBE")); // Asegúrate de usar un método seguro para almacenar contraseñas
            dios.setRol(Role.ROLE_DIOS);

            usuarioService.guardarEspacioComun(dios);

            System.out.println(usuarioService.obtenerUsuarios());
        } else {
            System.out.println("El usuario DIOS ya existe.");
        }
    }
}
