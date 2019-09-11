# Transaction Playground
To make a long story short, you cannot use transactions within a parallel stream. This is because each thread in the parallel stream has its own name thus it does participate in the transaction.

## The project
I created a services that loads cats in 4 different ways

1. sequentially
`curl -I -X GET  http://localhost:8080/api/cats/all`
2. sequentially but throwing an exception in order to create a rollback mark
`curl -I -X GET  http://localhost:8080/api/cats/all-exception`
3. in parallel `curl -I -X GET  http://localhost:8080/api/cats/all-parallel`
4. in parallel but throwing an exception in order to create a rollback mark `curl -I -X GET  http://localhost:8080/api/cats/all-parallel-exception`

There are also 2 helper calls
1. cleanup `curl -I -X DELETE  http://localhost:8080/api/cats/`
2. and one to actually view the cats `curl -X GET  http://localhost:8080/api/cats/`

## Execution

### Start the project
please execute `mvn clean package wildfly-swarm:run`    

### Normal Ordered
Call `curl -I -X GET  http://localhost:8080/api/cats/all` and then `curl -X GET  http://localhost:8080/api/cats/`  

### Normal Without Order aka Parallel
Call for cleanup `curl -I -X DELETE  http://localhost:8080/api/cats/`
Call `curl -I -X GET  http://localhost:8080/api/cats/all-parallel` and then `curl -X GET  http://localhost:8080/api/cats/`

The expected result is to see a list of cats. Without ordering. This is why parallel stream is first come-first served and reads randomly from the list.

### Normal With exception
Call for cleanup `curl -I -X DELETE  http://localhost:8080/api/cats/`
Call `curl -I -X GET  http://localhost:8080/api/cats/all-exception` and then `curl -X GET  http://localhost:8080/api/cats/` 

The expected result is an empty list. This is because the transaction was marked as rollback, so the jdbc transaction was rolledback thus all entries were not persisted to the database following the ACID model.

### Parallel With exception
Call for cleanup `curl -I -X DELETE  http://localhost:8080/api/cats/`
Call `curl -I -X GET  http://localhost:8080/api/cats/all-parallel-exception` and then `curl -X GET  http://localhost:8080/api/cats/` 

The expected result is **NOT** an empty list. This is because each thread in the parallel stream opens its own jdbc transaction and commits when done. So each time you do this, you get some cats displayed up until the point you get an Exception and the execution stops.

## Conclusion
The Streams API is designed to work correctly under certain guidelines. In practice, to benefit from parallelism, each operation is not allowed to change the state of shared objects (such operations are called side-effect-free). Provided you follow this guideline, the internal implementation of parallel streams cleverly splits the data, assigns different parts to independent threads, and merges the final result.

A test was created in `ThreadNameTest` to demonstrate the name change on the ThreadLocal variable
