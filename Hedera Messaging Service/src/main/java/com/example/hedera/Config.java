package com.example.hedera;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;

public final class Config {
    public final AccountId operatorId;
    public final PrivateKey operatorKey;
    public final PublicKey operatorPub;
    public final String network;
    public final String topicIdString; // Optional

    private Config(AccountId operatorId, PrivateKey operatorKey, PublicKey operatorPub, String network, String topicIdString) {
        this.operatorId = operatorId;
        this.operatorKey = operatorKey;
        this.operatorPub = operatorPub;
        this.network = network;
        this.topicIdString = topicIdString;
    }

    public static Config fromEnv() {
        String accountId = System.getenv("ACCOUNT_ID");
        String privateKeyStr = System.getenv("PRIVATE_KEY");
        String publicKeyStr = System.getenv("PUBLIC_KEY");
        String network = System.getenv("HEDERA_NETWORK");
        String topicIdString = System.getenv("TOPIC_ID");

        if (accountId == null || privateKeyStr == null) {
            throw new IllegalStateException("ACCOUNT_ID and PRIVATE_KEY must be set in environment variables.");
        }
        if (network == null) network = "testnet";

        AccountId operatorId = AccountId.fromString(accountId);
        PrivateKey operatorKey = PrivateKey.fromString(privateKeyStr);

        PublicKey operatorPub = null;
        if (publicKeyStr != null && !publicKeyStr.isBlank()) {
            operatorPub = PublicKey.fromString(publicKeyStr);
        } else {
            operatorPub = operatorKey.getPublicKey();
        }

        return new Config(operatorId, operatorKey, operatorPub, network, topicIdString);
    }

    public Client createClient() {
        Client client = switch (network.toLowerCase()) {
            case "mainnet" -> Client.forMainnet();
            case "previewnet" -> Client.forPreviewnet();
            default -> Client.forTestnet();
        };
        client.setOperator(operatorId, operatorKey);
        return client;
    }
}
