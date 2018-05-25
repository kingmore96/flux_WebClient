package com.example.fluxwebclient.client;

import com.example.fluxwebclient.annotation.ApiServer;
import com.example.fluxwebclient.pojo.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ApiServer(value = "http://localhost:8080/user")
public interface UserClient {

    @GetMapping(value = "/origin")
    Flux<User> findAll();

    @PostMapping("/add")
    Mono<User> add(@RequestBody Mono<User> user);

    @PostMapping("/add_2")
    Mono<User> add2(@RequestBody Mono<User> user);

    @DeleteMapping("/delete/{id}")
    Mono<Void> delete(@PathVariable("id") String id);

    @PutMapping("/update/{id}")
    Mono<User> update(@RequestBody Mono<User> user,@PathVariable("id") String id);

    @GetMapping("/find/{id}")
    Mono<User> findById(@PathVariable("id") String id);

    @DeleteMapping("/delete_none/{id}")
    Mono<Void> delete2(@PathVariable("id") String id);

}
