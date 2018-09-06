/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.gae.spring.push;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class PubSubHome {

  private static MessageRepository messageRepository = MessageRepositoryImpl.getInstance();
  private static int MAX_MESSAGES = 10;

  /**
   * Retrieve received messages in html.
   *
   * @return html representation of messages (one per row)
   */
  public static String getReceivedMessages() {
    List<Message> messageList = messageRepository.retrieve(MAX_MESSAGES);
    return convertToHtmlTable(messageList);
  }

  private static String convertToHtmlTable(List<Message> messages) {
    StringBuilder sb = new StringBuilder();
    for (Message message : messages) {
      sb.append("<tr>");
      sb.append("<td>" + message.getMessageId() + "</td>");
      sb.append("<td>" + message.getData() + "</td>");
      sb.append("<td>" + message.getPublishTime() + "</td>");
      sb.append("</tr>");
    }
    return sb.toString();
  }
  
  @RequestMapping("/home")
  public String welcome(Map<String, Object> model) {
	  model.put("messages", PubSubHome.getReceivedMessages());
	  return("home");
  }

}
