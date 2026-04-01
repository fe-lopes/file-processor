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
The `ArrayList` is not thread-safe and is being modified by multiple threads concurrently leading to data corruption and runtime exceptions, such as `ArrayIndexOutOfBoundsException`.

##  2. Race condition
```java
executor.shutdown();
System.out.println(lines.size());
```
`shutdown()` does not wait for currently running tasks to complete so it may return partial or incorrect results.

##  3. Duplicate file processing 
Each thread reads the entire file independently, causing the same file to be processed multiple times significantly degrading performance and increasing disk contention. It wastes resources without providing any functional benefit.

## 4. Poor exception handling and lack of observability
```java
catch (Exception e) {
    e.printStackTrace();
}
```
Errors are not properly tracked or contextualized, making debugging and monitoring extremely difficult.

## 5. Improper resource management
```java
BufferedReader br = new BufferedReader(new FileReader("data.txt"));
...
br.close();
```
If an exception occurs before `close()` is called, the file remains open, leading to resource leaks. Over time, this can exhaust file descriptors and cause system instability or failures when opening new files.

## 6. Misuse of multithreading
Parallelism is applied to a task that is I/O-bound (file reading), bringing no performance gain and increasing overhead.

## 7. Lack of separation of concerns
File reading, processing logic and output handling were all mixed in a single class. This makes the code difficult to test, maintain, and extend.