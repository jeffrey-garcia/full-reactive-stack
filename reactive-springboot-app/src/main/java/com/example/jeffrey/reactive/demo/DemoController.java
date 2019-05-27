package com.example.jeffrey.reactive.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
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

    @RequestMapping(value="/customers", method=GET)
    public List<Customer> getCustomers() {
        return getCustomerList();
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
                .delayElements(Duration.ofMillis(100))
                .subscribeOn(Schedulers.elastic());

//        return Flux.defer(() -> {
//            getCustomer();
//            return Flux.fromIterable(getCustomerList());
//        }).subscribeOn(Schedulers.parallel());
    }

    @RequestMapping(value="/client/rx/customers", method=GET)
    public Flux<Customer> getCustomerWebClient() {
        WebClient client = WebClient.create("http://localhost:8081");

        return client.get()
                .uri("/rx/customers")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Customer.class);
    }

    protected List<Customer> getCustomerList() {
        List<Customer> customers = new ArrayList<>();
        for (int i=0; i<1000000; i++) {
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
        WebClient client = WebClient.create("http://localhost:8081");

        return client.get()
                .uri("/rx/sampleModel")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(SampleModel.class);
    }

}
