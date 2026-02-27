package com.example.atm.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "com.example.atm.config",
        "com.example.atm.socket",
        "com.example.atm.iso"
})
public class RootConfig {
}
