package com.example.fluxwebclient;

import com.example.fluxwebclient.client.UserClient;
import com.example.fluxwebclient.interfaces.ProxyCreator;
import com.example.fluxwebclient.proxys.JDKProxyCreatorImpl;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FluxwebclientApplication {

    public static void main(String[] args) {
        SpringApplication.run(FluxwebclientApplication.class, args);
    }

}
