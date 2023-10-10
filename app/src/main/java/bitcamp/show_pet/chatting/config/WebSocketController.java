package bitcamp.show_pet.chatting.config;

import bitcamp.show_pet.chatting.model.vo.ChatMessageVO;
import bitcamp.show_pet.chatting.model.vo.ChatRoomVO;
import bitcamp.show_pet.chatting.service.ChattingService;
import bitcamp.show_pet.chatting.service.PapagoTranslationService;
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

  @Autowired
  private PapagoTranslationService papagoTranslationService;

  @MessageMapping("/send")
  public ChatMessageVO handleSendMessage(ChatMessageVO message) {
    System.out.println("Received message: " + message.getContent());

    ChatRoomVO chatRoom = chattingService.getChatRoomByRoomId(message.getRoomId());
    System.out.println("Checking chat room with ID: " + message.getRoomId());
    if (chatRoom == null) {
      System.out.println("Chat room not found with ID: " + message.getRoomId());
      throw new IllegalArgumentException("존재하지 않는 채팅방입니다. RoomId: " + message.getRoomId());
    }

    String translatedMessage = papagoTranslationService.detectAndTranslate(message.getContent(), "ko");
    // 메시지 번역

    // 번역된 내용으로 메시지 업데이트
    message.setContent(translatedMessage);

    chattingService.saveMessage(message);

    messagingTemplate.convertAndSend("/topic/messages/" + message.getRoomId(), message);

    return message;
  }
}
