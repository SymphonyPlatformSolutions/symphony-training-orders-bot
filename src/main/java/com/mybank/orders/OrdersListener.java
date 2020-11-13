package com.mybank.orders;

import com.symphony.bdk.core.service.message.MessageService;
import com.symphony.bdk.gen.api.model.V4Message;
import com.symphony.bdk.gen.api.model.V4MessageSent;
import com.symphony.bdk.gen.api.model.V4SymphonyElementsAction;
import com.symphony.bdk.spring.events.RealTimeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
class OrdersListener {
  private final MessageService messages;

  public OrdersListener(MessageService messages) {
    this.messages = messages;
  }

  @EventListener
  public void onMessageSent(RealTimeEvent<V4MessageSent> event) {
    V4Message msg = event.getSource().getMessage();
    String msgText = msg.getMessage().replaceAll("<[^>]*>", "");

    if (msgText.startsWith("/order")) {
      String message = messages.templates()
        .newTemplateFromClasspath("/templates/order-form.ftl")
        .process(Map.of());
      messages.send(msg.getStream(), message);
    }
  }

  @EventListener
  public void onSymphonyElementsAction(
    RealTimeEvent<V4SymphonyElementsAction> event
  ) {
    if (event.getSource().getFormId().equals("order")) {
      @SuppressWarnings("unchecked")
      Map<String, String> values = (Map<String, String>) event.getSource().getFormValues();

      String ticker = values.get("ticker").replace("$", "");
      int quantity = Integer.parseInt(values.get("quantity"));
      int price = Integer.parseInt(values.get("price"));

      Map<String, Object> data = Map.of(
        "ticker", ticker,
        "quantity", quantity,
        "price", price
      );

      String message = messages.templates()
        .newTemplateFromClasspath("/templates/order-confirm.ftl")
        .process(data);
      messages.send(event.getSource().getStream(), message);
    }
  }
}
