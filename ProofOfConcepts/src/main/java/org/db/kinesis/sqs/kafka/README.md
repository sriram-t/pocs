# Message Queue & Streaming Platforms Comparison

This document compares **Amazon SQS**, **Amazon Kinesis**, and **Apache Kafka** in terms of architecture, use cases, throughput, ordering, and challenges when increasing shards/partitions.

---

## 1. Amazon SQS (Simple Queue Service)

- **Type:** Message queue (point-to-point)
- **Use Case:** Decoupling microservices, background jobs, task queues
- **Data Model:** Each message is independent
- **Retention:** Up to 14 days
- **Processing:** Consumers typically delete messages once processed
- **Scaling:** Highly elastic, automatically scales to handle traffic
- **Delivery:** At-least-once (may need deduplication)
- **Ordering:**
    - Standard queues: best-effort (no global order)
    - FIFO queues: strict within `MessageGroupId`, lower throughput

**Analogy:** Think of SQS like a **post office** — producers drop letters (messages) into a queue, and consumers pick them up one at a time and then discard them.

**Challenges When Increasing Shards/Partitions:**
- Standard queues scale automatically; no manual shard tuning required.
- FIFO queues have throughput limits per `MessageGroupId`.

---

## 2. Amazon Kinesis (Kinesis Data Streams)

- **Type:** Streaming platform (real-time data stream)
- **Use Case:** Collecting/analyzing continuous event data (logs, IoT telemetry, clickstreams)
- **Data Model:** Ordered streams (shards)
- **Retention:** 24 hours default, up to 365 days
- **Processing:** Multiple consumers can read the same stream in parallel (replay supported)
- **Scaling:** Scales by adding shards (manual or autoscaling)
- **Delivery:** At-least-once (exactly-once possible with Kinesis Data Analytics)
- **Ordering:** Strict **within a shard**

**Analogy:** Think of Kinesis like a **live TV broadcast** — multiple people can watch the same stream in real-time, rewind, and replay.

**Challenges When Increasing Shards:**
- Requires resharing or splitting key ranges
- Temporary ordering effects may occur if keys are redistributed
- Consumers may need rebalancing
- Hot shard problem if traffic is unevenly distributed

---

## 3. Apache Kafka

- **Type:** Streaming platform / log-based message broker
- **Use Case:** High-throughput event streaming, real-time analytics, log aggregation
- **Data Model:** Ordered streams (topics & partitions)
- **Retention:** Configurable (hours to months)
- **Processing:** Multiple consumers per topic/partition; supports replay
- **Scaling:** Scales by adding partitions and brokers
- **Delivery:** At-least-once by default; exactly-once achievable with Kafka Streams/transactions
- **Ordering:** Strict **within a partition**; global order across topic **not guaranteed**

**Analogy:** Think of Kafka like a **distributed log** — producers append to partitions; consumers read in order per partition.

**Challenges When Increasing Partitions:**
- Key-based ordering may break when increasing partitions
- More partitions → higher metadata overhead in brokers
- Consumers must rebalance across new partitions
- Higher replication cost per partition

---

## 4. Summary Table

| Feature | SQS | Kinesis | Kafka |
|---------|-----|---------|-------|
| Type | Message queue | Streaming platform | Streaming platform / log broker |
| Use Case | Decoupling microservices, background jobs | Real-time event data collection | High-throughput event streaming & analytics |
| Data Model | Independent messages | Ordered streams (shards) | Ordered streams (topics & partitions) |
| Retention | Up to 14 days | 24h default (max 365d) | Configurable |
| Processing | Consumers delete messages | Multiple consumers, replay supported | Multiple consumers, replay supported |
| Scaling | Automatic | By adding shards | By adding partitions/brokers |
| Delivery | At-least-once | At-least-once | At-least-once (exactly-once optional) |
| Ordering | Standard: none <br> FIFO: within `MessageGroupId` | Strict within shard | Strict within partition |
| Throughput | High (standard) <br> Lower (FIFO) | High per shard | Very high, scales with partitions/brokers |
| Challenges When Increasing Shards/Partitions | Standard auto-scaled; FIFO throughput limit | Shard redistribution, rebalancing, hot shards | Ordering may break, metadata & rebalance overhead, higher replication cost |

---

## Notes

- **Throughput vs Ordering Tradeoff:** Increasing partitions/shards improves throughput but may affect ordering (especially Kafka & Kinesis).
- **Operational Complexity:** Kafka requires careful planning when scaling partitions; Kinesis requires shard management; SQS is simplest for scaling but FIFO queues are throughput-limited.
