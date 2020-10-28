package com.imooc.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author 2349
 */
@SpringBootApplication
@EnableDiscoveryClient
@RestController
@Slf4j
public class SleuthBApplication {

    @LoadBalanced
    @Bean
    public RestTemplate lb() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/traceB")
    public String traceB() {
        log.info("------------- traceB --------------");
        return "traceB";
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(SleuthBApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
