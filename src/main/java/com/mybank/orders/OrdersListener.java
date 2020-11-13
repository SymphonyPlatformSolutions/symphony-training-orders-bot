package com.mybank.orders;

import com.symphony.bdk.core.SymphonyBdk;
import com.symphony.bdk.core.service.datafeed.RealTimeEventListener;
import com.symphony.bdk.gen.api.model.V4Initiator;
import com.symphony.bdk.gen.api.model.V4Message;
import com.symphony.bdk.gen.api.model.V4MessageSent;
import com.symphony.bdk.gen.api.model.V4SymphonyElementsAction;
import java.util.Map;

class OrdersListener implements RealTimeEventListener {
  private final SymphonyBdk bdk;

  public OrdersListener(SymphonyBdk bdk) {
    this.bdk = bdk;
  }

  @Override
  public void onMessageSent(V4Initiator initiator, V4MessageSent event) {
    V4Message msg = event.getMessage();
    String msgText = msg.getMessage().replaceAll("<[^>]*>", "");

    if (msgText.startsWith("/order")) {
      String message = bdk.messages().templates()
        .newTemplateFromClasspath("/templates/order-form.ftl")
        .process(Map.of());
      bdk.messages().send(msg.getStream(), message);
    }
  }

  @Override
  public void onSymphonyElementsAction(
    V4Initiator initiator,
    V4SymphonyElementsAction event
  ) {
    if (event.getFormId().equals("order")) {
      @SuppressWarnings("unchecked")
      Map<String, String> values = (Map<String, String>) event.getFormValues();

      String ticker = values.get("ticker").replace("$", "");
      int quantity = Integer.parseInt(values.get("quantity"));
      int price = Integer.parseInt(values.get("price"));

      Map<String, Object> data = Map.of(
        "ticker", ticker,
        "quantity", quantity,
        "price", price
      );

      String message = bdk.messages().templates()
        .newTemplateFromClasspath("/templates/order-confirm.ftl")
        .process(data);
      bdk.messages().send(event.getStream(), message);
    }
  }
}
