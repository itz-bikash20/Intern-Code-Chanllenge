package com.example.hedera;

import com.hedera.hashgraph.sdk.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeoutException;

public class HcsSendMessages {
    public static void main(String[] args) throws TimeoutException, PrecheckStatusException, ReceiptStatusException {
        Config cfg = Config.fromEnv();
        if (cfg.topicIdString == null || cfg.topicIdString.isBlank()) {
            throw new IllegalStateException("TOPIC_ID must be set to send messages.");
        }
        Client client = cfg.createClient();
        TopicId topicId = TopicId.fromString(cfg.topicIdString);

        System.out.println("Sending messages to topic " + topicId + " on " + cfg.network);

//        Helper to send a single message
//        send(client, topicId, "Hello, Aryan!");
//        send(client, topicId, "Learning HCS");
//        send(client, topicId, "Message 3");
        send(client, topicId, "How are you?");

        client.close();
    }

    private static void send(Client client, TopicId topicId, String message) throws TimeoutException, PrecheckStatusException, ReceiptStatusException {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("Publishing \"" + message + "\" at " + ts);

        TopicMessageSubmitTransaction tx = new TopicMessageSubmitTransaction()
                .setTopicId(topicId)
                .setMessage(message.getBytes(StandardCharsets.UTF_8));

        TransactionResponse resp = tx.execute(client);
        TransactionReceipt receipt = resp.getReceipt(client);

        // Log consensus timestamp if available via record (optional)
        TransactionRecord record = resp.getRecord(client);
        if (record != null) {
            System.out.println("Consensus timestamp: " + record.consensusTimestamp);
        } else {
            System.out.println("Message submitted. (Consensus timestamp will be available via mirror)");
        }
    }
}