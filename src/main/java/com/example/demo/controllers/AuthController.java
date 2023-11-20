package com.example.demo.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.entity.Edificio;
import com.example.demo.model.entity.Role;
import com.example.demo.model.entity.Usuario;
import com.example.demo.model.entity.UsuarioDTO;
import com.example.demo.services.EdificioService;
import com.example.demo.services.UsuarioService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
public class AuthController {

    private final int EXPIRATION_TIME_IN_MIN = 40;

    @Autowired
    private UsuarioService userLoginService;
    
    @Autowired
    private EdificioService edificioService;

    @Autowired
    private SecretKey secretKey;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioDTO credentials) {
        try {
            Usuario usuario = userLoginService.encontrarUsuarioPorCredenciales(credentials.getUsername(), credentials.getContrasena());

            // Obtener roles del usuario
            List<?> roles = usuario.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

            String token = Jwts.builder()
                    .setSubject(credentials.getUsername())
                    .claim("roles", roles)  // Usar la variable roles en lugar de authorities
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_IN_MIN * 60 * 1000))
                    .signWith(secretKey, SignatureAlgorithm.HS256)
                    .compact();

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", token);
            responseBody.put("roles", roles);
            
            System.out.println(token);
            return ResponseEntity.ok().body(responseBody);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales Inválidas");
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsuarioDTO credentials)
    {
    	if(!userLoginService.existeUsuario(credentials.getUsername()))
    	{
    		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    		credentials.setContrasena(passwordEncoder.encode(credentials.getContrasena()));
    		Edificio edificio = edificioService.buscarPorPinSeguridad(credentials.getPinEdificio());
    		userLoginService.guardarEspacioComun(convertToEntity(credentials, edificio));
    		return new ResponseEntity<>(credentials, HttpStatus.CREATED);
    	}
    	else {
			return new ResponseEntity<>("Nombre de usuario no disponible", HttpStatus.NOT_ACCEPTABLE);
		}
    		
    }
    
    public Usuario convertToEntity(UsuarioDTO usuarioDTO, Edificio edificio) {
	    Usuario usuario = new Usuario();
	    usuario.setNombre(usuarioDTO.getNombre());
	    usuario.setApellido(usuarioDTO.getApellido());
	    usuario.setDni(usuarioDTO.getDni());
	    usuario.setContrasena(usuarioDTO.getContrasena());
	    usuario.setUsername(usuarioDTO.getUsername());
	    usuario.setRol(Role.ROLE_USER);
	    usuario.setPinEdificio(usuarioDTO.getPinEdificio());
	    usuario.setEdificio(edificio);


	    return usuario;
	}
}