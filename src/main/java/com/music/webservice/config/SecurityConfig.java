package com.music.webservice.config;


import com.music.webservice.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private Environment env;

  @Bean
  @Override
  public UserDetailsService userDetailsService() {
    // Tạo ra user trong bộ nhớ
    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    manager.createUser(
            User.withDefaultPasswordEncoder() // Sử dụng mã hóa password đơn giản
                    .username("loda")
                    .password("loda")
                    .roles("USER") // phân quyền là người dùng.
                    .build()
    );
    return manager;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.headers().frameOptions().disable();
    http.csrf().ignoringAntMatchers("/**").disable();
    http.cors().configurationSource(corsConfigurationSource());
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    String corsMapping = env.getProperty("security.web.cors-mapping");
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowCredentials(true);
    if (StringUtils.isEmpty(corsMapping)) {
      configuration.addAllowedOrigin("*");
    } else {
      String[] corsArray = corsMapping.split(",");
      for (String cors: corsArray) {
        configuration.addAllowedOrigin(cors);
      }
    }
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}

