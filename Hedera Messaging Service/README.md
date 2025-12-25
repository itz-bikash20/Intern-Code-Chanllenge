# Hedera HCS Messaging Demo

A simple Java application demonstrating how to create topics, send messages, and receive messages using Hedera Consensus Service (HCS).

## Overview

This project provides a basic implementation of Hedera's Consensus Service, allowing you to:
- Create a new HCS topic
- Send messages to an existing topic
- Subscribe to and receive messages from a topic in real-time

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- A Hedera testnet account (see setup instructions below)

## Getting Your Hedera Credentials

1. Visit the [Hedera Portal](https://portal.hedera.com/)
2. Create a new account or log in to your existing account
3. Navigate to the testnet section
4. Create a new testnet account if you don't have one
5. Note down the following credentials:
   - **Account ID** (format: `0.0.XXXXXXX`)
   - **Private Key** (starts with `302e020...`)
   - **Public Key** (starts with `302a3005...`)

## Project Structure

```
hcs-messaging-service/
├── pom.xml                # Maven build configuration
└── src/
    └── main/
        └── java/
            └── com/
                └── example/
                    └── hedera/
                        ├── Config.java              # Configuration handler for environment variables
                        ├── HcsCreateTopic.java      # Creates a new HCS topic
                        ├── HcsSendMessages.java     # Sends messages to a topic
                        └── HcsReceiveMessages.java  # Subscribes to and receives messages from a topic
```

## Setup

### 1. Set Environment Variables

#### Windows (PowerShell)
```powershell
$env:ACCOUNT_ID="0.0.XXXXXXX"
$env:PRIVATE_KEY="302e020...XXXXXX"
$env:PUBLIC_KEY="302a3005....XXXX"
$env:HEDERA_NETWORK="testnet"
```

#### Linux/Mac (Bash)
```bash
export ACCOUNT_ID="0.0.XXXXXXX"
export PRIVATE_KEY="302e020...XXXXXX"
export PUBLIC_KEY="302a3005....XXXX"
export HEDERA_NETWORK="testnet"
```

### 2. Build the Project

```bash
mvn clean package
```

This will create a shaded JAR file at `target/hedera-hcs-messaging-1.0.0-shaded.jar` containing all dependencies.

## Usage

### Step 1: Create a Topic

First, create a new HCS topic:

```bash
java -cp target/hedera-hcs-messaging-1.0.0-shaded.jar com.example.hedera.HcsCreateTopic
```

This will output a Topic ID (e.g., `0.0.752XXXX`). Save this for the next steps.

### Step 2: Set Topic ID

Add the Topic ID to your environment:

#### Windows (PowerShell)
```powershell
$env:TOPIC_ID="0.0.752XXXX"
```

#### Linux/Mac (Bash)
```bash
export TOPIC_ID="0.0.752XXXX"
```

### Step 3: Send Messages

Send messages to the topic:

```bash
java -cp target/hedera-hcs-messaging-1.0.0-shaded.jar com.example.hedera.HcsSendMessages
```

Edit the `HcsSendMessages.java` file to customize the messages being sent. The current implementation sends a "How are you?" message.

### Step 4: Receive Messages

In a separate terminal window (with the same environment variables set), subscribe to receive messages:

```bash
java -cp target/hedera-hcs-messaging-1.0.0-shaded.jar com.example.hedera.HcsReceiveMessages
```

This will:
- Subscribe to the specified topic
- Replay messages from the last hour
- Continue listening for new messages in real-time
- Press `Ctrl+C` to stop

## How It Works

### Topic Creation
The `HcsCreateTopic` class creates a new topic with:
- An admin key (for topic management)
- A submit key (controls who can send messages)
- A descriptive memo

### Sending Messages
The `HcsSendMessages` class:
- Connects to the Hedera network
- Submits messages to the specified topic
- Logs the consensus timestamp for each message

### Receiving Messages
The `HcsReceiveMessages` class:
- Subscribes to a topic using the mirror node
- Retrieves historical messages from the past hour
- Listens for new messages in real-time
- Displays message content and consensus timestamps

## Configuration

All configuration is handled through environment variables:

| Variable | Description | Required | Default |
|----------|-------------|----------|---------|
| `ACCOUNT_ID` | Your Hedera account ID | Yes | - |
| `PRIVATE_KEY` | Your account's private key | Yes | - |
| `PUBLIC_KEY` | Your account's public key | No | Derived from private key |
| `HEDERA_NETWORK` | Network to use | No | `testnet` |
| `TOPIC_ID` | HCS topic ID | Yes (for send/receive) | - |

## Features

- **Real-time messaging**: Messages are delivered via consensus with cryptographic proof
- **Message replay**: Retrieve historical messages from any point in time
- **Secure**: Uses public key cryptography for authentication
- **Decentralized**: Leverages Hedera's distributed consensus

## Troubleshooting

- **"ACCOUNT_ID and PRIVATE_KEY must be set"**: Ensure environment variables are properly set
- **"TOPIC_ID must be set"**: Set the `TOPIC_ID` environment variable after creating a topic
- **Connection issues**: Verify you're using the correct network (testnet/mainnet/previewnet)
- **Insufficient balance**: Ensure your testnet account has sufficient HBAR for transactions

## Notes

- The receiver replays messages from the last hour by default (configurable in `HcsReceiveMessages.java`)
- Messages are UTF-8 encoded strings
- Consensus timestamps are provided by the Hedera network
- All transactions are recorded on the distributed ledger

## Resources

- [Hedera Documentation](https://docs.hedera.com/)
- [Hedera Java SDK](https://github.com/hashgraph/hedera-sdk-java)
- [HCS Documentation](https://docs.hedera.com/guides/docs/sdks/consensus)
- [Hedera Portal](https://portal.hedera.com/)
