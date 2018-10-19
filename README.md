# Full Reactive Stack
Typical restful controllers receive a request and a thread is occupied until the response is sent. In that time, the controller has to retrieve the data the thread is blocking while the data store performs the query. That turns in a performance bottleneck with rising concurrent requests. With reactive programming, the thread can perform other tasks while the data store retrieves the data. That offers a performance boost but requires a change to the reactive programming paradigm.

The goal of this project is to be reactive from top to bottom. To do that the project uses Angular in the frontend and Spring Boot with Reactive Web as server. Mongodb is the database connected with the reactive MongoDB driver. That enables a reactive chain from the browser to the DB. The project uses an in memory MongoDB to be just cloned build and ready to run.

<br/>

## Technology Stack
1. SpringBoot 2.0.4
2. Spring Webflux 5.0.8
3. Tomcat 8.5.32 (embedded web server in SpringBoot)
4. Java 8

For the purpose of this project, we swap the embedded web server in SpringBoot from Netty to Tomcat, so we have fine grained control to calibrate the max-connection and max-thread, in order to easily demonstrate the efficiency gain using the flux architecture from end-to-end.

<br/>

## Reactive systems and Spring WebFlux
To get clearer on what reactive systems are, it's helpful to understand the fundamental problem they're designed to solve.

### Blocking vs non-blocking web frameworks
In traditional web applications, when a web server receives a request from a client, it accepts that request and places it in an execution queue. A thread in the execution queue’s thread pool then receives the request, reads its input parameters, and generates a response. Along the way, if the execution thread needs to call a blocking resource such as a database, a filesystem, or another web service, that thread executes the blocking request and awaits a response. In this paradigm the thread is effectively blocked until the external resource responds, which causes performance issues and limits scalability.
<br/>

If you have 100 threads in your web server’s thread pool, and 101 requests arrive, then that last extra request will not be served until one of the others finish processing their requests. If the others can finish (and thus free up the thread they’re utilising) before that 101th request arrives, great! There’s possibly no need for reactive programming. If you can free up threads faster than new requests arrive, and the time spent in those threads is mostly due to input/output, then there is no need for reactive programming.
<br/>

To combat these issues, developers create generously sized thread pools, so that while one thread is blocked another thread can continue to process requests. This require to scale the resource capacity for each running instance of the web service vertically, or scaling horizontally by creating more instances of the web service. Which is not ideal because both incur additional operating cost while not fully utilising the computation power of the commodity hardware.
<br/>

Non-blocking web frameworks such as NodeJS takes a different approach. Instead of executing a blocking request and waiting for it to complete, they use non-blocking I/O. In this paradigm, an application executes a request, provides code to be executed when a response is returned, and then gives its thread back to the server. When an external resource returns a response, the provided code will be executed. Internally, non-blocking frameworks operate using an event loop. Within the loop, the application code either provides a callback or a future containing the code to execute when the asynchronous loop completes.
<br/>

By nature, non-blocking frameworks are event-driven. This requires a different programming paradigm and a new approach to reasoning about how your code will be executed. Once you've wrapped your head around it, reactive programming can lead to very scalable applications.
