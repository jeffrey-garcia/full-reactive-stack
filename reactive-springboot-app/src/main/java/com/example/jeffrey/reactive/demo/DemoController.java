package com.example.jeffrey.reactive.demo;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.ipc.netty.tcp.TcpClient;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class DemoController {
    private static final Logger LOG = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value="/customer", method=GET)
    public Customer getCustomer(@RequestParam(value="name", required=false, defaultValue="World") String name) {

        LOG.info("greeting endpoint: " + name);
        LOG.info("thread name: " + Thread.currentThread().getName());

//        String result = restTemplate.getForObject(URI.create("http://localhost:8080/web/services/greeting2?name=" + name), String.class);
//        LOG.info("greeting endpoint: " + name + " " + result);
//        return result;

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new Customer(null, name, null);
    }

    @RequestMapping(value="/customers", method=GET)
    public ResponseEntity<List<Customer>> getCustomers(@RequestParam(value="count", required=false, defaultValue="5") int count) {
        ResponseEntity result = new ResponseEntity(getCustomerList(count), HttpStatus.OK);
        return result;
//        return getCustomerList(5);
    }

    @RequestMapping(value="/client/customers", method=GET)
    public int getCustomersByRestTemplate() {
        URI uri = URI.create("http://localhost:8081/customers?count=10000000");

        List<Customer> result = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Customer>>() {}).getBody();

        return result.size();
    }

    @RequestMapping(value="/rx/customer", method=GET)
    public Mono<Customer> getCustomerStream(@RequestParam(value="name", required=false, defaultValue="World") String name) {

        LOG.info("/rx/customer endpoint: " + name);
        LOG.info("thread name: " + Thread.currentThread().getName());

        return Mono.fromCallable(() -> {
//            String result = restTemplate.getForObject(URI.create("http://localhost:8080/web/services/greeting2?name=" + name), String.class);
//            LOG.info("greeting endpoint: " + name + " " + result);
//            return result;
            Thread.sleep(3000);
            return new Customer(null, name, null);
        }).subscribeOn(Schedulers.parallel());

        // simulate the delay of the Mono element
//        Mono<String> result = Mono.just("completed " + name);
//        return result.delayElement(Duration.ofSeconds(10));
    }

    @RequestMapping(value="/rx/customers", method=GET)
    public Flux<Customer> getCustomersStream(@RequestParam(value="count", required=false, defaultValue="5") int count) {

        LOG.info("/rx/customers");
        LOG.info("thread name: " + Thread.currentThread().getName());

        // To create a visible streaming effect in the front-end UI, simulate I/O latency in DB
        // where each customer query has a processing time of 1500 milliseconds
//        return Flux
//                .fromStream(getCustomerList(count).stream())
//                .delayElements(Duration.ofMillis(100))
//                .subscribeOn(Schedulers.elastic());

        return Flux.defer(() -> Flux.fromIterable(getCustomerList(count))).subscribeOn(Schedulers.parallel());
    }

    @RequestMapping(value="/client/rx/customers", method=GET)
    public Flux<Customer> getCustomersByWebClient() {
        WebClient client = WebClient.create("http://localhost:8081");

        Flux<Customer> result = client.get()
                .uri("/rx/customers?count=1000000")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Customer.class);

        return result;
    }

    protected List<Customer> getCustomerList(int count) {
        List<Customer> customers = new ArrayList<>();
        for (int i=0; i<count; i++) {
            customers.add(getCustomer());
        }
        return customers;
    }

    protected Customer getCustomer() {
        return new Customer(UUID.randomUUID().toString(), null, null);
    }

    protected Customer getBlockingCustomer() {
        // Simulate a blocking operation
        // 10 seconds latency
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Customer(UUID.randomUUID().toString(), null, null);
    }

    protected SampleModel getSampleModel() {
        SampleModel model = new SampleModel();
        model.setId(UUID.randomUUID().toString());
        model.setFirstName(null);
        model.setLastName(null);
        return model;
    }

    protected List<SampleModel> getSampleModelList() {
        List<SampleModel> models = new ArrayList<>();
        for (int i=0; i<5; i++) {
            models.add(getSampleModel());
        }
        return models;
    }

    @RequestMapping(value="/rx/sampleModel", method=GET)
    public Flux<SampleModel> getSampleModelStream() {

        // To create a visible streaming effect in the front-end UI, simulate I/O latency in DB
        // where each customer query has a processing time of 1500 milliseconds I
        return Flux
                .fromStream(getSampleModelList().stream())
                .delayElements(Duration.ofMillis(100))
                .subscribeOn(Schedulers.elastic());
    }

    @RequestMapping(value="/client/rx/sampleModel", method=GET)
    public Flux<SampleModel> getSampleModelWebClient() {
        // include the spring-boot-starter-reactor-netty dependency into POM.xml if you want to use WebClient here
        WebClient client = WebClient.create("http://localhost:8081");

        return client.get()
                .uri("/rx/sampleModel")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(SampleModel.class);
    }

}
