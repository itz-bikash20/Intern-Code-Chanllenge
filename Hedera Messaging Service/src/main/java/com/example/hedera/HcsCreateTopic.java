package com.example.hedera;

import com.hedera.hashgraph.sdk.*;
import java.util.concurrent.TimeoutException;

public class HcsCreateTopic {
    public static void main(String[] args) throws TimeoutException, PrecheckStatusException, ReceiptStatusException {
        Config cfg = Config.fromEnv();
        Client client = cfg.createClient();

        System.out.println("Creating HCS Topic on " + cfg.network + " with operator " + cfg.operatorId);

        TopicCreateTransaction tx = new TopicCreateTransaction()
                .setAdminKey(cfg.operatorPub)
                .setSubmitKey(cfg.operatorPub) // Optional: restrict submit to this key
                .setTopicMemo("Simple Hedera Messaging Demo");

        TransactionResponse resp = tx.execute(client);
        TransactionReceipt receipt = resp.getReceipt(client);

        TopicId topicId = receipt.topicId;
        System.out.println("Topic created: " + topicId); // e.g., 0.0.34567

        client.close();
    }
}