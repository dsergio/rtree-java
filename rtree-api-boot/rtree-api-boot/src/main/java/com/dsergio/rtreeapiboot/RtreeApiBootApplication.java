package com.dsergio.rtreeapiboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "RTree API", version = "1.0", description = "Documentation of RTree API"))
public class RtreeApiBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(RtreeApiBootApplication.class, args);
    }
    
    
}
