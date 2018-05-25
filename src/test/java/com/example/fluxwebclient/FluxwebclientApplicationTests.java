package com.example.fluxwebclient;

import com.example.fluxwebclient.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sun.awt.windows.WEmbeddedFrame;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FluxwebclientApplicationTests {

    @Test
    public void contextLoads() {
        WebClient client = WebClient.create("http://localhost:8080/user");
        Mono<User> user = client.post()
                .uri("/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(User.builder().name("wew").age(22).build()),User.class)
                .retrieve()
                .bodyToMono(User.class);
        user.subscribe(System.out::println);
    }

}
