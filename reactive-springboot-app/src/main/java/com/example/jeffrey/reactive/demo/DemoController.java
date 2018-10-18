package com.example.jeffrey.reactive.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public Flux<Customer> getCustomersStream() {

        LOG.info("/rx/customers");
        LOG.info("thread name: " + Thread.currentThread().getName());

        // To create a visible streaming effect in the front-end UI, simulate I/O latency in DB
        // where each customer query has a processing time of 1500 milliseconds I
        return Flux
                .fromStream(getCustomerList().stream())
                .delayElements(Duration.ofMillis(1500))
                .subscribeOn(Schedulers.elastic());

//        return Flux.defer(() -> {
//            getCustomer();
//            return Flux.fromIterable(getCustomerList());
//        }).subscribeOn(Schedulers.parallel());
    }

    protected List<Customer> getCustomerList() {
        List<Customer> customers = new ArrayList<>();
        for (int i=0; i<3; i++) {
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
}
