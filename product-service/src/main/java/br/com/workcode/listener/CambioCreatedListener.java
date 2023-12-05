package br.com.workcode.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CambioCreatedListener {

	@RabbitListener(queues = "cambio.v1.cambio-created")
	public void onCambioCreated(Long id) {
		System.out.println("Received Id: " + id);
	}
}
