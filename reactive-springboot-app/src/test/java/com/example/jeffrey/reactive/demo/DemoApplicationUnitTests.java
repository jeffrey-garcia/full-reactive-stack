package com.example.jeffrey.reactive.demo;

import org.junit.Assert;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DemoApplicationUnitTests {
    private static final Logger LOG = LoggerFactory.getLogger(DemoApplicationUnitTests.class);

    @Test
    public void testMono() throws Exception {
        List<Integer> elements = new ArrayList<>(1);

        // produce a publisher that emits at most 1 item
        Mono<Integer> mono = Mono.just(1);
        // subscribe to it in order for it to emit the elements
        mono.log().subscribe(elements::add);

        Assert.assertTrue(elements.contains(mono.block()));
        Assert.assertTrue(elements.size() == 1);
    }

    @Test
    public void testMonoOnWorkerThread() throws Exception {
        List<Integer> elements = new ArrayList<>(1);

        // produce a publisher that emits at most 1 item
        Mono<Integer> mono = Mono.just(1);
        // subscribe to it in order for it to emit the elements
        mono.log()
                .subscribeOn(Schedulers.parallel())
                .doOnSuccess(i -> {
                    System.out.println("a");
                    Assert.assertTrue(elements.contains(i));
                })
                .subscribe(elements::add);
    }

    @Test
    public void testFlux() throws Exception {
        List<Integer> elements = new ArrayList<>();

        // produce a stream of data
        Flux<Integer> flux = Flux.just(1, 2, 3, 4);
        // subscribe to it in order for it to emit the elements
        flux.log().subscribe(elements::add);

        Assert.assertTrue(elements.containsAll(flux.collectList().block()));
    }

    @Test
    public void testFluxOnWorkerThread() throws Exception {
        List<Integer> elements = new ArrayList<>();

        // produce a stream of data
        Flux<Integer> flux = Flux.just(1, 2, 3, 4);
        // subscribe to it in order for it to emit the elements
        flux.log().subscribeOn(Schedulers.parallel()).subscribe(elements::add);

        Assert.assertTrue(elements.containsAll(flux.collectList().block()));
    }

    @Test
    public void testFluxWithBackPressure() throws Exception {
        List<Integer> elements = new ArrayList<>();

        // produce a stream of data
        Flux<Integer> flux = Flux.just(1, 2, 3, 4);

        // Apply backpressure so the upstream only send two elements at a time by using request():
        flux.log().subscribe(new Subscriber<Integer>() {
            Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                subscription.request(2);
            }

            @Override
            public void onNext(Integer integer) {
                elements.add(integer);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });

        Assert.assertTrue(elements.containsAll(flux.collectList().block().subList(0,1)));
    }


    @Test
    public void testFluxWithBackPressureOnWorkerThread() throws Exception {
        List<Integer> elements = new ArrayList<>();

        // produce a stream of data
        Flux<Integer> flux = Flux.just(1, 2, 3, 4);

        // Apply backpressure so the upstream only send two elements at a time by using request():
        flux.log().subscribeOn(Schedulers.parallel()).subscribe(new Subscriber<Integer>() {
            Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                subscription.request(2);
            }

            @Override
            public void onNext(Integer integer) {
                elements.add(integer);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });

        Assert.assertTrue(elements.containsAll(flux.collectList().block().subList(0,1)));
    }

    @Test
    public void testFluxSink() throws Exception {
        List<Integer> input = Arrays.asList(0,1,2,3,4);
        List<Integer> output = new ArrayList<>();

        ConnectableFlux<Integer> flux = Flux.create((FluxSink<Integer> sink) -> {
            /* get all the latest data and emit them one by one to downstream */
            input.forEach(element -> {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sink.next(element);
            });

            // notify flux completion when all data has been processed
            sink.complete();
        }).publish();

        flux.log().doOnComplete(() -> {
            Assert.assertTrue(output.containsAll(input));
        }).subscribe(integer -> {
            LOG.info("$$ {}", integer);
            output.add(integer);
        });

        flux.connect();
    }
}
