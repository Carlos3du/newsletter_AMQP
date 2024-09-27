package com.example.prodmessenger;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProdmessengerApplication {

    // Nome do exchange
    static final String topicExchangeName = "topic-exchange";

    // Chave de roteamento (pode ser personalizada ou modificada conforme necessário)
    static final String routingKey = "rota.um.#";

    // Define o exchange do tipo 'topic'
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(topicExchangeName, false, true);
    }

    public static void main(String[] args) {
        SpringApplication.run(ProdmessengerApplication.class, args);
    }
}
