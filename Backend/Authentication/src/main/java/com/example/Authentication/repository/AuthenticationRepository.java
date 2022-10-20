package com.example.Authentication.repository;


import com.example.Authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


//Clase que extienden de JPARepository para que se conecten a la base de datos de editoriales.
@Repository
public interface AuthenticationRepository extends JpaRepository<User, String> {

}

