package com.miw.gildedrose.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "surgeprice.config")
@Data
public class SurgePriceConfigProperties {

    private float surgeByPercent;
    private int surgeInterval;
    private int surgeThreshold;
}
