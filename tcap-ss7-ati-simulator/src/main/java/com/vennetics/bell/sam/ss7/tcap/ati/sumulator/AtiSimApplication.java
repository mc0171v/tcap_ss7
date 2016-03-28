package com.vennetics.bell.sam.ss7.tcap.ati.sumulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.vennetics.bell.sam.core.CoreErrorsConfig;
import com.vennetics.bell.sam.error.SamErrorsConfig;
import com.vennetics.bell.sam.rest.config.RestConfig;

@SpringBootApplication
@EnableEurekaClient
@SuppressWarnings({ "checkstyle:hideutilityclassconstructor", "squid:S1118" })
@ComponentScan(basePackages = { "com.vennetics.bell.sam.ss7.tcap.ati.simulator",
                                "com.vennetics.bell.sam.ss7.tcap.common"})
@EntityScan("com.vennetics.bell.sam.*")
@Import({ SamErrorsConfig.class, CoreErrorsConfig.class, RestConfig.class })
public class AtiSimApplication {

    public static void main(final String[] args) {
        SpringApplication.run(AtiSimApplication.class, args);
    }
}

