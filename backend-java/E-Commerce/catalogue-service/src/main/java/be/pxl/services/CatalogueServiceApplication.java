package be.pxl.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * CatalogueServiceApplication
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CatalogueServiceApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(CatalogueServiceApplication.class, args);
    }
}
