package com.example.moviesdb.user.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration { //configuring all https in our app
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private AuthenticationProvider authenticationProvider;

    public SecurityConfiguration(JWTAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    //Spring security DSL : domain specific language

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> customizer = null;
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/**")
                        .permitAll().anyRequest().authenticated()
                ).sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//create nex session for each request
                ).authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);



        return http.build();
    }
    /*

    @Bean: This annotation indicates that the method it is applied to will produce a bean to be managed by the Spring container.

public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception: This method defines a SecurityFilterChain bean and takes an HttpSecurity object as a parameter. The HttpSecurity object is the main entry point for configuring Spring Security.

http.csrf(AbstractHttpConfigurer::disable): This disables CSRF (Cross-Site Request Forgery) protection. CSRF protection is often disabled in stateless applications, particularly those that use token-based authentication like JWT.

.authorizeHttpRequests(auth -> auth.requestMatchers("/**").permitAll().anyRequest().authenticated()): This configures authorization rules. It permits all requests (permitAll()) for any path ("/**") and requires authentication for any other request.

.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)): This sets the session creation policy to STATELESS, indicating that the application should not create an HTTP session and should rely on tokens (stateless authentication).

.authenticationProvider(authenticationProvider): This sets a custom authentication provider. The authenticationProvider bean is assumed to be defined elsewhere in the code.

.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class): This adds a custom filter (jwtAuthenticationFilter) before the UsernamePasswordAuthenticationFilter. This is likely a filter responsible for processing JWT (JSON Web Token) authentication.

return http.build(): Finally, the configured http object is returned after applying all the configurations.
     */
}
