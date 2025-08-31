package com.stifell.spring.process_web.doccraft.configuration;

import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author stifell on 01.09.2025
 */
@Configuration
public class JodConverterConfig {
    @Bean
    public OfficeManager officeManager() {
        return LocalOfficeManager.builder()
                .install()
                .build();
    }

    @Bean
    public LocalConverter localConverter(OfficeManager officeManager) {
        return LocalConverter.make(officeManager);
    }
}
