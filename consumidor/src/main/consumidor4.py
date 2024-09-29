import pika as pk
import src.main.modules.general_functions as gf

# URL de conexão com CloudAMQP
url = "amqps://nqhehetw:AxqCWPtqfY1pZK3420clEM7AczyXA9x2@jackal-01.rmq.cloudamqp.com/nqhehetw"
params = pk.URLParameters(url)
connection = pk.BlockingConnection(params)
channel = connection.channel()

# Declarar o exchange do tipo 'topic'
channel.exchange_declare(exchange='channel_exchange', exchange_type='topic')
result = channel.queue_declare('', exclusive=True)
queue_name = result.method.queue

# As rotas disponíveis pra escolha:
rotas = {
    '1': 'Papo com CHOQUEI (Gustavo Lima, Deolane, Juliette)', # Rota pra Choquei que tem Gustavo Lima, Deolane, Juliette
    '2': 'Papo com astros da Música',                          # Rota pra Música que tem Gustavo Lima, Deolane, Juliette
    '3': 'Papo com Gustavo Lima',                              # Rota individual pra Gustavo Lima
    '4': 'Papo com Deolane',                                   # Rota individual pra Deolane
    '5': 'Papo com Juliette',                                  # Rota individual pra Juliette
    '6': 'Papo exclusivo com CHOQUEI e Gustavo Lima',          # Rota pra Choquei e Gustavo Lima
}

# A pessoa vai ver as rotas disponíveis naquele dia:
print("Veja as rotas dispoíveis para participar:")
for key, route in rotas.items():
    print(f"{key}: {route}")

# Agora ela vai escolher:
while True:
    escolha = input("Agora escolha com que você vai interagir hoje:")

    # Validação:
    if escolha in rotas:
        routing_key = rotas[escolha]
        break
    else:
        print("Escolha inválida. Tente novamente.")

# Binding da fila com o exchange e a rota especificada
channel.queue_bind(exchange='channel_exchange', queue=queue_name, routing_key=routing_key)

print(f"[STATUS_CONSUMIDOR] Consumidor aguardando mensagens da rota: {routing_key}")

# Consumidor se inscreve na fila e escuta as mensagens
channel.basic_consume(queue=queue_name,
                      on_message_callback=gf.callback,
                      auto_ack=True)

print('[STATUS_CONSUMIDOR] Aguardando mensagens...')
channel.start_consuming()
import pika as pk
import src.main.modules.general_functions as gf

# URL de conexão com CloudAMQP
url = "amqps://nqhehetw:AxqCWPtqfY1pZK3420clEM7AczyXA9x2@jackal-01.rmq.cloudamqp.com/nqhehetw"
params = pk.URLParameters(url)
connection = pk.BlockingConnection(params)
channel = connection.channel()

# Declarar o exchange do tipo 'topic'
channel.exchange_declare(exchange='channel_exchange', exchange_type='topic')
result = channel.queue_declare('', exclusive=True)
queue_name = result.method.queue

routing_key = "rota.um.#"

# Binding da fila com o exchange e a rota especificada
channel.queue_bind(exchange='channel_exchange', queue=queue_name, routing_key=routing_key)

print(f"[STATUS_CONSUMIDOR] Consumidor aguardando mensagens da rota: {routing_key}")

# Consumidor se inscreve na fila e escuta as mensagens
channel.basic_consume(queue=queue_name,
                      on_message_callback=gf.callback,
                      auto_ack=True)

print('[STATUS_CONSUMIDOR] Aguardando mensagens...')
channel.start_consuming()
