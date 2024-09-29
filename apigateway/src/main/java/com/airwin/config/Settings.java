package com.airwin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.security.authentication.AbstractUserDetailsReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.web.client.RestTemplate;

import com.airwin.repository.UserRepository;
import com.airwin.service.UserService;
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
        //AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager());
        http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(req -> req
                        .pathMatchers("/api/user/**").hasAuthority("ADMIN")
                        .anyExchange().permitAll())
                        //.addFilterAt(webFilter, SecurityWebFiltersOrder.FIRST)
                        .exceptionHandling(e-> e.authenticationEntryPoint((exchange,exception) -> {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return Mono.empty();
                        }))
                        
                        .formLogin(login->login.loginPage("/api/login")
                           .authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.ACCEPTED))
                        .authenticationSuccessHandler((exchange, auth) -> {
                            ServerHttpResponse response=exchange.getExchange().getResponse();
                            String authorities = auth.getAuthorities().stream().findFirst().get().getAuthority();
                            response.setStatusCode(HttpStatus.OK);
                            response.writeWith(Mono.just(response.bufferFactory().wrap(authorities.getBytes())));
                            return Mono.empty();})
                        .authenticationFailureHandler((exchange, exception) -> {
                            System.out.println("Loginmm Failed");
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
        String patientUrl=discoveryClient.getNextServerFromEureka("PATIENTMANAGER", false).getHomePageUrl();
        String riskUrl=discoveryClient.getNextServerFromEureka("RISKMANAGER", false).getHomePageUrl();
        return builder.routes()
        .route("PATIENTMANAGER",r->r.path("/api/patient/**").uri(patientUrl))
        .route("RISKMANAGER",r->r.path("/api/note/**").uri(riskUrl))
                .build();
    }

}