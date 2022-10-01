package apicourse.consumers;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import apicourse.dtos.UserEventDto;
import apicourse.enums.ActionType;
import apicourse.services.UserService;

@Component
public class UserConsumer {

    @Autowired
    UserService userService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${api.broker.queue.usereventqueue.name}", durable = "true"),
            exchange = @Exchange(value = "${api.broker.exchange.usereventexchange}", type = ExchangeTypes.FANOUT, ignoreDeclarationExceptions = "true"))
    )
    public void listenUserEvent(@Payload UserEventDto userEventDto){
        var userModel = userEventDto.convertToUserModel();

        switch(ActionType.valueOf(userEventDto.getActionType())){
            case CREATE:
            	userService.save(userModel);
                break;
            case UPDATE:
                userService.save(userModel);
                break;
            case DELETE:
                userService.delete(userEventDto.getUserId());
                break;
        }
    }
}
