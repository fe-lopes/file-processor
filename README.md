# File processor technical test

Simple Java application that reads a file, processes its lines, and converts them to uppercase.

A complete refactoring of the original Java code was performed. The solution now follows a single-threaded, sequential approach, which is more appropriate for I/O-bound operations. Responsibilities were separated, the core logic was isolated for testability, and logging was introduced to improve observability.

Multithreading was entirely removed, as it does not provide any benefit in this scenario. There is no clear need for parallelism, such as CPU-intensive tasks (e.g., heavy computation, cryptography, compression, or large-scale parsing).

---

# Critical issues identified

##  1. Unsafe shared state
```java
private static List<String> lines = new ArrayList<>();
```
The `ArrayList` is not thread-safe and is being modified by multiple threads concurrently, leading to data corruption and runtime exceptions, such as `ArrayIndexOutOfBoundsException`.

__Provided solution:__
The shared mutable state was completely removed by eliminating unnecessary multithreading. Avoiding shared state is the preferred approach.

##  2. Race condition
```java
executor.shutdown();
System.out.println(lines.size());
```
`shutdown()` does not wait for currently running tasks to complete so it may return partial or incorrect results.

__Provided solution:__
The issue was resolved by removing the multithreaded model entirely. In scenarios where concurrency is necessary, this would be addressed using `executor.awaitTermination()` or synchronization constructs like `Future` to ensure all tasks complete before accessing results.

##  3. Duplicate file processing 
Each thread reads the entire file independently, causing the same file to be processed multiple times significantly degrading performance and increasing disk contention. It wastes resources without providing any functional benefit.

__Provided solution:__
File is read exactly once using a single `BufferedReader`. Processing is performed in a single pass, eliminating redundant I/O.

## 4. Poor exception handling and lack of observability
```java
catch (Exception e) {
    e.printStackTrace();
}
```
Errors are not properly tracked or contextualized, making debugging and monitoring extremely difficult.

__Provided solution:__
Exception handling was improved by catching more specific exceptions and replacing `printStackTrace()` with structured logging using a logger

## 5. Improper resource management
```java
BufferedReader br = new BufferedReader(new FileReader("data.txt"));
...
br.close();
```
If an exception occurs before `close()` is called, the file remains open, leading to resource leaks. Over time, this can exhaust file descriptors and cause system instability or failures when opening new files.

__Provided solution:__
Code was updated to use `try-with-resources`, ensuring that the  `BufferedReader` is automatically closed regardless of whether an exception occurs.

## 6. Misuse of multithreading
Parallelism is applied to a task that is I/O-bound (file reading), bringing no performance gain and increasing overhead.

__Provided solution:__
Multithreading was removed in favor of a simple sequential approach, which is more appropriate for I/O-bound operations. This reduces complexity, eliminates concurrency risks, and improves maintainability. If future requirements justify parallelism, it can be reintroduced in a controlled well-designed manner.

## 7. Lack of separation of concerns
File reading, processing logic and output handling were all mixed in a single class. This makes the code difficult to test, maintain, and extend.

__Provided solution:__
Responsibilities were separated, isolating file I/O from business logic. This improves testability, enhances readability, and aligns the code with clean architecture and SOLID principles.
