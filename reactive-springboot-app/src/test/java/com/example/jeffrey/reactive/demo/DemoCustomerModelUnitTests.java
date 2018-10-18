package com.example.jeffrey.reactive.demo;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DemoCustomerModelUnitTests {

    @Test
    public void testList() {

        final List<Customer> customerList = new ArrayList<>();
        Runnable initList = () -> {
            for (int i=0;i<10;i++) {
                customerList.add(new Customer(String.valueOf(i), null, null));
            }
            Assert.assertTrue(customerList.size() == 10);
        };

        final List<Customer> odd_list = new ArrayList<>();
        final List<Customer> even_list = new ArrayList<>();
        Runnable filterList = () -> {
            odd_list.addAll(
                    customerList
                            .stream()
                            .filter(customer -> (Integer.parseInt(customer.getId()) % 2 == 1))
                            .collect(Collectors.toList())
            );
            Assert.assertTrue(odd_list != null);
            Assert.assertTrue(odd_list.size() == 5);
            odd_list.forEach(customer -> {
                Assert.assertTrue(Integer.parseInt(customer.getId()) % 2 == 1);
            });

            even_list.addAll(
                    customerList
                            .stream()
                            .filter(customer -> (Integer.parseInt(customer.getId()) % 2 == 0))
                            .collect(Collectors.toList())
            );
            Assert.assertTrue(even_list != null);
            Assert.assertTrue(even_list.size() == 5);
            even_list.forEach(customer -> {
                Assert.assertTrue(Integer.parseInt(customer.getId()) % 2 == 0);
            });
        };

        final List<Customer> mergedList = new ArrayList<>();
        Runnable mergeList = () -> {
            mergedList.addAll(Stream.concat(odd_list.stream(), even_list.stream()).collect(Collectors.toList()));
            Assert.assertTrue(mergedList != null);
            Assert.assertTrue(mergedList.size() == 10);
            mergedList.forEach(customer ->
                Assert.assertTrue(
                        customerList
                                .stream()
                                .anyMatch(customer1 -> customer.getId() == customer1.getId())
                )
            );
        };

        final List<String> customerIdList = new ArrayList<>();
        Runnable transformList = () -> {
            customerIdList.addAll(
                    customerList
                            .stream()
                            .map(customer -> String.valueOf(customer.getId())).collect(Collectors.toList())
            );
            Assert.assertTrue(customerIdList != null);
            Assert.assertTrue(customerIdList.size() == 10);

            customerIdList.forEach(customerId -> {
                Assert.assertTrue(
                        customerList
                                        .stream()
                                        .filter(customer -> (Integer.parseInt(customer.getId()) == Integer.parseInt(customerId))) != null
                );
            });
        };

        final Map<String, Customer> customerIdMap = new HashMap<>();
        Runnable transformListToMap = () -> {
            customerIdMap.putAll(
                    customerList
                            .stream()
                            .collect(Collectors.toMap(customer->String.valueOf(customer.getId()), customer->customer))
            );
            Assert.assertTrue(customerIdMap != null);
            Assert.assertTrue(customerIdMap.size() == 10);
            customerIdMap.entrySet().forEach(entry ->
                Assert.assertTrue(
                        customerList
                                .stream()
                                .filter(customer -> customer.getId() == entry.getValue().getId()) != null
                )
            );
        };

        Runnable aggregateIdCount = () -> {
            String oddCustomerCount = customerList.stream().map(Customer::getId).reduce("0", (lastCount, id) -> {
               if (Integer.parseInt(id) %2 == 1) {
                   int temp = Integer.parseInt(lastCount) + 1;
                   lastCount = String.valueOf(temp);
               }
               return lastCount;
            });
            Assert.assertTrue(Integer.parseInt(oddCustomerCount) == 5);

            String evenCustomerCount = customerList.stream().map(Customer::getId).reduce("0", (lastCount, id) -> {
                if (Integer.parseInt(id) %2 == 0) {
                    int temp = Integer.parseInt(lastCount) + 1;
                    lastCount = String.valueOf(temp);
                }
                return lastCount;
            });
            Assert.assertTrue(Integer.parseInt(evenCustomerCount) == 5);
        };

        // obsoleted version
//        final Map<String, String> customerGroupMap = new HashMap<>();
//        Runnable groupListToMap = () -> {
//            customerGroupMap.putAll(
//                    customerList
//                            .stream()
//                            .collect(Collectors.toMap(
//                                customer->String.valueOf(customer.getId()),
//                                customer->{
//                                    if (customer.getId() %2 == 1) {
//                                        return "odd";
//                                    } else {
//                                        return "even";
//                                    }
//                                }
//                            ))
//            );
//            Assert.assertTrue(customerGroupMap != null);
//
//            customerGroupMap.forEach((key,value)->{
//                if (Integer.parseInt(key) % 2 == 1) {
//                    Assert.assertTrue(value.equals("odd"));
//                } else {
//                    Assert.assertTrue(value.equals("even"));
//                }
//            });
//
//            Map<String, Long> customerGroupCountMap = customerGroupMap.entrySet().stream().collect(Collectors.groupingBy(entry -> entry.getValue(), Collectors.counting()));
//            Assert.assertTrue(customerGroupCountMap != null);
//            Assert.assertTrue(customerGroupCountMap.size() == 2);
//            Assert.assertTrue(customerGroupCountMap.get("odd").intValue() == 5);
//            Assert.assertTrue(customerGroupCountMap.get("even").intValue() == 5);
//        };

        final Map<Integer, List<Customer>> customerGroupMap = new HashMap<>();
        Runnable groupListToMap = () -> {
            customerGroupMap.putAll(
                    customerList
                            .stream()
                            .collect(Collectors.groupingBy(customer -> Integer.parseInt(customer.getId()) % 2))
            );
            Assert.assertTrue(customerGroupMap != null);

            Assert.assertTrue(customerGroupMap.size() == 2);
            Assert.assertTrue(customerGroupMap.containsKey(0));
            Assert.assertTrue(customerGroupMap.containsKey(1));

            customerGroupMap.forEach((key, value) -> {
                if (key.intValue() % 2 == 0) {
                    Assert.assertTrue(value.size() == 5);

                    // using filter to validate the list only contain even customer id
                    Assert.assertTrue(value.stream().filter(customer -> Integer.parseInt(customer.getId()) %2 == 0).count() == 5);

                    // using reduce to validate the list only contain even customer id
                    String totalEvenCount = value.stream().map(Customer::getId).reduce("0", (lastCount, id) -> {
                        if (Integer.parseInt(id) % 2 == 0) {
                            int temp = Integer.parseInt(lastCount) + 1;
                            lastCount = String.valueOf(temp);
                        }
                        return lastCount;
                    });
                    Assert.assertTrue(Integer.parseInt(totalEvenCount) == 5);

                } else {
                    Assert.assertTrue(value.size() == 5);

                    // using filter to validate the list only contain odd customer id
                    Assert.assertTrue(value.stream().filter(customer -> Integer.parseInt(customer.getId()) %2 == 1).count() == 5);

                    // using reduce to validate the list only contain odd customer id
                    String totalOddCount = value.stream().map(Customer::getId).reduce("0", (lastCount, id) -> {
                        if (Integer.parseInt(id) % 2 == 1) {
                            int temp = Integer.parseInt(lastCount) + 1;
                            lastCount = String.valueOf(temp);
                        }
                        return lastCount;
                    });
                    Assert.assertTrue(Integer.parseInt(totalOddCount) == 5);

                }
            });

        };

        final Map<Integer, Long> customerGroupCountMap = new HashMap<>();
        Runnable groupListToMapWithCount = () -> {
            customerGroupCountMap.putAll(
                    customerList
                            .stream()
                            .collect(Collectors.groupingBy(customer -> Integer.parseInt(customer.getId()) % 2, Collectors.counting()))
            );
            Assert.assertTrue(customerGroupCountMap != null);
            Assert.assertTrue(customerGroupCountMap.containsKey(0));
            Assert.assertTrue(customerGroupCountMap.containsKey(1));

            customerGroupCountMap.forEach((key, value) -> {
                if (key.intValue() % 2 == 0) {
                    Assert.assertTrue(value.intValue() == 5);
                } else {
                    Assert.assertTrue(value.intValue() == 5);
                }
            });
        };

        final List<Customer> sortedCustomerList = new ArrayList<>();
        Runnable sortList = () -> {
            sortedCustomerList.addAll(
                    customerList
                            .stream()
                            .sorted(Comparator.comparing(Customer::getId).reversed())
                            .collect(Collectors.toList())
            );
            Assert.assertTrue(sortedCustomerList.size() == 10);
            for (int i=0;i<10;i++) {
                Assert.assertTrue(sortedCustomerList.get(i).getId() == customerList.get(9-i).getId());
            }
        };

        initList.run();
        filterList.run();
        mergeList.run();
        transformList.run();
        transformListToMap.run();
        aggregateIdCount.run();
        groupListToMap.run();
        groupListToMapWithCount.run();
        sortList.run();
    }
}
