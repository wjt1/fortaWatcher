package com.web3.fortawather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "watcher")
@EnableConfigurationProperties
public class WatcherCfg {

    /**
     * 监听地址
     */
    private List<String> addressList;
    /**
     * bot token
     */
    private String botToken;
    /**
     * chatId
     */
    private String chatId;
}
