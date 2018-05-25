package com.example.fluxwebclient.config;

import com.example.fluxwebclient.client.UserClient;
import com.example.fluxwebclient.interfaces.ProxyCreator;
import com.example.fluxwebclient.proxys.JDKProxyCreatorImpl;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class UserClientConfig {

    /**
     * 注册jdk动态代理实现类
     * @return
     */
    @Bean
    public ProxyCreator jdkProxyCreator(){
        return new JDKProxyCreatorImpl();
    }

    /**
     * 使用FactoryBean注册UserClient的代理对象
     * @return
     */
    @Bean
    public FactoryBean<UserClient> userClient(ProxyCreator proxyCreator){
        return new FactoryBean<UserClient>() {
            @Override
            public UserClient getObject() throws Exception {
                return (UserClient) proxyCreator.createProxy(this.getObjectType());
            }

            @Override
            public Class<?> getObjectType() {
                return UserClient.class;

            }
        };
    }
}
