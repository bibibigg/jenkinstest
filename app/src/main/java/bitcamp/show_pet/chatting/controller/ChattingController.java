package bitcamp.show_pet.chatting.controller;

import bitcamp.show_pet.chatting.model.vo.ChatMessageVO;
import bitcamp.show_pet.chatting.model.vo.ChatRoomVO;
import bitcamp.show_pet.chatting.service.ChattingService;
import bitcamp.show_pet.chatting.service.PapagoTranslationService;
import bitcamp.show_pet.member.model.vo.Member;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/chatting")
public class ChattingController {

  @Autowired
  private ChattingService chattingService;

  @Autowired
  private PapagoTranslationService papagoTranslationService;

  @RequestMapping("/room/{roomId}")
  public ModelAndView getChatRoom(@PathVariable("roomId") String roomId,
      HttpServletRequest request) {
    if (roomId == null || roomId.trim().isEmpty() || !roomId.matches("^[a-fA-F0-9\\-]{36}$")) {
      throw new IllegalArgumentException("잘못된 채팅방 ID입니다.");
    }

    ChatRoomVO chatRoom = chattingService.getChatRoomByRoomId(roomId);
    if (chatRoom == null) {
      throw new IllegalArgumentException("채팅방을 찾을 수 없습니다.");
    }

    ModelAndView mv = new ModelAndView("chatting/room");
    mv.addObject("room", chatRoom);
    mv.addObject("loginUser", request.getSession().getAttribute("loginUser"));
    return mv;
  }

  @RequestMapping("/message/{roomId}")
  @ResponseBody
  public List<ChatMessageVO> getChatMessages(@PathVariable String roomId) {
    return chattingService.getMessagesByRoomId(roomId);
  }

  @RequestMapping("/send")
  @ResponseBody
  public void sendMessage(ChatMessageVO message) {
    System.out.println("Received message: " + message.getContent());
    System.out.println("Room ID: " + message.getRoomId());
    System.out.println("Sender ID: " + message.getSenderId());

    try {
      chattingService.saveMessage(message);
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
  }

  @RequestMapping("/chatting/roomsForSeller")
  @ResponseBody
  public List<ChatRoomVO> getRoomsForSeller(HttpServletRequest request) {
    Member loginUser = (Member) request.getSession().getAttribute("loginUser");
    if (loginUser == null) {
      throw new IllegalArgumentException("로그인이 필요합니다.");
    }
    return chattingService.getChatRoomsForSeller(loginUser.getId());
  }

  @RequestMapping("/createOrGetChatRoom")
  @ResponseBody
  public ResponseEntity<Map<String, Object>> createOrGetChatRoom(@RequestParam int sellerId,
      @RequestParam int currentUserId, @RequestParam int postId) {
    System.out.println("채팅방 생성로직 호출!");
    Map<String, Object> result = new HashMap<>();
    String existingRoomId = chattingService.checkChatRoomExists(sellerId, currentUserId, postId);
    if (existingRoomId != null) {
      ChatRoomVO existingRoom = chattingService.getChatRoomByPostIdAndUserId(postId, currentUserId);
      if (existingRoom != null && existingRoom.getRoomId().equals(existingRoomId)) {
        result.put("success", true);
        result.put("roomId", existingRoomId);
        return ResponseEntity.ok(result);
      }
    }
    try {

      String roomId = chattingService.createOrGetChatRoom(sellerId, currentUserId, postId);
      if (roomId == null) {
        throw new RuntimeException("채팅방 ID를 가져오는데 실패했습니다.");
      }
      result.put("success", true);
      result.put("roomId", roomId);
    } catch (Exception e) {
      result.put("success", false);
      result.put("message", e.getMessage());
    }
    return ResponseEntity.ok(result);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception ex) {
    // 로그에는 상세한 정보를, 사용자에게는 간단한 메시지를 보여줍니다.
    Logger.getLogger(ChattingController.class.getName()).log(Level.SEVERE, null, ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
  }

  @RequestMapping("/myChatRooms")
  public ModelAndView getMyChatRooms(HttpServletRequest request) {
    Member loginUser = (Member) request.getSession().getAttribute("loginUser");
    if (loginUser == null) {
      throw new IllegalArgumentException("로그인이 필요합니다.");
    }
    List<ChatRoomVO> chatRooms = chattingService.getChatRoomsForMember(loginUser.getId());
    ModelAndView mv = new ModelAndView("member/profile");
    mv.addObject("chatRooms", chatRooms);
    return mv;
  }

//  @PostMapping("/detectLanguage")
//  public ResponseEntity<ChatMessageVO> detectLanguage(@RequestBody ChatMessageVO message) {
//    try {
//      String content = message.getContent();
//      String detectedLang = papagoTranslationService.detectLanguage(content);
//      message.setDetectedLanguage(detectedLang);
//
//      return ResponseEntity.ok(message);
//    } catch (Exception ex) {
//      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }
//  }

  @PostMapping("/translate")
  public ResponseEntity<String> translateMessage(@RequestBody ChatMessageVO message) {
    String content = message.getContent();
    String targetLang = message.getTargetLang();

    if (content == null || content.isEmpty() || targetLang == null || targetLang.isEmpty()) {
      return ResponseEntity.badRequest().body("Content and targetLang are required.");
    }

    String translatedMessage = papagoTranslationService.detectAndTranslate(content, targetLang);
    return ResponseEntity.ok(translatedMessage);
  }

}
