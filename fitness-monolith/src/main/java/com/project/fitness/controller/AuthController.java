package com.project.fitness.controller;
import com.project.fitness.dto.RegisterRequest;
import com.project.fitness.dto.UpdateUserRequest;
import com.project.fitness.dto.UserResponse;
import com.project.fitness.services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(userServices.getAllUsers());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String>deleteUser(@PathVariable String id){
        userServices.deleteUserById(id);
        return ResponseEntity.ok("user delete successfully !");
    }
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse>updateUser(@PathVariable  String id , @RequestBody UpdateUserRequest request){
        return ResponseEntity.ok(userServices.updateUserById( id , request));
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id){
        return  ResponseEntity.ok(userServices.getUserById(id));
    }

}
