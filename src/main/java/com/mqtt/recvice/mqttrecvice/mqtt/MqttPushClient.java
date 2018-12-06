package com.mqtt.recvice.mqttrecvice.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttPushClient {

    private static final Logger log = LoggerFactory.getLogger(MqttPushClient.class);

    private static MqttClient client;

    public static MqttClient getClient() {
        return client;
    }

    public static void setClient(MqttClient client) {
        MqttPushClient.client = client;
    }

    private MqttConnectOptions getOption(String userName, String password, int outTime, int KeepAlive) {
        //MQTT连接设置
        MqttConnectOptions option = new MqttConnectOptions();
        //设置是否清空session,false表示服务器会保留客户端的连接记录，true表示每次连接到服务器都以新的身份连接
        option.setCleanSession(true);
        //设置连接的用户名
        option.setUserName(userName);
        //设置连接的密码
        option.setPassword(password.toCharArray());
        //设置超时时间 单位为秒
        option.setConnectionTimeout(outTime);
        //设置会话心跳时间 单位为秒 服务器会每隔(1.5*keepTime)秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        option.setKeepAliveInterval(KeepAlive);
        //setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
        //option.setWill(topic, "close".getBytes(), 2, true);
        return option;
    }

    /**
     * 连接
     * @param host
     * @param clientID
     * @param username
     * @param password
     * @param timeout
     * @param KeepAlive
     */
    public void connect(String host, String clientID, String username, String password, int timeout, int KeepAlive){
        MqttClient client;
        try {
            client = new MqttClient(host, clientID, new MemoryPersistence());
            MqttConnectOptions options = getOption(username,password,timeout,KeepAlive);
            MqttPushClient.setClient(client);
            try {
                client.setCallback(new PushCallback());
                if (!client.isConnected()) {
                    client.connect(options);
                    log.info("MQTT连接成功");
                }else {//这里的逻辑是如果连接成功就重新连接
                    client.disconnect();
                    client.connect(options);
                    log.info("MQTT断连成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 断线重连
     * @throws Exception
     */
    public Boolean reConnect() throws Exception {
        Boolean isConnected = false;
        if(null != client) {
            client.connect();
            if(client.isConnected()){
                isConnected = true;
            }
        }
        return isConnected;
    }

    /**
     * 发布，默认qos为0，非持久化
     * @param topic
     * @param pushMessage
     */
    public void publish(String topic,String pushMessage){
        publish(0, false, topic, pushMessage);
    }

    /**
     * 发布
     * @param qos
     * @param retained
     * @param topic
     * @param pushMessage
     */
    public void publish(int qos,boolean retained,String topic,String pushMessage){
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());
        MqttTopic mTopic = MqttPushClient.getClient().getTopic(topic);
        if(null == mTopic){
            log.error("MQTT topic 不存在");
        }
        MqttDeliveryToken token;
        try {
            token = mTopic.publish(message);
            token.waitForCompletion();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订阅某个主题，qos默认为0
     * @param topic
     */
    public void subscribe(String topic){
        subscribe(topic,0);
    }

    /**
     * 订阅某个主题
     * @param topic
     * @param qos
     */
    public void subscribe(String topic,int qos){
        try {
            MqttPushClient.getClient().subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
