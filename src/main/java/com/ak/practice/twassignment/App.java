package com.ak.practice.twassignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages={"com.ak.practice"})
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class App 
{
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
    
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
