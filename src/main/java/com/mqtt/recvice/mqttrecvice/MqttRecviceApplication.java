package com.mqtt.recvice.mqttrecvice;

import com.mqtt.recvice.mqttrecvice.mqtt.MqttPushClient;
import com.mqtt.recvice.mqttrecvice.mqtt.PushCallback;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class MqttRecviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqttRecviceApplication.class, args);

        try {
            //apollo地址
            String HOST = "tcp://120.79.193.17:1883";
            //要订阅的主题
            String TOPIC1="topic";
            //指你Apollo中的用户名密码
            String userName="admin";
            String pwd="iovadmin";
            String clientid =UUID.randomUUID().toString().replace("-","");
            MqttClient client=new MqttClient(HOST,clientid,new MemoryPersistence());
            // MQTT的连接对象
            MqttConnectOptions options = new MqttConnectOptions();
            //设置连接参数
            //清除session回话
            options.setCleanSession(false);
            options.setUserName(userName);
            options.setPassword(pwd.toCharArray());
            //超时设置
            options.setConnectionTimeout(10);
            //心跳保持时间
            options.setKeepAliveInterval(20);
            //遗嘱:当该客户端端口连接时，会向whb主题发布一条信息
            options.setWill("whb","我挂了，你加油".getBytes(),2,true);
            //监听对象：自己创建
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {

                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    System.out.println(s +"-----------" + mqttMessage.toString());
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
            //打开连接
            client.connect(options);
            //设置消息级别
            int[] Qos={1};
            //订阅主题

            client.publish(TOPIC1,"{\"Act\":\"MyPosition\", \"Gis\":\"133.234567,24.345678\", \"Name\":\"深圳世界之窗\"}".getBytes(),2,false);
            String[] topics={TOPIC1};
            client.subscribe(topics,Qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
}
