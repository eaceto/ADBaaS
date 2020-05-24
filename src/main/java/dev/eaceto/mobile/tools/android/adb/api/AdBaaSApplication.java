package dev.eaceto.mobile.tools.android.adb.api;

import dev.eaceto.mobile.tools.android.adb.api.properties.StorageProperties;
import dev.eaceto.mobile.tools.android.adb.api.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class AdBaaSApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdBaaSApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
