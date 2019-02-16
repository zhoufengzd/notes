# rabbitmq Notes
* protocol: amqp (advanced message queuing protocol)
* general purpose message broker: point to point, request/reply, pub-sub
* reliable delivery, routing, federation

## setup and ui:
* `rabbitmq-plugins enable rabbitmq_management`
* http://localhost:15672/#/

## Architecture:
* producer -> exchange -> queue -> consumer
* broker api:
    * declare, publish, deliver, reject
* exchange: direct where to send message
    * default exchange: exchange=""
        * routing key = queue name
    * direct exchange: routing key / address
    * topic exchange: a list of words, delimited by a period (.)
    * fanout exchange: to all queues under this exchange
    * header exchange:
    * dead letter exchange
* channel: virtual connection (tcp connection)
    * channels are not thread safe.
    * delivery -> ACK on the same channel
    * one channel per consumer

## Commands
```
rabbitmqctl list_exchanges
rabbitmqctl list_bindings
rabbitmqctl change_password <zenoss> <new-password>

rabbitmqctl add_user hydra hydra_pwd
rabbitmqctl set_user_tags hydra administrator
rabbitmqctl set_permissions -p / hydra ".*" ".*" ".*"
```

## Classes
* blocking_connection.py
```
class BlockingConnection(object):
    def channel(self, channel_number=None):
    def add_timeout(self, deadline, callback_method):
    def sleep(self, duration):
    def close(self, reply_code=200, reply_text='Normal shutdown'):

class BlockingChannel(object):
    def basic_consume(self,
                      consumer_callback,
                      queue,
                      no_ack=False,
                      exclusive=False,  # whether allow other consumers on the queue
                      consumer_tag=None,
                      arguments=None):
```
* pika/spec.py
```
class BasicProperties(amqp_object.Properties):

methods = {
    0x000A000A: Connection.Start,
    0x000A0028: Connection.Open,
    ...
    0x0014000A: Channel.Open,
    ...
    0x0028000A: Exchange.Declare,
    ...
    0x0032000A: Queue.Declare,
    0x00320014: Queue.Bind,
}
```
