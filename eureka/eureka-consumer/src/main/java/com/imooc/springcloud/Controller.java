package com.imooc.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@RestController
@Slf4j
public class Controller {

    @Autowired
    private LoadBalancerClient client;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/hello")
    public String hello() {
        ServiceInstance instance = client.choose("eureka-client");
        if (Objects.isNull(instance)) {
            return "No available instance";
        }

        String targetUrl = String.format("http://%s:%d/sayHi", instance.getHost(), instance.getPort());

        log.debug("url:{}", targetUrl);

        return restTemplate.getForObject(targetUrl, String.class);

    }

    @PostMapping("/hello")
    public Friend hello(@RequestBody Friend friend) {
        ServiceInstance instance = client.choose("eureka-client");
        if (Objects.isNull(instance)) {
            return null;
        }

        String targetUrl = String.format("http://%s:%d/sayHi", instance.getHost(), instance.getPort());

        log.debug("url:{}", targetUrl);

        return restTemplate.postForObject(targetUrl, friend,Friend.class);

    }
}
