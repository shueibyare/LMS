package com.example.LibraryManagementSystem.Controller;

import com.example.LibraryManagementSystem.DTO.LoginRequestDTO;
import com.example.LibraryManagementSystem.DTO.LoginResponseDTO;
import com.example.LibraryManagementSystem.DTO.RegisterRequestDTO;
import com.example.LibraryManagementSystem.Entity.User;
import com.example.LibraryManagementSystem.Service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/registeradminuser")
    public ResponseEntity<User> registerAdminUser(@RequestBody RegisterRequestDTO registerRequestDTO){
        return ResponseEntity.ok(authenticationService.registerAdminUser(registerRequestDTO));
    }


}
