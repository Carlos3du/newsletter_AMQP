package com.example.prodmessenger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final ConfigurableApplicationContext context;

    // Mapeamento de opções para as rotas
    private static final Map<String, String> rotas = new HashMap<>();

    static {
        rotas.put("1", "rota.choquei.#");
        rotas.put("2", "rota.musica.#");
        rotas.put("3", "rota.*.gustavolima");
        rotas.put("4", "rota.*.deolane");
        rotas.put("5", "rota.*.juliette");
        rotas.put("6", "rota.choquei.gustavolima");
    }

    public Runner(RabbitTemplate rabbitTemplate, ConfigurableApplicationContext context) {
        this.rabbitTemplate = rabbitTemplate;
        this.context = context;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Selecione a rota:");
        System.out.println("1 - Choquei");
        System.out.println("2 - Musica");
        System.out.println("3 - Gustavo Lima");
        System.out.println("4 - Deolane");
        System.out.println("5 - Juliette");
        System.out.println("6 - Choquei Gustavo Lima");
        System.out.print("Digite o número da rota: ");
        String escolhaRota = scanner.nextLine();

        String routingKey = rotas.get(escolhaRota);
        if (routingKey == null) {
            System.out.println("Rota inválida. Encerrando...");
            context.close();
            return;
        }

        String nomeDoInfluenciador = null;

        if (escolhaRota.equals("1") || escolhaRota.equals("2")) {
            System.out.print("Digite o nome do influenciador (Gustavo Lima, Deolane, Juliette): ");
            nomeDoInfluenciador = scanner.nextLine().toLowerCase();

            routingKey = routingKey.replace("#", "*" + "." + nomeDoInfluenciador);
        } else if (escolhaRota.equals("3")) {
            nomeDoInfluenciador = "Gustavo Lima";
        } else if (escolhaRota.equals("4")) {
            nomeDoInfluenciador = "Deolane";
        } else if (escolhaRota.equals("5")) {
            nomeDoInfluenciador = "Juliette";
        }

        while (true) {
            System.out.println("Digite a mensagem ou 'sair' para encerrar:");
            String corpoDaMensagem = scanner.nextLine();

            if (corpoDaMensagem.equalsIgnoreCase("sair")) {
                break;
            }

            String mensagemFormatada = formatarMensagem(nomeDoInfluenciador, corpoDaMensagem);

            rabbitTemplate.convertAndSend(ProdmessegerApplication.topicExchangeName, routingKey, mensagemFormatada);

            System.out.println("Mensagem enviada: " + mensagemFormatada);
        }

        context.close();
    }

    private String formatarMensagem(String nomeDoInfluenciador, String corpoDaMensagem) {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
        String dataHora = agora.format(formatter);
        return String.format("[%s] %s : %s", dataHora, nomeDoInfluenciador, corpoDaMensagem);
    }
}
