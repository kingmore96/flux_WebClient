package com.example.fluxwebclient.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerInfo {
    /**
     * ApiServer注解上的url地址
     */
    private String url;
}
