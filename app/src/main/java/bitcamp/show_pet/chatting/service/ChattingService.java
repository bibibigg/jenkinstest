package bitcamp.show_pet.chatting.service;

import bitcamp.show_pet.chatting.model.vo.ChatMessageVO;
import bitcamp.show_pet.chatting.model.vo.ChatRoomVO;
import java.util.List;

public interface ChattingService {

  ChatRoomVO getChatRoomByPostIdAndUserId(int postId, int currentUserId);

  void saveMessage(ChatMessageVO message);

  List<ChatRoomVO> getChatRoomsForSeller(int sellerId);

  List<ChatRoomVO> getChatRoomsForMember(int memberId);

  String createOrGetChatRoom(int sellerId, int currentUserId, int postId);

  ChatRoomVO getChatRoomByRoomId(String roomId);

  List<ChatMessageVO> getMessagesByRoomId(String roomId);

  String checkChatRoomExists(int sellerId, int currentUserId, int postId);
}
