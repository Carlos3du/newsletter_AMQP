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

        // Mostrar as opções de rota e pedir para o usuário escolher uma
        System.out.println("Selecione a rota:");
        System.out.println("1 - Choquei");
        System.out.println("2 - Musica");
        System.out.println("3 - Gustavo Lima");
        System.out.println("4 - Deolane");
        System.out.println("5 - Juliette");
        System.out.println("6 - Choquei Gustavo Lima");
        System.out.print("Digite o número da rota: ");
        String escolhaRota = scanner.nextLine();

        // Verificar se a escolha é válida e obter a chave de roteamento
        String routingKey = rotas.get(escolhaRota);
        if (routingKey == null) {
            System.out.println("Rota inválida. Encerrando...");
            context.close();
            return;
        }

        // Perguntar o nome do influenciador uma vez no início
        System.out.print("Digite o nome do influenciador: ");
        String nomeDoInfluenciador = scanner.nextLine();

        while (true) {
            System.out.println("Digite a mensagem ou 'sair' para encerrar:");
            String corpoDaMensagem = scanner.nextLine();

            if (corpoDaMensagem.equalsIgnoreCase("sair")) {
                break;
            }

            // Formatar a mensagem com a data e hora atuais, o nome do influenciador e o corpo da mensagem
            String mensagemFormatada = formatarMensagem(nomeDoInfluenciador, corpoDaMensagem);

            // Envia a mensagem para o exchange e a rota especificada
            rabbitTemplate.convertAndSend(ProdmessegerApplication.topicExchangeName, routingKey, mensagemFormatada);

            System.out.println("Mensagem enviada: " + mensagemFormatada);
        }

        // Fecha o contexto do Spring quando o loop terminar
        context.close();
    }

    // Metodo para formatar a mensagem no padrão desejado
    private String formatarMensagem(String nomeDoInfluenciador, String corpoDaMensagem) {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
        String dataHora = agora.format(formatter);
        return String.format("[%s] %s : %s", dataHora, nomeDoInfluenciador, corpoDaMensagem);
    }
}
