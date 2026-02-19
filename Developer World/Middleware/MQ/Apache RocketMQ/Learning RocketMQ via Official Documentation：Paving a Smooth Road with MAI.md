# Quick Navigation



RocketMQ official site：https://rocketmq.apache.org/

RocketMQ GitHub link：https://github.com/apache/rocketmq

RocketMQ docs v5.x：https://rocketmq.apache.org/docs/

RocketMQ docs v4.x：https://rocketmq.apache.org/docs/4.x/



# Introduction



## Why choose RocketMQ

### Why RocketMQ

> During the early stages of RocketMQ's development at Alibaba, we utilized it for a multitude of purposes, including asynchronous communications, search, social networking activity flows, data pipelines, and trade processes. As our trade business grew, we noticed that the messaging cluster was under increasing pressure.

#### **[TOPIC 1] RocketMQ's original**

📖 Answer via **Google AI Overviews** |🧑‍⚖️ Verified by **Qwen3.5-Plus**

Apache RocketMQ, originally developed by Alibaba in 2012 to handle **massive e-commerce traffic**, was open-sourced the same year and became an Apache **Top-Level Project** in 2017. It evolved from a 2012 internal messaging solution into a cloud-native platform (v5.0 in 2022) supporting streaming, IoT, and high-reliability financial transactions.

**Key Historical Milestones**

