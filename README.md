# CPU Cache Simulation Library

## Overview
This library simulates a cache memory module to demonstrate the basic operations of cache behavior, including cache hits, cache misses, eviction policies, and write operations. The goal is to model a simple cache system that can interact with a backing store (a text file) and track cache access history.

### Specifications

#### Background
A cache is a temporary storage for data that allows faster access compared to its original storage, known as the backing store. Data is stored as key-value pairs, where the key serves as a unique identifier. The cache interacts with the agent (which could be a user or software) to provide data more efficiently.

- **Cache hit**: When the requested data is present in the cache.
- **Cache miss**: When the requested data is not in the cache, prompting the cache to access the backing store and store the data for future access.

Caches have limited capacity, so when a cache miss occurs, the cache may need to evict an existing entry to make room for the new data. This simulation follows a "least recently used" (LRU) replacement policy, where the least recently accessed data is evicted when the cache is full.

#### Goal
The primary goal is to simulate cache behavior, focusing on:
- Cache hits and misses
- Data retrieval and write operations
- Cache eviction policies (LRU)
- Tracking the number of cache misses during a sequence of accesses

### Class Summary
The simulation is implemented with the following key classes:

- **CacheItem**: Represents a single data item in the cache, consisting of a key and its associated data.
- **CacheResponse**: A wrapper class that stores a `CacheItem` and provides additional metrics like whether the request was a cache hit or miss.
- **Cache**: The main class representing the cache, which holds `CacheItem` objects and implements cache management policies (LRU).
- **CacheSim**: Coordinates the simulation of cache accesses and maintains a history of cache states during the simulation.
- **NotFoundException**: An exception thrown when requested data is not found in the cache or backing store.

### Tools Used
This project was developed using the following tools:
- **Java**: For writing the code and extending the code base.
- **JUnit 5**: For writing and executing unit tests to ensure the correctness of cache simulation logic.
- **IntelliJ IDEA**: As the integrated development environment (IDE) for coding, testing, and debugging the project.

### Usage Instructions

Clone the repository:
   ```bash
   git clone https://github.com/OmarMehanna/CPU-Cache-Simulation-Library.git
