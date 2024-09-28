package com.example.prodmessenger;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class ProdmessegerApplication {

    // Defina o exchange e a chave de roteamento
    static final String topicExchangeName = "channel_exchange";
    static final String routingKey = "rota.um.#";  // Chave de roteamento

    public static void main(String[] args) {
        SpringApplication.run(ProdmessegerApplication.class, args);
    }
}