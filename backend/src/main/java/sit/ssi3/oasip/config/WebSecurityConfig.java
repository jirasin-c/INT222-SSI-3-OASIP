package sit.ssi3.oasip.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;


import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sit.ssi3.oasip.exceptions.CustomAccessDeniedHandler;

@Configuration @EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private Argon2PasswordEncoder argon2PasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(argon2PasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
                .and()
//                .exceptionHandling().accessDeniedHandler(new JwtAccessDenied()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/api/login").permitAll()
                .antMatchers("/api/event-categories/**", "/api/events").permitAll()
                .antMatchers(HttpMethod.GET,"/api/categories/**").permitAll()
//                .antMatchers(HttpMethod.POST, "/api/event").access("hasRole('admin') or hasRole('student')")
                .antMatchers(HttpMethod.POST, "/api/event").permitAll()
                .antMatchers("/api/users" ,"/api/users/**","/api/match/**").hasRole("admin")
//                .antMatchers("/api/events/**").hasRole("student")
                .antMatchers(HttpMethod.POST ,  "/api/events/**" ).access("hasRole('admin') or hasRole('student')")
                .antMatchers(HttpMethod.DELETE ,  "/api/events/**" ).access("hasRole('admin') or hasRole('student')")
                .antMatchers(HttpMethod.PUT ,  "/api/events/**" ).access("hasRole('admin') or hasRole('student')")
                .anyRequest().authenticated();
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Override @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

}