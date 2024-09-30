import pika as pk
import src.main.modules.interface_functions as interface


url = "amqps://nqhehetw:AxqCWPtqfY1pZK3420clEM7AczyXA9x2@jackal-01.rmq.cloudamqp.com/nqhehetw"
params = pk.URLParameters(url)


connection = pk.BlockingConnection(params)
channel = connection.channel()


channel.exchange_declare(exchange='channel_exchange', exchange_type='topic', durable=False)


queue_name = 'auditoria_log'
channel.queue_declare(queue=queue_name, durable=True)


routing_key = '#'
channel.queue_bind(exchange='channel_exchange', queue=queue_name, routing_key=routing_key)


print(f"[STATUS_AUDITORIA] Log de auditoria aguardando todas as mensagens...")
channel.basic_consume(queue=queue_name,
                      on_message_callback=interface.callback_log,
                      auto_ack=False)
channel.start_consuming()
