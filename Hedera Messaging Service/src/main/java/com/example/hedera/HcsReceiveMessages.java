package com.example.hedera;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.TopicId;
import com.hedera.hashgraph.sdk.TopicMessage;
import com.hedera.hashgraph.sdk.TopicMessageQuery;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class HcsReceiveMessages {
    public static void main(String[] args) {
        Config cfg = Config.fromEnv();
        if (cfg.topicIdString == null || cfg.topicIdString.isBlank()) {
            throw new IllegalStateException("TOPIC_ID must be set to receive messages.");
        }
        Client client = cfg.createClient();
        TopicId topicId = TopicId.fromString(cfg.topicIdString);

        System.out.println("Subscribing to topic " + topicId + " on " + cfg.network);

        // Replay messages from the last hour
        Instant start = Instant.now().minusSeconds(3600);

        TopicMessageQuery query = new TopicMessageQuery()
                .setTopicId(topicId)
                .setStartTime(start);

        // Only two arguments: client + onNext consumer
        query.subscribe(client, (TopicMessage message) -> {
            String contents = new String(message.contents, StandardCharsets.UTF_8);
            System.out.printf("Message received: \"%s\" at %s%n", contents, message.consensusTimestamp);
        });

        System.out.println("Listening... Press Ctrl+C to stop.");
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException ignored) { }
    }
}
