package com.airwin.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


import com.airwin.model.User;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Component
public interface UserRepository extends ReactiveCrudRepository<User, Integer> {
public Mono<User> findByUsername(String username);
@NonNull
public Mono<User> save( @NonNull User user);

public Mono<User> findByUserid(int userid);

public Mono<Void> deleteByUserid(int userid);
@NonNull
public Flux<User> findAll();
}
