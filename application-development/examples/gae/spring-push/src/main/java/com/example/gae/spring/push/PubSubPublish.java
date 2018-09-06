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

import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.ProjectTopicName;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PubSubPublish {

  @PostMapping("/pubsub/publish")
  public void doPost(@RequestParam("payload") String payload, HttpServletResponse resp)
      throws IOException, ServletException {
    Publisher publisher = this.publisher;
    try {
      String topicId = System.getenv("PUBSUB_TOPIC");
      // create a publisher on the topic
      if (publisher == null) {
          ProjectTopicName topicName = ProjectTopicName.newBuilder()
                  .setProject(ServiceOptions.getDefaultProjectId())
                  .setTopic(topicId)
                  .build();
              publisher = Publisher.newBuilder(topicName).build();      }
      // construct a pubsub message from the payload
      PubsubMessage pubsubMessage =
          PubsubMessage.newBuilder().setData(ByteString.copyFromUtf8(payload)).build();

      publisher.publish(pubsubMessage);
      // redirect to home page
      resp.sendRedirect("/home");
    } catch (Exception e) {
      resp.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  private Publisher publisher;

  public PubSubPublish() { }

  PubSubPublish(Publisher publisher) {
    this.publisher = publisher;
  }
}
