package com.system.exception;

public class BusinessException extends RuntimeException {

  static final long serialVersionUID = -827283928318391L;

  private Object messages;
  
  private Object extra;
  
  public BusinessException(String message) {

    super(message);
  }

  public BusinessException(Object messages) {

    super();

    this.messages = messages;
  }
  
  public BusinessException(Object messages, Object extra) {

    super();

    this.messages = messages;
    
    this.extra = extra;
  }

  public Object getMessages() {
    return messages;
  }

  public void setMessages(Object messages) {
    this.messages = messages;
  }

  public Object getExtra() {
    return extra;
  }

  public void setExtra(Object extra) {
    this.extra = extra;
  }

}
