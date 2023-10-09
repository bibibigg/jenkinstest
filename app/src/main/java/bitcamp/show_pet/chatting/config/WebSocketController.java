package bitcamp.show_pet.chatting.config;

import bitcamp.show_pet.chatting.model.vo.ChatMessageVO;
import bitcamp.show_pet.chatting.model.vo.ChatRoomVO;
import bitcamp.show_pet.chatting.service.ChattingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

  @Autowired
  private ChattingService chattingService;

  @Autowired
  private SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/send")
  public ChatMessageVO handleSendMessage(ChatMessageVO message) {
    System.out.println("Received message: " + message.getContent());

    ChatRoomVO chatRoom = chattingService.getChatRoomByRoomId(message.getRoomId());
    System.out.println("Checking chat room with ID: " + message.getRoomId());
    if (chatRoom == null) {
      System.out.println("Chat room not found with ID: " + message.getRoomId());
      throw new IllegalArgumentException("존재하지 않는 채팅방입니다. RoomId: " + message.getRoomId());
    }

    chattingService.saveMessage(message);

    messagingTemplate.convertAndSend("/topic/messages/" + message.getRoomId(), message);

    return message;
  }
}
