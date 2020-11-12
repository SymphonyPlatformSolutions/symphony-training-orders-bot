package com.mybank.orders;

import com.symphony.bdk.core.activity.ActivityMatcher;
import com.symphony.bdk.core.activity.form.FormReplyActivity;
import com.symphony.bdk.core.activity.form.FormReplyContext;
import com.symphony.bdk.core.activity.model.ActivityInfo;
import com.symphony.bdk.core.activity.model.ActivityType;
import com.symphony.bdk.core.service.message.MessageService;

public class PriceFormActivity extends FormReplyActivity<FormReplyContext> {
  private final MessageService messages;

  public PriceFormActivity(MessageService messages) {
    this.messages = messages;
  }

  @Override
  protected ActivityMatcher<FormReplyContext> matcher() {
    return c -> c.getFormId().equals("price");
  }

  @Override
  protected void onActivity(FormReplyContext context) {
    String ticker = context.getFormValue("ticker");
    int price = (int) (Math.random() * 777);
    String response = "The price of " + ticker + " is " + price;
    messages.send(context.getSourceEvent().getStream(), response);
  }

  @Override
  protected ActivityInfo info() {
    return new ActivityInfo().type(ActivityType.FORM).description("Get price form");
  }
}
