package com.mybank.orders;

import com.symphony.bdk.core.activity.command.CommandContext;
import com.symphony.bdk.core.service.message.MessageService;
import com.symphony.bdk.spring.annotation.Slash;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class PriceCommand {
  private final MessageService messages;

  public PriceCommand(MessageService messages) {
    this.messages = messages;
  }

  @Slash(value = "/price", mentionBot = false)
  public void showPriceForm(CommandContext context) {
    String message = messages.templates()
      .newTemplateFromClasspath("/templates/price-form.ftl")
      .process(Map.of());
    messages.send(context.getStreamId(), message);
  }
}
