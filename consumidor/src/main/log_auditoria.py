import pika as pk


# Conexão com o servidor AMQP (RabbitMQ, por exemplo)
connection = pk.BlockingConnection(pk.ConnectionParameters('localhost'))
channel = connection.channel()

# Declarar o exchange do tipo 'topic'
channel.exchange_declare(exchange='meu_exchange', exchange_type='topic')

# Criar uma fila durável para o consumidor de auditoria
channel.queue_declare(queue='auditoria_fila', durable=True)

# Binding da fila com o exchange e o padrão '#' para capturar todas as mensagens
routing_key = "#"
channel.queue_bind(exchange='meu_exchange', queue='auditoria_fila', routing_key=routing_key)

print(f" [*] Consumidor de auditoria aguardando todas as mensagens...")

# Função callback para processar as mensagens recebidas
def callback(ch, method, properties, body):
    print(f" [AUDITORIA] Mensagem recebida com chave {method.routing_key}: {body.decode()}")
    # Confirmação manual que a mensagem foi recebida e processada
    ch.basic_ack(delivery_tag=method.delivery_tag)



# Consumidor se inscreve na fila e escuta todas as mensagens
channel.basic_consume(queue='auditoria_fila',
                      on_message_callback=callback)

print(' [*] Aguardando todas as mensagens. Para sair, pressione CTRL+C')
channel.start_consuming()
