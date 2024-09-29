def menu():
    print("Opções de canais para se conectar:\n"
          "1. Fofocas da CHOQUEI (Gustavo Lima, Deolane, Juliette)\n"
          "2. Papo com ícones da Música (Gustavo Lima, Deolane, Juliette)\n"
          "3. Tudo Sobre Gustavo Lima\n"
          "4. Tudo Sobre Deolane\n"
          "5. Tudo Sobre Juliette\n"
          "6. Fofocas da CHOQUEI de Gustavo Lima")


    choice = input("Digite a opção desejada para começar: ")

    routes = {
        '1': 'rota.choquei.#',
        '2': 'rota.musica.#',
        '3': 'rota.#.gustavolima',
        '4': 'rota.#.deolane',
        '5': 'rota.#.juliette',
        '6': 'rota.choquei.gustavolima'
    }

    if choice in routes:
        return routes.get(choice)  # Padrão 'choquei.#' caso escolha inválida
    else:
        return 0

# Função callback para processar as mensagens recebidas
def callback(ch, method, properties, body):
    print(f"{body.decode()}")

# Função callback para processar as mensagens recebidas
def callback_log(ch, method, properties, body):
    print(f"[AUDITORIA] {method.routing_key}: {body.decode()}")
    ch.basic_ack(delivery_tag=method.delivery_tag)
