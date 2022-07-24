package com.miw.gildedrose;

import com.miw.gildedrose.config.SurgePriceConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class GildedRoseApp {

    public static void main(String[] args) {
        SpringApplication.run(GildedRoseApp.class, args);
    }
}
