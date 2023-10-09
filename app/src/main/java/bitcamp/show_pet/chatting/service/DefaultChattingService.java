package bitcamp.show_pet.chatting.service;

import bitcamp.show_pet.chatting.model.dao.ChattingDAO;
import bitcamp.show_pet.chatting.model.vo.ChatMessageVO;
import bitcamp.show_pet.chatting.model.vo.ChatRoomVO;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultChattingService implements ChattingService {

  @Autowired
  private ChattingDAO chattingDAO;

  @Override
  public ChatRoomVO getChatRoomByPostIdAndUserId(int postId, int currentUserId) {
    return chattingDAO.getChatRoomByPostIdAndUserId(postId, currentUserId);
  }

  @Override
  public List<ChatMessageVO> getMessagesByRoomId(String roomId) {
    return chattingDAO.getMessagesByRoomId(roomId);
  }

  @Override
  public void saveMessage(ChatMessageVO message) {
    chattingDAO.saveMessage(message);
    // 번역된 내용이 존재하는 경우 번역된 메시지도 저장
    if (message.getTranslatedContent() != null && !message.getTranslatedContent().isEmpty()) {
      ChatMessageVO translatedMessage = new ChatMessageVO();
      translatedMessage.setRoomId(message.getRoomId());
      translatedMessage.setSenderId(message.getSenderId());
      translatedMessage.setContent(message.getTranslatedContent()); // 번역된 내용 저장
      translatedMessage.setTargetLang(message.getTargetLang());
      chattingDAO.saveMessage(translatedMessage);
    }
  }

  @Override
  public List<ChatRoomVO> getChatRoomsForSeller(int sellerId) {
    return chattingDAO.getChatRoomsForSeller(sellerId);
  }

  @Override
  public List<ChatRoomVO> getChatRoomsForMember(int memberId) {
    return chattingDAO.getChatRoomsForMember(memberId);
  }

  @Override
  public String createOrGetChatRoom(int sellerId, int currentUserId, int postId) {
    String existingRoomId = chattingDAO.checkChatRoomExists(sellerId, currentUserId, postId);
    if (existingRoomId != null) {
      return existingRoomId;
    }
    String newRoomId = UUID.randomUUID().toString();
    chattingDAO.createChatRoom(sellerId, currentUserId, newRoomId, postId);
    return newRoomId;
  }

  @Override
  public ChatRoomVO getChatRoomByRoomId(String roomId) {
    return chattingDAO.getChatRoomByRoomId(roomId);
  }

  @Override
  public String checkChatRoomExists(int sellerId, int currentUserId, int postId) {
    return chattingDAO.checkChatRoomExists(sellerId, currentUserId, postId);
  }
}
