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
      String form = "<form id=\"order\">";
      form += "<text-field name=\"ticker\" placeholder=\"Ticker\" /><br />";
      form += "<text-field name=\"quantity\" placeholder=\"Quantity\" /><br />";
      form += "<text-field name=\"price\" placeholder=\"Price\" /><br />";
      form += "<button type=\"action\" name=\"order\">Place Order</button>";
      form += "</form>";

      bdk.messages().send(msg.getStream(), form);
    }
  }

  @Override
  public void onSymphonyElementsAction(
    V4Initiator initiator,
    V4SymphonyElementsAction event
  ) {
    @SuppressWarnings("unchecked")
    Map<String, String> values = (Map<String, String>) event.getFormValues();

    String ticker = values.get("ticker").replace("$", "");
    int quantity = Integer.parseInt(values.get("quantity"));
    int price = Integer.parseInt(values.get("price"));

    String replyTemplate = "Order placed for %d of <cash tag=\"%s\"/> @ %d";
    bdk.messages().send(event.getStream(), String.format(replyTemplate, quantity, ticker, price));
  }
}
