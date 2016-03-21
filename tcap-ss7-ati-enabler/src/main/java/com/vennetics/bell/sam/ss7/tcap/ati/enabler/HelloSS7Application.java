package com.vennetics.bell.sam.ss7.tcap.ati.enabler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@SuppressWarnings({ "checkstyle:hideutilityclassconstructor", "squid:S1118" })
@ComponentScan(basePackages = { "com.vennetics.bell.sam.ss7.tcap.ati.enabler",
                                "com.vennetics.bell.sam.ss7.tcap.common"})
public class HelloSS7Application {

    public static void main(final String[] args) {
        SpringApplication.run(HelloSS7Application.class, args);
    }
}

