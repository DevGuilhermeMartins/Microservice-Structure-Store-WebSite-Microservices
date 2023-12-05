package br.com.workcode.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CatalogCreatedListener {

	@RabbitListener(queues = "catalog.v1.catalog-created")
	public void onCatalogCreated(Long id) {
		System.out.println("Received Id: " + id);
	}
}
