package bitcamp.show_pet.chatting.model.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ChatMessageVO implements Serializable {

  private int messageId;
  private String roomId;  // roomId의 타입을 String으로 변경
  private int senderId;
  private String content;
  private String translatedContent;
  private LocalDateTime sentAt;
  private String detectedLanguage;

  private char isRead;

  private String targetLang;

  public void setDetectedLanguage(String detectedLanguage) {
    this.detectedLanguage = detectedLanguage;
  }

  public String getDetectedLanguage() {
    return detectedLanguage;
  }

  public String getTargetLang() {
    return targetLang;
  }

  public void setTargetLang(String targetLang) {
    this.targetLang = targetLang;
  }



  public int getMessageId() {
    return messageId;
  }

  public void setMessageId(int messageId) {
    this.messageId = messageId;
  }

  public String getRoomId() {
    return roomId;
  }

  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }

  public int getSenderId() {
    return senderId;
  }

  public void setSenderId(int senderId) {
    this.senderId = senderId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public LocalDateTime getSentAt() {
    return sentAt;
  }

  public void setSentAt(LocalDateTime sentAt) {
    this.sentAt = sentAt;
  }

  public char getIsRead() {
    return isRead;
  }

  public void setIsRead(char isRead) {
    this.isRead = isRead;
  }

  public String getTranslatedContent() {
    return translatedContent;
  }

  public void setTranslatedContent(String translatedContent) {
    this.translatedContent = translatedContent;
  }
}
