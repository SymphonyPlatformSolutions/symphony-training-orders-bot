package com.mybank.orders;

import com.symphony.bdk.core.SymphonyBdk;
import com.symphony.bdk.core.activity.command.SlashCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import static com.symphony.bdk.core.config.BdkConfigLoader.loadFromFile;

/**
 * Simple Bot Application.
 */
public class BotApplication {

  /** The Logger */
  private static final Logger log = LoggerFactory.getLogger(BotApplication.class);

  public static void main(String[] args) throws Exception {

    // Initialize BDK entry point
    final SymphonyBdk bdk = new SymphonyBdk(loadFromFile("config.yaml"));

    bdk.datafeed().subscribe(new OrdersListener(bdk));

    bdk.activities().register(SlashCommand.slash(
      "/price",
      false,
      context -> {
        String message = bdk.messages().templates()
          .newTemplateFromClasspath("/templates/price-form.ftl")
          .process(Map.of());
        bdk.messages().send(context.getStreamId(), message);
      }
    ));

    bdk.activities().register(new PriceFormActivity(bdk.messages()));

    bdk.datafeed().start();
  }
}
