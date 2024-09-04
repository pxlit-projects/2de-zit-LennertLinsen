package be.pxl.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * WishlistServiceApplication
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class WishlistServiceApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(WishlistServiceApplication.class, args);
    }
}
