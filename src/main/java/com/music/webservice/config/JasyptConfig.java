package com.music.webservice.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class JasyptConfig {

  @Autowired
  private Environment env;

  @Value("${JWT_SECRET}")
  private String JWT_SECRET;

  @Bean("jasyptStringEncryptor")
  public StringEncryptor stringEncryptor() {
    StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setPassword(env.getProperty("jasypt.config.secret-key"));
    config.setPoolSize(1);
    encryptor.setConfig(config);
    return encryptor;
  }
}
