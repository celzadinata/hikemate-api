package com.enigmacamp.security;

import com.enigmacamp.constant.APIUrl;
import com.enigmacamp.constant.enums.UserRole;
import jakarta.servlet.DispatcherType;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfiguration {
    private final AuthenticationFilter authenticationFilter;
    private final AccessDeniedHandlerImpl accessDeniedHandler;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final CorsConfigurationSource configurationSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(configurationSource))
                .exceptionHandling(cfg -> {
                    cfg.accessDeniedHandler(accessDeniedHandler);
                    cfg.authenticationEntryPoint(authenticationEntryPoint);
                })
                .sessionManagement(cfg -> cfg.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> req
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers("/api/v1/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(APIUrl.AUTH + "/**").permitAll()

                        .requestMatchers(HttpMethod.POST,APIUrl.MOUNTAIN_API + "/**").hasAuthority(UserRole.SUPERADMIN.name())
                        .requestMatchers(HttpMethod.GET, APIUrl.MOUNTAIN_API +"/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, APIUrl.MOUNTAIN_API +"/**").hasAnyAuthority(UserRole.RANGER.name(), UserRole.SUPERADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, APIUrl.MOUNTAIN_API +"/**").hasAuthority(UserRole.SUPERADMIN.name())

                        .requestMatchers(HttpMethod.POST, APIUrl.HIKER_API + "/**").hasAuthority(UserRole.SUPERADMIN.name())
                        .requestMatchers(HttpMethod.GET, APIUrl.HIKER_API + "/").hasAuthority(UserRole.SUPERADMIN.name())
                        .requestMatchers(HttpMethod.GET, APIUrl.HIKER_API + "/{id}").hasAnyAuthority(UserRole.HIKER.name(), UserRole.SUPERADMIN.name())
                        .requestMatchers(HttpMethod.PATCH, APIUrl.HIKER_API + "/**").hasAnyAuthority(UserRole.HIKER.name(), UserRole.SUPERADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, APIUrl.HIKER_API + "/**").hasAnyAuthority(UserRole.HIKER.name(), UserRole.SUPERADMIN.name())

                        .requestMatchers(APIUrl.IMAGE_API + "/**").permitAll()

                        .requestMatchers(HttpMethod.POST, APIUrl.MOUNTAIN_ROUTE_API + "/**").hasAnyAuthority(UserRole.RANGER.name(), UserRole.SUPERADMIN.name())
                        .requestMatchers(HttpMethod.GET, APIUrl.MOUNTAIN_ROUTE_API + "/**").hasAnyAuthority(UserRole.HIKER.name(), UserRole.RANGER.name(), UserRole.SUPERADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, APIUrl.MOUNTAIN_ROUTE_API + "/**").hasAnyAuthority(UserRole.RANGER.name(), UserRole.SUPERADMIN.name())

                        .requestMatchers(HttpMethod.POST, APIUrl.PAYMENT_API + "/**").hasAuthority(UserRole.SUPERADMIN.name())

                        .requestMatchers(HttpMethod.POST, APIUrl.RANGER_API + "/**").hasAuthority(UserRole.SUPERADMIN.name())
                        .requestMatchers(HttpMethod.GET, APIUrl.RANGER_API + "/**").hasAnyAuthority(UserRole.RANGER.name(),UserRole.SUPERADMIN.name())
                        .requestMatchers(HttpMethod.PATCH, APIUrl.RANGER_API + "/**").hasAnyAuthority(UserRole.RANGER.name(), UserRole.SUPERADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, APIUrl.RANGER_API + "/**").hasAuthority(UserRole.SUPERADMIN.name())


                        .requestMatchers(HttpMethod.POST, APIUrl.ROUTE_API + "/**").hasAuthority(UserRole.SUPERADMIN.name())
                        .requestMatchers(HttpMethod.GET, APIUrl.ROUTE_API + "/**").hasAnyAuthority(UserRole.RANGER.name(),UserRole.SUPERADMIN.name())
                        .requestMatchers(HttpMethod.PATCH, APIUrl.ROUTE_API + "/**").hasAuthority(UserRole.SUPERADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, APIUrl.ROUTE_API + "/**").hasAuthority(UserRole.SUPERADMIN.name())

                        .requestMatchers(HttpMethod.POST, APIUrl.TRANSACTION_API + "/**").hasAuthority(UserRole.HIKER.name())
                        .requestMatchers(HttpMethod.GET, APIUrl.TRANSACTION_API + "/**").hasAnyAuthority(UserRole.HIKER.name(), UserRole.RANGER.name(), UserRole.SUPERADMIN.name())
                        .requestMatchers(HttpMethod.GET, APIUrl.TRANSACTION_API + "/statistic**").hasAnyAuthority(UserRole.RANGER.name(), UserRole.SUPERADMIN.name())
                        .requestMatchers(HttpMethod.PATCH, APIUrl.TRANSACTION_API + "/**").hasAnyAuthority(UserRole.RANGER.name(), UserRole.SUPERADMIN.name())

                        .requestMatchers(APIUrl.GEOAPIFY_API + "/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

