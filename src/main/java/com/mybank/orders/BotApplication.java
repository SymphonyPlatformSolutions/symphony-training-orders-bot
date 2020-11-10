package com.mybank.orders;

import com.symphony.bdk.core.SymphonyBdk;
import com.symphony.bdk.core.auth.AuthSession;
import com.symphony.bdk.gen.api.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import static com.symphony.bdk.core.config.BdkConfigLoader.loadFromFile;

/**
 * Simple Bot Application.
 */
public class BotApplication {

  /** The Logger */
  private static final Logger log = LoggerFactory.getLogger(BotApplication.class);

  public static void main(String[] args) throws Exception {

    // Initialize BDK entry point
    final SymphonyBdk bdk = new SymphonyBdk(loadFromFile("/config.yaml"));

    // Query for a user
    List<UserV2> users = bdk.users().searchUserByUsernames(List.of("ys"));
    long userId = users.get(0).getId();

    // Create an IM stream
    Stream stream = bdk.streams().create(userId);

    // Send a message
    bdk.messages().send(stream.getId(), "Hello IM");

    // Create a room
    V3RoomAttributes roomAttributes = new V3RoomAttributes()
      .name("Fancy Room")
      .description("Fancy Description");
    V3RoomDetail v3RoomDetail = bdk.streams().create(roomAttributes);

    // Add a member to the new room
    String roomId = v3RoomDetail.getRoomSystemInfo().getId();
    bdk.streams().addMemberToRoom(userId, roomId);

    // Send a message into the room
    bdk.messages().send(roomId, "Hello Room");

    // Send a message into the room on behalf of another user
    AuthSession ys = bdk.obo("ys");
    bdk.obo(ys).messages().send(roomId, "Hello from OBO");
  }
}
