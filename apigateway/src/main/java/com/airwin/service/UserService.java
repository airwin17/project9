package com.airwin.service;

import java.util.Map;
import java.util.HashMap;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import com.airwin.Exceptions.SelfDeleteException;
import com.airwin.dto.UserDTO;
import com.airwin.model.User;
import com.airwin.repository.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UserService implements ReactiveUserDetailsService {
private UserRepository userRepository;
public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
}
@Override
public Mono<UserDetails> findByUsername(String username) {
    return userRepository.findByUsername(username).map(user->(UserDetails) user);
}
/**
 * add new user if it doesn't exist or update if it does
 * and encrypt the new password if it exists
 * @param  user to save
 * @return {@code Mono<String>} the username of the user
*/
public Mono<Void> save(User user) {
    String password = user.getPassword();
    return Mono.create(sink->{
        if(password != null) {
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            userRepository.save(user).subscribe();
            sink.success();
        }else {
            userRepository.findByUserid(user.getUserid()).subscribe(res->{
                user.setPassword(res.getPassword());
                userRepository.save(user).subscribe();
                sink.success();
            });
        }
    });
}
/**
 * get all users
 * @return {@link Flux} of {@link UserDTO}
 */
public Flux<UserDTO> findAll() {
    return Flux.create(sink->{
        userRepository.findAll().doOnNext(user->{
            sink.next(new UserDTO(user));
        }).doOnComplete(sink::complete).subscribe();
    });
}
/**
*delete user by id if it's not the current user and it exists 
*@param id the id of the user to delete
*@param userid the id of the current loged user
*@throws {@link SelfDeleteException} if it's the current user
*@throws {@link UsernameNotFoundException} if the user doesn't exist
*@return {@code Mono<Void>}
*/
public Mono<Void> deleteById(int id, int userid){
    return Mono.create(sink->{
        userRepository.findByUserid(id).subscribe(user->{
              if(user.getUserid() == userid) {
                  sink.error(new SelfDeleteException());
              }else {
                  userRepository.deleteByUserid(id).subscribe();
                  sink.success();
              }
           },(error)->{
               sink.error(new UsernameNotFoundException("User not found"));
           }
           ,()->{sink.error(new UsernameNotFoundException("User not found"));}
           );
    });
}
/**
 * 
 * check if user data is valid in both create and update cases
 * @param user the user to validate
 * @return {@code Map<String,String>} of errors where key is the field name and value is the error message
 * */
public Mono<Map<String,String>> validateUser(User user) {
    return Mono.create(sink->{
        Map<String,String> errors = new HashMap<>();
        if(user.getUserid()==null||(user.getUserid()!=null&&user.getPassword()!=null)){
            String passwordRegex = "^(?=.*[a-zA-Z])(?=.*[0-9]).{6,}$";
            if(!user.getPassword().matches(passwordRegex)){
                errors.put("password","Password must be at least 6 characters long and contain at least one letter and one number");
            }
        }
        if(user.getUsername()!=null&&!user.getUsername().isBlank()){
            userRepository.findByUsername(user.getUsername()).subscribe(olduser->{
                if(olduser!=null){
                    errors.put("username","Username already exists");
                    if(olduser.getUsername().equals(user.getUsername())){
                        errors.remove("username");
                    }
                }
            });
            
        }else{
            errors.put("username","Username cannot be blank");
        }
        sink.success(errors);
    });
}

}