- **2012 (Birth):** Alibaba developed RocketMQ to address performance bottlenecks with ActiveMQ and Kafka, specifically needing low latency and high reliability for its e-commerce business, and immediately open-sourced it.
- **2015 (Cloud Era):** RocketMQ was launched on [Alibaba Cloud](https://www.alibabacloud.com/blog/the-road-to-large-scale-commercialization-of-apache-rocketmq-on-alibaba-cloud_599621), becoming the first open-source message queue to provide a public cloud (SaaS) service.
- **2016 (Apache Donation):** Alibaba donated the project to the Apache Software Foundation.
- **2017 (Apache TLP):** RocketMQ graduated as an Apache **Top-Level Project (TLP)**, becoming the first Chinese open-source project to achieve this status.
- **2022 (Version 5.0):** Released to focus on cloud-native architecture, introducing gRPC (Google Remote Procedure Call) support and separating storage from computation, evolving it into a messaging, event, and streaming platform.

**Evolution Phases**

- **1.0–3.0 (Internals & Open Source):** Polished within Alibaba, supporting massive traffic during the "Double 11" Shopping Festival.
- **4.0 (Cloud & Standardization):** Focused on maturing as an Apache project and expanding its user base beyond Alibaba.
- **5.0 (Cloud Native):** Shifted towards a stateless Proxy layer and independent client repositories (Java/C++/Go/Rust) for better cloud integration, as detailed in the [GitHub repository](https://github.com/apache/rocketmq-clients).

Today, RocketMQ serves as a foundational component for microservices and big data in many industries.

#### [TOPIC 2] RocketMQ's core use cases

📖 Answer via **Gemini 3**

It’s impressive to see how RocketMQ evolved from an internal Alibaba project to a top-level Apache foundation powerhouse. Handling the sheer scale of "Double 11" (Singles’ Day) required a tool that wasn't just a mailbox, but a high-performance backbone for the entire ecosystem.

Here is an explanation of those core use cases and why RocketMQ was the "secret sauce" for each.

1. **Asynchronous Communications (Decoupling)**

    In a massive microservices architecture, you don't want Service A to wait for Service B to finish a task before moving on. This is called **decoupling**.

    - The Goal: Improve system responsiveness and prevent "cascading failures" (where one slow service drags down the whole site).
    - How it works: When a user performs an action, the primary service sends a message to RocketMQ and immediately tells the user "Success." Other background services (like sending a confirmation email or generating a PDF) pick up that message and process it at their own pace.

2. **Search (Real-time Indexing)**

    For a giant like Alibaba, search results need to be updated the second a seller changes a price or a product goes out of stock.

    - The Goal: Keep the search index consistent with the database without slowing down the database writes.

    - How it works: Whenever a change occurs in the database, a "Change Data Capture" (CDC) event is sent to RocketMQ. A search consumer subscribes to these messages and updates the search engine (like Elasticsearch) in near real-time.

3. **Social Networking Activity Flows**

    Think of "Feeds"—the stream of updates when a friend posts a photo or likes a product. In social networking, one action can trigger thousands of updates for followers.

    - The Goal: Handle massive "fan-out" (one message going to many people) and high-concurrency traffic.

    - How it works: When an activity occurs, RocketMQ distributes that event to various "Timeline" services. Because RocketMQ supports high-throughput "Pub/Sub" (Publish/Subscribe), it ensures that millions of users see updates simultaneously without the database melting under the pressure.

4. **Data Pipelines (ETL & Buffering)**

    Alibaba generates **mountains of log data** and transaction records that need to be moved into data warehouses for analysis.

    - The Goal: Act as a massive "shock absorber" or buffer between fast data producers and slower data consumers.

    - How it works: RocketMQ collects logs and stream data from thousands of servers. It holds this data reliably until big data tools (like Spark, Flink, or Hadoop) are ready to pull it for **"Extract, Transform, Load" (ETL)** processing. This prevents the destination system from being overwhelmed by traffic spikes.

5. **Trade Processes (Distributed Transactions)**

    This is arguably RocketMQ's most critical role. When you buy an item, multiple things must happen: your money is deducted, the seller's inventory decreases, and a shipping order is created.

    - The Goal: Ensure "Eventual Consistency" across different databases without using slow, heavy-duty locking mechanisms.

    - How it works: RocketMQ uses **Transactional Messages**. It ensures that the message is only "delivered" to downstream services if the local database transaction (like the payment) succeeds. If the payment fails, the message is deleted. If the payment succeeds but the inventory service is down, RocketMQ will keep retrying until the inventory is correctly updated.

---

After observing and analyzing the performance of the ActiveMQ IO module, we identified a bottleneck as the number of queues and virtual topics increased. We attempted to address this issue through various methods, such as throttling, circuit breakers, and service downgrades, but none proved satisfactory. We also considered using Kafka, a popular messaging solution, but it did not meet our requirements for low latency and high reliability, as explained below. As a result, we made the decision to develop a new messaging engine capable of handling a wider range of use cases, from traditional pub/sub to high-volume, real-time, zero-error transaction systems.

Since its inception, Apache RocketMQ has been widely adopted by enterprise developers and cloud vendors due to its simple architecture, rich business functionality, and extreme scalability. After more than a decade of extensive scenario polishing, RocketMQ has become the industry standard for financial-grade reliable business messages and is widely used in Internet, big data, mobile Internet, IoT, and other fields.

---

💡TIP

The following table shows the comparison between RocketMQ, ActiveMQ and Kafka.

| Messaging Product | Client SDK           | Protocol and Specification                           | Ordered Message                                              | Scheduled Message | Batched Message                                 | Broadcast Message | Message Filter                                          | Server Triggered Redelivery | Message Storage                                              | Message Retroactive                          | Message Priority | High Availability and Failover                               | Message Track | Configuration                                                | Management and Operation Tools                               |
| ----------------- | -------------------- | ---------------------------------------------------- | ------------------------------------------------------------ | ----------------- | ----------------------------------------------- | ----------------- | ------------------------------------------------------- | --------------------------- | ------------------------------------------------------------ | -------------------------------------------- | ---------------- | ------------------------------------------------------------ | ------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| ActiveMQ          | Java, .NET, C++ etc. | Push model, support OpenWire, STOMP, AMQP, MQTT, JMS | Exclusive Consumer or Exclusive Queues can ensure ordering   | Supported         | Not Supported                                   | Supported         | Supported                                               | Not Supported               | Supports very fast persistence using JDBC along with a high performance journal，such as levelDB, kahaDB | Supported                                    | Supported        | Supported, depending on storage,if using levelDB it requires a ZooKeeper server | Not Supported | The default configuration is low level, user need to optimize the configuration parameters | Supported                                                    |
| Kafka             | Java, Scala etc.     | Pull model, support TCP                              | Ensure ordering of messages within a partition               | Not Supported     | Supported, with async producer                  | Not Supported     | Supported, you can use Kafka Streams to filter messages | Not Supported               | High performance file storage                                | Supported offset indicate                    | Not Supported    | Supported, requires a ZooKeeper server                       | Not Supported | Kafka uses key-value pairs format for configuration. These values can be supplied either from a file or programmatically. | Supported, use terminal command to expose core metrics       |
| RocketMQ          | Java, C++, Go        | Pull model, support TCP, JMS, OpenMessaging          | Ensure strict ordering of messages,and can scale out gracefully | Supported         | Supported, with sync mode to avoid message loss | Supported         | Supported, property filter expressions based on SQL92   | Supported                   | High performance and low latency file storage                | Supported timestamp and offset two indicates | Not Supported    | Supported, Master-Slave model, without another kit           | Supported     | Work out of box,user only need to pay attention to a few configurations | Supported, rich web and terminal command to expose core metrics |



## Concepts

This section describes the core concepts of Apache RocketMQ.

### Topic

A topic is a top-level container that is used in Apache RocketMQ to transfer and store messages that belong to the same business logic. Learn more [Topic](https://rocketmq.apache.org/docs/domainModel/02topic).



# Quick Start



# Domain Model

## Domain Model

This section describes the domain model of Apache RocketMQ.

Apache RocketMQ is a distributed middleware service that adopts an asynchronous communication model and a publish/subscribe message transmission model.

For more information about the communication model and transmission model, see **Communication model** and **Message transmission model**.

The asynchronous communication model of Apache RocketMQ features simple system topology and weak upstream-downstream coupling. Apache RocketMQ is used in asynchronous decoupling and load shifting scenarios.

## Topic

This section describes the definition, model relationship, internal attributes, and behavior constraints of topics in Apache RocketMQ. This topic also provides version compatibility information and usage notes for topics.

### Definition

A topic is logically a collection of queues; we may publish messages to or receive from it.

Topics provide the following benefits:

- **Message categorization and message isolation**: When you create a messaging service based on Apache RocketMQ, we recommend that you use different topics to manage messages of different business types for isolated storage and subscription.

- **Identity and permission management**: Messages in Apache RocketMQ are anonymous. You can use a topic to perform identity and permission management for messages of a specific category.

### Model relationship

The following figure shows the position of a topic in the domain model of Apache RocketMQ.

![](https://rocketmq.apache.org/assets/images/archifortopic-ef512066703a22865613ea9216c4c300.png)

In Apache RocketMQ, a topic is a top-level storage container in which all message resources are defined. A topic is a logical concept and not the actual unit that stores messages.

A topic contains one or more queues. Message storage and scalability are implemented based on queues. All constraints and attribute settings for a topic are implemented based on the queues in the topic.