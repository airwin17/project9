package com.airwin.controllers;

import java.util.List;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.airwin.Exceptions.SelfDeleteException;
import com.airwin.dto.UserDTO;
import com.airwin.model.User;
import com.airwin.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
public class UserContoller {
    private UserService userService;

    public UserContoller(UserService userService) {
        this.userService = userService;
    }
    @Operation(summary = "Add new user \n Password must be at least 6 characters long and contain at least one letter and one number and username must be unique.")
    @PostMapping("/add")
    public Mono<ResponseEntity<?>> addUser(@RequestBody User user) {
        
        return Mono.create(sink->{
            userService.validateUser(user).subscribe(res->{
                if(res.size()>0) {
                    sink.success(ResponseEntity.badRequest().body(res));
                }else{
                    userService.save(user).subscribe();
                    sink.success(ResponseEntity.ok("User added successfully"));
                }
            });
        });
    }
    @Operation(summary = "Get all users")
    @GetMapping("/getAll")
    public Mono<ResponseEntity<List<UserDTO>>> getAllUsers() {
        return Mono.create(sink -> {
            userService.findAll()
                .collectList()
                .map(ResponseEntity::ok)
                .subscribe(sink::success, sink::error);
        });
    }
    @Operation(summary = "Delete user by id")
    @DeleteMapping("/delete")
    public Mono<ResponseEntity<String>> deleteUser(@RequestParam("userid") int id,@AuthenticationPrincipal User user) {
        return Mono.create(sink->{
            userService.deleteById(id,user.getUserid()).subscribe(res->{
                sink.success(ResponseEntity.ok("User deleted successfully"));
            },error->{
                if(error instanceof SelfDeleteException) {
                    sink.success(ResponseEntity.status(HttpStatus.SC_NOT_ACCEPTABLE).body("Cannot delete yourself"));
                }else if (error instanceof UsernameNotFoundException) {
                    sink.success(ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("User not found"));
                }
            });
        });
        
    }
}
