package com.mqtt.recvice.mqttrecvice.mqtt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "mqtt")
public class MqttConfiguration {

    private String host;
    private String clientId;
    private String username;
    private String password;
    private String topic;
    private int timeout;
    private int KeepAlive;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getKeepAlive() {
        return KeepAlive;
    }

    public void setKeepAlive(int keepAlive) {
        KeepAlive = keepAlive;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Bean
    public MqttPushClient getMqttPushClient(){
        MqttPushClient mqttPushClient = new MqttPushClient();
        mqttPushClient.connect(host, clientId, username, password, timeout,KeepAlive);
        return mqttPushClient;
    }
}
