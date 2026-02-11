package com.project.fitness.controller;

import com.project.fitness.dto.RegisterRequest;
import com.project.fitness.dto.UserResponse;
import com.project.fitness.model.User;
import com.project.fitness.services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
 // only final class ka constructor build

public class AuthController {
    private  final  UserServices userServices;
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest registerRequest){
        // i return  user just because after saving the entry user ,get userid and we now able to see that userid
      return  ResponseEntity.ok(userServices.register(registerRequest));
    }
//    @RequestMapping("/api/activities")
//    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest registerRequest){
//        // i return  user just because after saving the entry user ,get userid and we now able to see that userid
//        return  ResponseEntity.ok(userServices.register(registerRequest));
//    }

//    @GetMapping("/retrive")
//    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest registerRequest){
//        // i return  user just because after saving the entry user ,get userid and we now able to see that userid
//        return  ResponseEntity.ok(userServices.register(registerRequest));
//    }
//
}
