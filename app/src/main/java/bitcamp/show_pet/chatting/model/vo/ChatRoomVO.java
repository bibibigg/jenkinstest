package bitcamp.show_pet.chatting.model.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ChatRoomVO implements Serializable {

  private static final long serialVersionUID = 1L;
  private String roomId;
  private int postId;
  private int sellerId;
  private int buyerId;
  private String lastMessage;
  private LocalDateTime lastUpdated;
  private String sellerNickname;
  private String buyerNickname;
  private String postTitle;  // 추가된 필드
  private String postAttachment;

  public String getRoomId() {
    return roomId;
  }

  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }

  public int getPostId() {
    return postId;
  }

  public void setPostId(int postId) {
    this.postId = postId;
  }

  public int getSellerId() {
    return sellerId;
  }

  public void setSellerId(int sellerId) {
    this.sellerId = sellerId;
  }

  public int getBuyerId() {
    return buyerId;
  }

  public void setBuyerId(int buyerId) {
    this.buyerId = buyerId;
  }

  public String getLastMessage() {
    return lastMessage;
  }

  public void setLastMessage(String lastMessage) {
    this.lastMessage = lastMessage;
  }

  public LocalDateTime getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(LocalDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public String getSellerNickname() {
    return sellerNickname;
  }

  public void setSellerNickname(String sellerNickname) {
    this.sellerNickname = sellerNickname;
  }

  public String getBuyerNickname() {
    return buyerNickname;
  }

  public void setBuyerNickname(String buyerNickname) {
    this.buyerNickname = buyerNickname;
  }

  public String getPostTitle() {
    return postTitle;
  }

  public void setPostTitle(String postTitle) {
    this.postTitle = postTitle;
  }

  public String getPostAttachment() {
    return postAttachment;
  }

  public void setPostAttachment(String postAttachment) {
    this.postAttachment = postAttachment;
  }
}
