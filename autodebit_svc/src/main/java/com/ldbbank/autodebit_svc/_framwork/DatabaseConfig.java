package com.ldbbank.autodebit_svc._framwork;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean(name = "sms")
    @ConfigurationProperties(prefix = "spring.smsalert")
    public DataSource SMSJdbcTemplate() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "SMSJdbcTemplate")
    public JdbcTemplate SMSDatasource(@Qualifier("sms") DataSource SMSJdbcTemplate) {
        return new JdbcTemplate(SMSJdbcTemplate);
    }

    @Bean(name = "corebank")
    @ConfigurationProperties(prefix = "spring.corebank")
    public DataSource CoreBankDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "CoreBankJdbcTemplate")
    public JdbcTemplate CoreBankJdbcTemplate(@Qualifier("corebank") DataSource CoreBankJdbcTemplate) {
        return new JdbcTemplate(CoreBankJdbcTemplate);

    }

    @Bean(name = "ebank")
    @ConfigurationProperties(prefix = "spring.ebank.primary")
    public DataSource EBankDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "EBankJdbcTemplate")
    public JdbcTemplate EBankJdbcTemplate(@Qualifier("ebank") DataSource EBankJdbcTemplate) {
        return new JdbcTemplate(EBankJdbcTemplate);

    }

    @Bean(name = "atm")
    @ConfigurationProperties(prefix = "spring.atm")
    public DataSource ATMDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "ATMJdbcTemplate")
    public JdbcTemplate ATMJdbcTemplate(@Qualifier("atm") DataSource ATMJdbcTemplate) {
        return new JdbcTemplate(ATMJdbcTemplate);

    }

    @Bean(name = "debit")
    @ConfigurationProperties(prefix = "spring.debit")
    public DataSource DEBITDataSource() {
        return DataSourceBuilder.create().build();
    }
    @Bean(name = "DEBITJdbcTemplate")
    public JdbcTemplate DEBITJdbcTemplate(@Qualifier("debit") DataSource DEBITJdbcTemplate) {
        return new JdbcTemplate(DEBITJdbcTemplate);
    }

    @Bean(name = "crb")
    @ConfigurationProperties(prefix = "spring.crb")
    public DataSource CRBDataSource() {
        return DataSourceBuilder.create().build();
    }
    @Bean(name = "CRBJdbcTemplate")
    public JdbcTemplate CRBJdbcTemplate(@Qualifier("crb") DataSource CRBJdbcTemplate) {
        return new JdbcTemplate(CRBJdbcTemplate);
    }


}
