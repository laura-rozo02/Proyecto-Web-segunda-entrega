package com.example.User.controller;


import com.example.User.domain.User;
import com.example.User.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins="http://localhost:4200")
@RestController
public class UserController {
    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/users") //listar los usuarios
    public List<User> list() {

        List<User> activos = new ArrayList<>();
        for (User actual : userService.listAll())
        {
            if(actual.isActivo())
            {
                activos.add(actual);
            }
        }
        return activos;
    }

    @GetMapping("/admin/user/{username}") //obtener usuario por su username
    public ResponseEntity<User> get(@PathVariable("username") final String username) {
        try {
            User user = userService.get(username);
            if(user.isActivo()){
                return new ResponseEntity<User>(user, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }catch(NoSuchElementException e){
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/users") //crear un usuario
    public void add(@RequestBody User user) {
        userService.save(user);
    }

    @PutMapping("/admin/user/{username}") //actualizar un usuario
    public ResponseEntity<?> update(@RequestBody User user, @PathVariable ("username") final String username) {
        try {
            User existingUser = userService.get(username);
            userService.save(user);
            System.out.println(existingUser);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Inactiva un usuario dado
    @DeleteMapping("/users/{username}")
    public ResponseEntity<User> delete(@PathVariable("username") final String username){
        User existingUser = userService.get(username);
        existingUser.setActivo(false);
        User result = userService.save(existingUser);
        return ResponseEntity.ok(result);
    }

    @Modifying
    @GetMapping("/user/{name}") //Filtrar usuarios dado un nombre
    public ResponseEntity<List<User>> filterByName(@PathVariable("name") String name){
        List<User> users= userService.filterByName("%" + name + "%");
        if(users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }
}
