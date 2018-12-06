package com.mqtt.recvice.mqttrecvice.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushCallback implements MqttCallback {

    private static final Logger log = LoggerFactory.getLogger(PushCallback.class);

    @Override
    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        //log.info("[MQTT] 连接断开，30S之后尝试重连...");
//        while(true) {
//            try {
//                Thread.sleep(3000);
//                if(service.reConnect()){
//                    System.out.println("重连成功");
//                    break;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                continue;
//            }
//        }
        log.info(cause.getMessage()+"连接断开");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //publish后会执行到这里
       log.info("pushComplete---------" + token.isComplete());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // subscribe后得到的消息会执行到这里面
        log.info("接收消息主题 : " + topic);
        log.info("接收消息Qos : " + message.getQos());
        log.info("接收消息内容 : " + new String(message.getPayload()));
    }
}
