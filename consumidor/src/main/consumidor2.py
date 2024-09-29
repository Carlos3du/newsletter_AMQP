import pika as pk
import os
import src.main.modules.general_functions as gf

# URL de conexão com CloudAMQP
url = "amqps://nqhehetw:AxqCWPtqfY1pZK3420clEM7AczyXA9x2@jackal-01.rmq.cloudamqp.com/nqhehetw"
params = pk.URLParameters(url)

# Conexão com o servidor AMQP via CloudAMQP
connection = pk.BlockingConnection(params)
channel = connection.channel()

# Declarar o exchange do tipo 'topic'
channel.exchange_declare(exchange='channel_exchange', exchange_type='topic')

# Criar uma fila exclusiva para o consumidor
result = channel.queue_declare('', exclusive=True)
queue_name = result.method.queue

# Agora ela vai escolher:
while True:
    escolha = gf.menu()

    # Validação:
    if escolha != 0:
        routing_key = escolha
        break
    else:
        print("Escolha inválida. Tente novamente.")

# Binding da fila com o exchange e a rota especificada
channel.queue_bind(exchange='channel_exchange', queue=queue_name, routing_key=routing_key)


print(f'[STATUS_CONSUMIDOR] Bem vindo ao canal {routing_key}, aguardando mensagens...')

# Consumidor se inscreve na fila e escuta as mensagens
channel.basic_consume(queue=queue_name,
                      on_message_callback=gf.callback,
                      auto_ack=True)


channel.start_consuming()
