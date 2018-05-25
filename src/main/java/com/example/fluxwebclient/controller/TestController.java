package com.example.fluxwebclient.controller;

import com.example.fluxwebclient.client.IUserApi;
import com.example.fluxwebclient.client.UserClient;
import com.example.fluxwebclient.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class TestController {

    @Autowired
    private UserClient userClient;


    @Autowired
    private IUserApi iUserApi;

    /**
     * 测试信息提取：包括启动的时候serverInfo和methodInfo
     */
    @GetMapping("/")
    public void testExtractInfo(){
        userClient.add(Mono.just(User.builder().age(12).name("小红").build()));
        userClient.delete("5b0610ad87e8e430bc41bc7f");
        //userClient.findAllByStream();
        userClient.findById("5b06331087e8e430bc41bc80");
        userClient.update(Mono.just(User.builder().age(13).name("小诗诗").build()),"5b06331087e8e430bc41bc80");
    }

    /**
     * 测试查询总用户，没有参数
     */
    @GetMapping("/list")
    public void testList(){
        Flux<User> users = userClient.findAll();
        users.subscribe(System.out::println);
    }

    /**
     * 测试带uri参数的删除
     */
    @GetMapping("/delete")
    public void testDelete(){
        Mono<Void> delete = userClient.delete("5b0751ac87e8e4dfb0214bd8");
        delete.subscribe();
    }

    /**
     * 测试查询单个
     */
    @GetMapping("/findbyid")
    public void testFindById(){
        Mono<User> byId = userClient.findById("5b06816d87e8e47b6876c261");
        byId.subscribe(System.out::println);
    }


    /**
     * 测试带body的
     */
    @GetMapping("/create")
    public void testCreate(){
        userClient.add(Mono.just(User.builder().name("Dafengzi").age(22).build()))
                .subscribe(System.out::println);

    }

    /**
     * 测试带body的，直接发送Mono
     */
    @GetMapping("/create2")
    public void testCreate2(){
        WebClient client = WebClient.create("http://localhost:8080/user");
        Mono<User> user = client.post()
                .uri("/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(User.builder().name("wew").age(22).build()),User.class)
                .retrieve()
                .bodyToMono(User.class);
        user.subscribe(System.out::println);
    }

    /**
     * 测试更新信息
     */

    @GetMapping("/update")
    public void testUpdate(){
        userClient.update(Mono.just(User.builder().name("小兰").age(99).build()),"5b07778687e8e43dc888d4c1")
        .subscribe(System.out::println);
    }

}
