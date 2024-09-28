package com.example.prodmessenger;

import java.util.Scanner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final ConfigurableApplicationContext context;

    public Runner(RabbitTemplate rabbitTemplate, ConfigurableApplicationContext context) {
        this.rabbitTemplate = rabbitTemplate;
        this.context = context;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Digite a mensagem ou 'sair' para encerrar:");
            String msg = scanner.nextLine();

            if (msg.equalsIgnoreCase("sair")) {
                break;
            }

            // Envia a mensagem para o exchange e a rota especificada
            rabbitTemplate.convertAndSend(ProdmessegerApplication.topicExchangeName, ProdmessegerApplication.routingKey, msg);

            System.out.println("Mensagem enviada: " + msg);
        }

        // Fecha o contexto do Spring quando o loop terminar
        context.close();
    }
}
