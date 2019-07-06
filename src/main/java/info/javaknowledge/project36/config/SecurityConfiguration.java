/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.javaknowledge.project36.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *
 * @author User
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private DataSource dataSource;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/welcome").access("hasRole('ROLE_USER')")
                .antMatchers("/admin").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/reportView").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/showingcategorypage").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/showsupplier").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/showproductpage").access("hasRole('ROLE_ADMIN')")
//                .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
                .and().formLogin().loginPage("/login").loginProcessingUrl("/perform_login")
                .successForwardUrl("/loginsuccess").usernameParameter("usename")
                .passwordParameter("userpassword").failureUrl("/login")
                .and().logout().logoutUrl("/logout")
                .logoutSuccessUrl("/welcome").invalidateHttpSession(true)
                .and().csrf().disable();
    }
    
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("in security.............");
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select emailid, password, status from UserRole where emailid=? and status='True'")
                .authoritiesByUsernameQuery("select emailid,role from UserRole where emailid=?");
    }
    
}
