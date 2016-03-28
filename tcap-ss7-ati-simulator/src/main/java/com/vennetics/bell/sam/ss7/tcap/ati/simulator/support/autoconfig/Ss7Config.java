package com.vennetics.bell.sam.ss7.tcap.ati.simulator.support.autoconfig;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig.Ss7ConfigurationProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = { "com.vennetics.bell.sam.ss7.tcap.common.support.autoconfig" })
@EnableConfigurationProperties(Ss7ConfigurationProperties.class)
public class Ss7Config {

    @Bean
    public PhoneNumberUtil phoneNumberUtil() {
        return PhoneNumberUtil.getInstance();
    }
}
