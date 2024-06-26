package com.atguigu.yygh.hosp.receiver;

import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.rabbit.constant.MqConst;
import com.atguigu.yygh.rabbit.service.RabbitService;
import com.atguigu.yygh.vo.msm.MsmVo;
import com.atguigu.yygh.vo.order.OrderMqVo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HospitalReceiver {

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private RabbitService rabbitService;

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = MqConst.QUEUE_ORDER, durable = "true"),
			exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_ORDER),
			key = {MqConst.ROUTING_ORDER}
	))
	public void receiver(OrderMqVo orderMqVo, Message message, Channel channel) throws IOException {
		if(null != orderMqVo.getAvailableNumber()) {
			try {
				//update appointment number
				Schedule schedule = scheduleService.getById(orderMqVo.getScheduleId());
				schedule.setReservedNumber(orderMqVo.getReservedNumber());
				schedule.setAvailableNumber(orderMqVo.getAvailableNumber());
				scheduleService.update(schedule);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			try {
				//cancel appointment and update
				Schedule schedule = scheduleService.getScheduleByHosScheduleId(orderMqVo.getScheduleId());
				int availableNumber = schedule.getAvailableNumber().intValue() + 1;
				schedule.setAvailableNumber(availableNumber);
				scheduleService.update(schedule);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		//send message
		MsmVo msmVo = orderMqVo.getMsmVo();
		if(null != msmVo) {
			rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_MSM, MqConst.ROUTING_MSM_ITEM, msmVo);
		}
	}
}