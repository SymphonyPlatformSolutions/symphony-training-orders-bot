package com.mybank.orders;

import com.symphony.bdk.core.SymphonyBdk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    bdk.datafeed().start();
  }
}
