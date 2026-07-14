package com.example.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.Conversation;
import jakarta.enterprise.context.ConversationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@ConversationScoped
public class HelloBean implements Serializable {
  private static final Logger LOG = LoggerFactory.getLogger(HelloBean.class);

  @Inject private Conversation conversation;

  private String name;
  private String message;
  private int count = 0;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getCount() {
    return count;
  }

  public void incrementCount() {
    tryBeginConversation();
    count++;
    LOG.info("Count up to {}", count);
  }

  private void tryBeginConversation() {
    if (conversation.isTransient()) {
      conversation.begin();
      LOG.info("Start!! id={}", conversation.getId());
    }
  }

  public void sayHello() {
    tryBeginConversation();
    if (name != null && !name.isEmpty()) {
      if ("wait".equals(name)) {
        try {
          TimeUnit.SECONDS.sleep(40L);
        } catch (InterruptedException e) {
          LOG.warn("sleep interrupted", e);
          Thread.currentThread().interrupt();
        }
      } else if ("error".equals(name)) {
        throw new RuntimeException("ERROR!!!");
      }
      message = "Hello, " + name + "!";
    } else {
      message = "Please enter your name.";
    }
  }

  public String beginConversation() {
    tryBeginConversation();
    return null; // Stay on the same page
  }

  public String endConversation() {
    if (!conversation.isTransient()) {
      conversation.end();
    }
    name = null;
    message = null;
    count = 0;
    return null; // Stay on the same page
  }

  public boolean isTransient() {
    return conversation.isTransient();
  }

  @Override
  public String toString() {
    return "HelloBean{"
        + "conversation="
        + conversation
        + ", name='"
        + name
        + '\''
        + ", message='"
        + message
        + '\''
        + ", count="
        + count
        + '}';
  }

  @PostConstruct
  public void initialized() {
    LOG.info("postConstruct: {}", this);
  }

  @PreDestroy
  public void preDestroy() {
    LOG.info("preDestroy: {}", this);
  }
}
