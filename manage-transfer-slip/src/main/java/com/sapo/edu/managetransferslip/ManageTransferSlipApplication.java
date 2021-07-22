package com.sapo.edu.managetransferslip;

import com.sapo.edu.managetransferslip.model.entity.CategoriesEntity;
import com.sapo.edu.managetransferslip.model.entity.TransferEntity;
import com.sapo.edu.managetransferslip.service.impl.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
//@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class ManageTransferSlipApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageTransferSlipApplication.class, args);
    }
}
