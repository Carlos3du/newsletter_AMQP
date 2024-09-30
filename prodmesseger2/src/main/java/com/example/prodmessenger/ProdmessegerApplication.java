package com.example.prodmessenger;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProdmessegerApplication {

    // Nome do exchange
    static final String topicExchangeName = "channel_exchange";
    //static final String routingKey = "rota.choquei.#";  // Chave de roteamento

    public static void main(String[] args) {
        SpringApplication.run(ProdmessegerApplication.class, args);
    }

    @Bean
    public TopicExchange channelExchange() {
        return new TopicExchange(topicExchangeName, false, false); // durable=false, autoDelete=false
    }
}