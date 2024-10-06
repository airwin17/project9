package com.airwin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.security.authentication.AbstractUserDetailsReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import com.airwin.model.User;
import com.airwin.repository.UserRepository;
import com.airwin.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.EurekaClient;

import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;

@Configuration
@EnableWebFluxSecurity
@EnableR2dbcRepositories(basePackageClasses = UserRepository.class)
public class Settings {
    @Autowired
private EurekaClient discoveryClient;
    
    private UserRepository userRepository;
    /**
     * configure security
     * overall rules:
     * - only admins can access /api/user/**
     * - authentication is done by username and password
     * - all other requests must be authenticated
     * - redirections are disabled
     * - login endpoint must be /api/login
     * @param {@link ServerHttpSecurity}
     * @return {@link SecurityWebFilterChain}
     */
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(req -> req
                        .pathMatchers("/api/user/**").hasAuthority("ADMIN")
                        .anyExchange().authenticated())
                        .exceptionHandling(e-> e.authenticationEntryPoint((exchange,exception) -> {     
                            return Mono.create(sink->{
                                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                sink.success();
                            });
                        }))
                        .addFilterAt((ex,chain)->{
                            System.out.println(ex.getRequest().getPath().value());
                            return chain.filter(ex);
                        }, SecurityWebFiltersOrder.LAST)
                        .formLogin(login->login.loginPage("/api/login")
                           .authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.ACCEPTED))
                        .authenticationSuccessHandler((exchange, auth) -> {
                            ServerHttpResponse response=exchange.getExchange().getResponse();
                            response.setStatusCode(HttpStatus.OK);
                            User user=(User) auth.getPrincipal();
                            user.setPassword("");
                            ObjectMapper mapper = new ObjectMapper();
                            response.getHeaders().add("Content-Type", "application/json");
                            
                            try {
                                return response.writeWith(Mono.just(response.bufferFactory().wrap(mapper.writeValueAsString(user).getBytes())));
                            } catch (JsonProcessingException e1) {
                                return Mono.empty();
                            }
                        })
                        .authenticationFailureHandler((exchange, exception) -> {
                            exchange.getExchange().getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return Mono.empty();
                        }))
                        .logout(logout -> logout.logoutUrl("/api/logout")
                            .logoutSuccessHandler((ex, auth) -> {
                                ex.getExchange().getResponse().setStatusCode(HttpStatus.OK);
                                return Mono.empty();
                            }))
                .authenticationManager(authenticationManager());
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        UserService userService = new UserService(userRepository);
        return userService;
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        ReactiveAuthenticationManager providerManager = 
            new UserDetailsRepositoryReactiveAuthenticationManager(userService(userRepository));
            ((AbstractUserDetailsReactiveAuthenticationManager) providerManager).setPasswordEncoder(passwordEncoder());
        return providerManager;
    }
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        String devHost="http://localhost";
        String prodHost="http://host.docker.internal";
        int patientPort=discoveryClient.getNextServerFromEureka("PATIENTMANAGER", false).getPort();
        int riskPort=discoveryClient.getNextServerFromEureka("RISKMANAGER", false).getPort();
        return builder.routes()
        .route("PATIENTMANAGER",r->r.path("/api/patient/**").uri(prodHost+":"+patientPort))
        .route("RISKMANAGER",r->r.path("/api/note/**").uri(prodHost+":"+riskPort))
                .build();
    }

}