package com.example.jeffrey.reactive.demo;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class TrainingUnitTests {

    @Test
    public void testSockMerchant() throws Exception {
        int [] array_1 = new int[] {1,2,1,2,1,3,2};
        int [] array_2 = new int[] {10, 20, 20, 10, 10, 30, 50, 10, 20};

        Assert.assertTrue(TrainingUnitTests.sockMerchant(array_1.length, array_1) == 2);
        Assert.assertTrue(TrainingUnitTests.sockMerchant(array_2.length, array_2) == 3);
    }

    // Complete the sockMerchant function below.
    static int sockMerchant(int total, int[] array) {
        List<Integer> socksList = Arrays.stream(array).boxed().collect(Collectors.toList());

        Map<Integer, Long> sockColorCountMap = socksList.stream().collect(Collectors.groupingBy(sock -> sock, Collectors.counting()));
        Assert.assertTrue(sockColorCountMap != null);

        int totalPairsOfSocks = sockColorCountMap.entrySet().stream().map(item -> item.getValue().intValue()).reduce(0, (lastCount, itemCount) -> {
            int numOfPairs = itemCount / 2 ;
            lastCount += numOfPairs;
            return lastCount;
        });

        return totalPairsOfSocks;
    }


    @Test
    public void testCountingValleys() {
        String s1 = "UDDDUDUU";
        String s2 = "DDUUUUDD";

        Assert.assertTrue(TrainingUnitTests.countingValleys(s1.length(), s1) == 1);
        Assert.assertTrue(TrainingUnitTests.countingValleys(s2.length(), s2) == 1);
    }

    // Complete the countingValleys function below.
    static int countingValleys(int n, String s) {
        List<String> stepsList = s.chars().mapToObj(c -> Character.toString((char)c)).collect(Collectors.toList());

        List<Integer> altitudeList = stepsList.stream().map(step -> {
            if (step.equals("U")) {
                return Integer.valueOf(1);
            } else {
                return Integer.valueOf(-1);
            }
        }).collect(Collectors.toList());

        List<Integer> totalValley = new ArrayList<>();

        altitudeList.stream().reduce(0, (lastAltitude, altitude) -> {
            lastAltitude = lastAltitude + altitude;
            if (lastAltitude == 0 && altitude == 1) {
                totalValley.add(Integer.valueOf(1)); // valley found!
            }
            return lastAltitude;
        });


        return totalValley.size();
    }

    @Test
    public void testJumpingOnClouds() {
        int [] array_1 = new int[] {0, 0, 1, 0, 0, 1, 0}; // return 4
        int [] array_2 = new int[] {0, 1, 0, 0, 0, 1, 0}; // return 3
        int [] array_3 = new int[] {0, 0, 1, 0, 0, 0, 0, 1, 0, 0}; // return 6
        int [] array_4 = new int[] {0, 0, 0, 0, 1, 0}; // return 3

        Assert.assertTrue(TrainingUnitTests.jumpingOnClouds(array_1) == 4);
        Assert.assertTrue(TrainingUnitTests.jumpingOnClouds(array_2) == 3);
        Assert.assertTrue(TrainingUnitTests.jumpingOnClouds(array_3) == 6);
        Assert.assertTrue(TrainingUnitTests.jumpingOnClouds(array_4) == 3);
    }

    // Complete the jumpingOnClouds function below.
    static int jumpingOnClouds(int[] c) {
        List<Integer> cloudsList = Arrays.stream(c).boxed().collect(Collectors.toList());

        List<List<Integer>> splitTrackList = new ArrayList<>();
        int minJumpCountForAllTracks = cloudsList.stream().reduce(0, (lastCount, cloud) -> {
            if (splitTrackList.size() <= 0) {
                splitTrackList.add(new ArrayList<>());
            }

            if (cloud == 0) {
                splitTrackList.get(splitTrackList.size()-1).add(cloud);
            } else {
                List<Integer> newTrack = new ArrayList<>();
                newTrack.add(cloud);
                splitTrackList.add(newTrack);
            }

            lastCount = splitTrackList.size() - 1;
            return lastCount;
        });

        int reducedJumpCountWithinTrack = 0;
        for (List<Integer> trackList:splitTrackList) {
            int cumulusCloudCount = trackList.stream().filter(cloud -> cloud == 0).collect(Collectors.toList()).size();
            if (cumulusCloudCount > 1) {
                reducedJumpCountWithinTrack += cumulusCloudCount / 2;
            }
        }

        return minJumpCountForAllTracks + reducedJumpCountWithinTrack;
    }

    @Test
    public void testRepeatedString() {
        String s0 = "abcac";
        long l0 = 10L;

        String s1 = "aba";
        long l1 = 10L;

        String s2 = "a";
        long l2 = 1000000000000L;

        Assert.assertTrue(TrainingUnitTests.repeatedString(s0, l0) == 4);
        Assert.assertTrue(TrainingUnitTests.repeatedString(s1, l1) == 7);
        Assert.assertTrue(TrainingUnitTests.repeatedString(s2, l2) == l2);
    }

    // Complete the repeatedString function below.
    static long repeatedString(String s, long n) {
        List<String> letterList = s.toLowerCase().chars().mapToObj(c -> Character.toString((char)c)).collect(Collectors.toList());
        int count_of_a_in_each_string = letterList.stream().filter(string -> string.equalsIgnoreCase("a")).collect(Collectors.toList()).size();

        long quotient = n/s.length();
        int remainder = (int)(n % s.length());

        String remainderString = s.substring(0, remainder);
        List<String> remainderLetterList = remainderString.toLowerCase().chars().mapToObj(c -> Character.toString((char)c)).collect(Collectors.toList());
        int count_of_a_in_remainder_string = remainderLetterList.stream().filter(string -> string.equalsIgnoreCase("a")).collect(Collectors.toList()).size();

        long total_count_of_a = (count_of_a_in_each_string * quotient) + count_of_a_in_remainder_string;

        return Long.valueOf(total_count_of_a);
    }

    @Test
    public void testCheckMagazine() {
        String [] m0 = new String[] {"attack", "at", "dawn"};
        String [] n0 = new String[] {"Attack", "at", "Dawn"};
        Assert.assertTrue(TrainingUnitTests.checkMagazine(m0, n0) == false);

        String [] m1 = new String[] {"give", "me", "one", "grand", "today", "night"};
        String [] n1 = new String[] {"give", "one", "grand", "today"};
        Assert.assertTrue(TrainingUnitTests.checkMagazine(m1, n1) == true);

        String [] m2 = new String[] {"two", "times", "three", "is", "not" , "four"};
        String [] n2 = new String[] {"two", "times", "two", "is", "four"};
        Assert.assertTrue(TrainingUnitTests.checkMagazine(m2, n2) == false);

        String [] m3 = new String[] {"ive", "got", "a", "lovely", "bunch", "of", "coconuts"};
        String [] n3 = new String[] {"ive", "got", "some", "coconuts"};
        Assert.assertTrue(TrainingUnitTests.checkMagazine(m3, n3) == false);
    }


    // Complete the checkMagazine function below.
    static boolean checkMagazine(String[] magazine, String[] note) {
        Map<String, Long> magazineWordCountMap = Arrays.stream(magazine).collect(Collectors.groupingBy(string -> string, Collectors.counting()));
        Map<String, Long> noteWordCountMap = Arrays.stream(note).collect(Collectors.groupingBy(string -> string, Collectors.counting()));

        List<Boolean> searchResultList = noteWordCountMap.entrySet().stream().map(item -> {
            Long totalCountInNote = item.getValue();

            Long totalCountInMagzaine = magazineWordCountMap.get(item.getKey()) == null ? 0L:magazineWordCountMap.get(item.getKey());

            if (totalCountInMagzaine >= totalCountInNote) {
                return Boolean.valueOf(true);
            } else {
                return Boolean.valueOf(false);
            }

        }).collect(Collectors.toList());

        boolean ransomNoteFound = false;
        if (searchResultList.stream().filter(result -> result.booleanValue() == false).count() == 0L) {
            ransomNoteFound = true;
        }

        System.out.println(ransomNoteFound == true ? "Yes":"No");
        return ransomNoteFound;
    }

    @Test
    public void testTwoStrings() {
        String s1 = "hello", s2 = "world";
        Assert.assertTrue(TrainingUnitTests.twoStrings(s1, s2).equals("YES"));

//        String s1 = "hi", s2 = "world";
//        Assert.assertTrue(TrainingUnitTests.twoStrings(s1, s2).equals("NO"));
    }

    // Complete the twoStrings function below.
    static String twoStrings(String s1, String s2) {
        Map<String, Long> s1_letterCountMap = s1.chars().mapToObj(c -> Character.toString((char)c)).collect(Collectors.groupingBy(string -> string, Collectors.counting()));
        Map<String, Long> s2_letterCountMap = s2.chars().mapToObj(c -> Character.toString((char)c)).collect(Collectors.groupingBy(string -> string, Collectors.counting()));

        boolean found = false;
        for (Map.Entry<String,Long> item:s1_letterCountMap.entrySet()) {
            if (s2_letterCountMap.get(item.getKey()) != null) {
                found = true;
                break;
            }
        }

        return found == true ? "YES":"NO";
    }

    @Test
    public void testSherlockAndAnagramsThread() throws InterruptedException {
        Map<String, Integer> sInputMap = new HashMap<>();
//        sInputMap.put("kkkk", 10);
//        sInputMap.put("abba", 4);
//        sInputMap.put("abcd", 0);
//        sInputMap.put("ifailuhkqq", 3);
//        sInputMap.put("cdcd", 5);
//        sInputMap.put("ifailuhkqqhucpoltgtyovarjsnrbfpvmupwjjjfiwwhrlkpekxxnebfrwibylcvkfealgonjkzwlyfhhkefuvgndgdnbelgruel", 399);
//        sInputMap.put("gffryqktmwocejbxfidpjfgrrkpowoxwggxaknmltjcpazgtnakcfcogzatyskqjyorcftwxjrtgayvllutrjxpbzggjxbmxpnde", 471);
//        sInputMap.put("mqmtjwxaaaxklheghvqcyhaaegtlyntxmoluqlzvuzgkwhkkfpwarkckansgabfclzgnumdrojexnrdunivxqjzfbzsodycnsnmw", 370);
//        sInputMap.put("ofeqjnqnxwidhbuxxhfwargwkikjqwyghpsygjxyrarcoacwnhxyqlrviikfuiuotifznqmzpjrxycnqktkryutpqvbgbgthfges", 403);
//        sInputMap.put("zjekimenscyiamnwlpxytkndjsygifmqlqibxxqlauxamfviftquntvkwppxrzuncyenacfivtigvfsadtlytzymuwvpntngkyhw", 428);
        sInputMap.put("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 166650);
        sInputMap.put("bbcaadacaacbdddcdbddaddabcccdaaadcadcbddadababdaaabcccdcdaacadcababbabbdbacabbdcbbbbbddacdbbcdddbaaa", 4832);
        sInputMap.put("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 166650);
        sInputMap.put("cacccbbcaaccbaacbbbcaaaababcacbbababbaacabccccaaaacbcababcbaaaaaacbacbccabcabbaaacabccbabccabbabcbba", 13022);
        sInputMap.put("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 166650);
        sInputMap.put("bbcbacaabacacaaacbbcaabccacbaaaabbcaaaaaaaccaccabcacabbbbabbbbacaaccbabbccccaacccccabcabaacaabbcbaca", 9644);
        sInputMap.put("cbaacdbaadbabbdbbaabddbdabbbccbdaccdbbdacdcabdbacbcadbbbbacbdabddcaccbbacbcadcdcabaabdbaacdccbbabbbc", 6346);
        sInputMap.put("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 166650);
        sInputMap.put("babacaccaaabaaaaaaaccaaaccaaccabcbbbabccbbabababccaabcccacccaaabaccbccccbaacbcaacbcaaaaaaabacbcbbbcc", 8640);
        sInputMap.put("bcbabbaccacbacaacbbaccbcbccbaaaabbbcaccaacaccbabcbabccacbaabbaaaabbbcbbbbbaababacacbcaabbcbcbcabbaba", 11577);

        Map<String, Integer> sOutputMap = new HashMap<>();

        final CountDownLatch latch = new CountDownLatch(sInputMap.size());

        sInputMap.forEach((key,value) -> {
            Thread t = new Thread(()->{
                int result = TrainingUnitTests.sherlockAndAnagrams(key);
                sOutputMap.put(key, result);
                latch.countDown();
            });
            t.start();
        });

        latch.await();

        sOutputMap.forEach((key,value) -> {
            Integer count = sInputMap.get(key);
            Assert.assertTrue(value.intValue() == count.intValue());
        });
    }

    @Test
    public void testSherlockAndAnagramsFuture() throws InterruptedException, TimeoutException, ExecutionException {
        Map<String, Integer> sInputMap = new HashMap<>();
//        sInputMap.put("kkkk", 10);
//        sInputMap.put("abba", 4);
//        sInputMap.put("abcd", 0);
//        sInputMap.put("ifailuhkqq", 3);
//        sInputMap.put("cdcd", 5);
//        sInputMap.put("ifailuhkqqhucpoltgtyovarjsnrbfpvmupwjjjfiwwhrlkpekxxnebfrwibylcvkfealgonjkzwlyfhhkefuvgndgdnbelgruel", 399);
//        sInputMap.put("gffryqktmwocejbxfidpjfgrrkpowoxwggxaknmltjcpazgtnakcfcogzatyskqjyorcftwxjrtgayvllutrjxpbzggjxbmxpnde", 471);
//        sInputMap.put("mqmtjwxaaaxklheghvqcyhaaegtlyntxmoluqlzvuzgkwhkkfpwarkckansgabfclzgnumdrojexnrdunivxqjzfbzsodycnsnmw", 370);
//        sInputMap.put("ofeqjnqnxwidhbuxxhfwargwkikjqwyghpsygjxyrarcoacwnhxyqlrviikfuiuotifznqmzpjrxycnqktkryutpqvbgbgthfges", 403);
//        sInputMap.put("zjekimenscyiamnwlpxytkndjsygifmqlqibxxqlauxamfviftquntvkwppxrzuncyenacfivtigvfsadtlytzymuwvpntngkyhw", 428);
        sInputMap.put("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 166650);
        sInputMap.put("bbcaadacaacbdddcdbddaddabcccdaaadcadcbddadababdaaabcccdcdaacadcababbabbdbacabbdcbbbbbddacdbbcdddbaaa", 4832);
        sInputMap.put("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 166650);
        sInputMap.put("cacccbbcaaccbaacbbbcaaaababcacbbababbaacabccccaaaacbcababcbaaaaaacbacbccabcabbaaacabccbabccabbabcbba", 13022);
        sInputMap.put("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 166650);
        sInputMap.put("bbcbacaabacacaaacbbcaabccacbaaaabbcaaaaaaaccaccabcacabbbbabbbbacaaccbabbccccaacccccabcabaacaabbcbaca", 9644);
        sInputMap.put("cbaacdbaadbabbdbbaabddbdabbbccbdaccdbbdacdcabdbacbcadbbbbacbdabddcaccbbacbcadcdcabaabdbaacdccbbabbbc", 6346);
        sInputMap.put("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 166650);
        sInputMap.put("babacaccaaabaaaaaaaccaaaccaaccabcbbbabccbbabababccaabcccacccaaabaccbccccbaacbcaacbcaaaaaaabacbcbbbcc", 8640);
        sInputMap.put("bcbabbaccacbacaacbbaccbcbccbaaaabbbcaccaacaccbabcbabccacbaabbaaaabbbcbbbbbaababacacbcaabbcbcbcabbaba", 11577);

        Map<String, Future<Integer>> sFutureMap = new HashMap<>();

        ExecutorService pool = Executors.newFixedThreadPool(5);

        sInputMap.forEach((key,value) -> {
            Future<Integer> future = pool.submit(()->TrainingUnitTests.sherlockAndAnagrams(key));
            sFutureMap.put(key, future);
        });

        sFutureMap.forEach((key,value) -> {
            Future<Integer> future = value;
            try {
//                Integer count = future.get(1000, TimeUnit.MILLISECONDS);
                Integer count = future.get();
                Assert.assertTrue(count.intValue() == sInputMap.get(key).intValue());
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail(key);
            }
        });
    }

    @Test
    public void testSherlockAndAnagrams() {
        List<String> sInputList = new ArrayList<>();
//        sInputList.add("kkkk:10");
//        sInputList.add("abba:4");
//        sInputList.add("abcd:0");
//        sInputList.add("ifailuhkqq:3");
//        sInputList.add("cdcd:5");
        sInputList.add("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa:166650");
        sInputList.add("bbcaadacaacbdddcdbddaddabcccdaaadcadcbddadababdaaabcccdcdaacadcababbabbdbacabbdcbbbbbddacdbbcdddbaaa:4832");
        sInputList.add("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa:166650");
        sInputList.add("cacccbbcaaccbaacbbbcaaaababcacbbababbaacabccccaaaacbcababcbaaaaaacbacbccabcabbaaacabccbabccabbabcbba:13022");
        sInputList.add("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa:166650");
        sInputList.add("bbcbacaabacacaaacbbcaabccacbaaaabbcaaaaaaaccaccabcacabbbbabbbbacaaccbabbccccaacccccabcabaacaabbcbaca:9644");
        sInputList.add("cbaacdbaadbabbdbbaabddbdabbbccbdaccdbbdacdcabdbacbcadbbbbacbdabddcaccbbacbcadcdcabaabdbaacdccbbabbbc:6346");
        sInputList.add("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa:166650");
        sInputList.add("babacaccaaabaaaaaaaccaaaccaaccabcbbbabccbbabababccaabcccacccaaabaccbccccbaacbcaacbcaaaaaaabacbcbbbcc:8640");
        sInputList.add("bcbabbaccacbacaacbbaccbcbccbaaaabbbcaccaacaccbabcbabccacbaabbaaaabbbcbbbbbaababacacbcaabbcbcbcabbaba:11577");

        List<Integer> sOutputList = new ArrayList<>();
        Map<String, Integer> cachedResultMap = new HashMap<>();

        sInputList.forEach(string -> {
            String key = string.split(":")[0];

            Integer cachedResult = cachedResultMap.get(key);
            int result;
            if (cachedResult == null) {
                result = TrainingUnitTests.sherlockAndAnagrams(key);
                cachedResultMap.put(key,result);
            } else {
                result = cachedResult.intValue();
            }
            sOutputList.add(result);
        });

        for (int i=0; i<sOutputList.size(); i++) {
            Integer count = sOutputList.get(i);
            Integer expectedCount = Integer.valueOf(sInputList.get(i).split(":")[1]);
            System.out.println(count);
            Assert.assertTrue(count.intValue() == expectedCount.intValue());
        }
    }

    private static class Patterns {
        Map<String, Set<Integer>> patternMap;
        Map<String, char[]> patternLetterArrayMap;
    }

    // Complete the sherlockAndAnagrams function below.
    static int sherlockAndAnagrams(String s) {
        List<String> stringLetterList = s.toLowerCase().chars().mapToObj(c -> Character.toString((char)c)).collect(Collectors.toList());

        Patterns patterns = scanPattern(s);
        Map<String, Set<Integer>> patternMap = patterns.patternMap;
        Map<String, char[]> patternLetterArrayMap = patterns.patternLetterArrayMap;

        Map<String, Set<String>> anagramsPairMap = new HashMap<>();

        Map<String, char[]> cachedCharArrayMap = new HashMap<>();

        patternMap.forEach((searchPattern, searchPatternPositionSet) -> {
            //System.out.println("searching for pattern: " + searchPattern);

            char [] searchPatternCharArray = patternLetterArrayMap.get(searchPattern);

            searchPatternPositionSet.forEach(searchPatternPosition -> {
                Map<String, Set<String>> tempAnagramsPairMap = searchAnagram(
                        s,
                        stringLetterList,
                        searchPattern,
                        searchPatternPosition,
                        searchPatternCharArray,
                        cachedCharArrayMap);

                // validate position set to remove duplicate
                tempAnagramsPairMap.forEach((key, value) -> {
                    String tempAnagramPair = key;
                    Set<String> tempAnagramPairPositionSet = value;

                    Set<String> anagramPairPositionSet = anagramsPairMap.get(key);

                    if (anagramPairPositionSet == null) {
                        // retry with reversed key
                        String key1 = key.split(",")[0];
                        String key2 = key.split(",")[1];
                        tempAnagramPair = key2 + "," + key1;
                        anagramPairPositionSet = anagramsPairMap.get(tempAnagramPair);
                        if (anagramPairPositionSet == null) {
                            tempAnagramPair = key;
                        }
                    }

                    for (String tempAnagramPairPosition:tempAnagramPairPositionSet) {
                        if (anagramPairPositionSet == null) {
                            anagramPairPositionSet = new HashSet<>();
                            anagramPairPositionSet.add(tempAnagramPairPosition);
                        } else {
                            // search if the position already exist
                            if (!anagramPairPositionSet.contains(tempAnagramPairPosition)) {
                                // retrying the search by reversing the position
                                int index1 = Integer.parseInt(tempAnagramPairPosition.split(",")[0]);
                                int index2 = Integer.parseInt(tempAnagramPairPosition.split(",")[1]);
                                String tempAnagramPairPositionReversed = index2 + "," + index1;
                                if (!anagramPairPositionSet.contains(tempAnagramPairPositionReversed)) {
                                    anagramPairPositionSet.add(tempAnagramPairPosition);
                                }
                            }
                        }
                    }

                    anagramsPairMap.put(tempAnagramPair, anagramPairPositionSet);
                    tempAnagramPairPositionSet.clear();
                });

                tempAnagramsPairMap.clear();
            });
        });

        long anagramPairsCount = anagramsPairMap.entrySet().stream().map(entrySet -> entrySet.getValue().size()).reduce(0, (lastCount, pairs) -> {
            lastCount = lastCount + pairs;
            return lastCount;
        });

        anagramsPairMap.clear();
        return Long.valueOf(anagramPairsCount).intValue();
    }

    private static Patterns scanPattern(String s) {
        // generate all possible patterns in the string and their appearing position in the string
        Map<String, Set<Integer>> patternMap = new HashMap<>();
        Map<String, char[]> patternLetterArrayMap = new HashMap<>();

        for (int i=1; i<s.length(); i++) {
            for (int j=0; j<s.length(); j++) {
                if (j+i <= s.length()) {
                    String pattern = s.substring(j,j+i);
                    //System.out.println("pattern " + pattern + " found: at (" + String.format("%s,%s",j,j+pattern.length()) + ")");

                    Set <Integer> patternPositionSet = patternMap.get(pattern);
                    if (patternPositionSet == null) {
                        patternPositionSet = new HashSet<>();
                        patternPositionSet.add(Integer.valueOf(j));
                        patternMap.put(pattern, patternPositionSet);

                        char[] patternCharsArray = pattern.toCharArray();
                        Arrays.sort(patternCharsArray);
                        patternLetterArrayMap.put(pattern, patternCharsArray);

                    } else {
                        patternPositionSet.add(Integer.valueOf(j));
                        patternMap.put(pattern, patternPositionSet);
                    }
                } else {
                    break;
                }
            }
        }

        Patterns patterns = new Patterns();
        patterns.patternMap = patternMap;
        patterns.patternLetterArrayMap = patternLetterArrayMap;
        return patterns;
    }

    private static Map<String, Set<String>> searchAnagram(
            String s,
            List<String> stringLetterList,
            String searchPattern,
            Integer searchPatternPosition,
            char[] searchPatternCharsArray,
            Map<String, char[]> cachedCharArrayMap
    ) {
        Map<String, Set<String>> anagramsPairMap = new HashMap<>();

        stringLetterList.stream().reduce(s, (lastString, string) -> {
            int position = s.length() - lastString.length();
            //System.out.println("position: " + position);

            if (lastString.length()>= searchPattern.length()) {
                String candidatePattern = lastString.substring(0, searchPattern.length());

                int searchPatternStartIndex = searchPatternPosition.intValue();
                if (searchPatternStartIndex == position) {
                    // skip if the current position is the search pattern start position
                } else {
                    boolean match = false;

                    char[] candidatePatternCharsArray = cachedCharArrayMap.get(candidatePattern);
                    if (candidatePatternCharsArray==null) {
                        candidatePatternCharsArray = candidatePattern.toCharArray();
                        Arrays.sort(candidatePatternCharsArray);
                        cachedCharArrayMap.put(candidatePattern, candidatePatternCharsArray);
                    }
                    match = Arrays.equals(searchPatternCharsArray, candidatePatternCharsArray);

                    if (match) {
                        //System.out.println("anagram pair found at position: " + position);
                        //System.out.println("pattern: " + searchPattern);
                        //System.out.println("anagram: " + candidatePattern);

                        // generate the anagram pair string
                        String anagramPair = searchPattern + "," + candidatePattern;
                        String anagramPairReversed = candidatePattern + "," + searchPattern;

                        // generate the anagram pair position
                        String anagramPairsPosition = searchPatternPosition + "," + position;
                        String anagramPairsPositionReversed = position + "," + searchPatternPosition;

                        Set<String> positionSet = anagramsPairMap.get(anagramPair);
                        if (positionSet != null) {
                            // validate if reversed anagram position already exist
                            if (!positionSet.contains(anagramPairsPosition)) {
                                positionSet.add(anagramPairsPosition);
                                anagramsPairMap.put(anagramPair, positionSet);
                            } else {
                                // reversed position is considered duplicated
                                if (!positionSet.contains(anagramPairsPositionReversed)) {
                                    positionSet.add(anagramPairsPosition);
                                    anagramsPairMap.put(anagramPair, positionSet);
                                }
                            }

                        } else {
                            // validate if reversed anagram pair already exist
                            positionSet = anagramsPairMap.get(anagramPairReversed);
                            if (positionSet == null) {
                                positionSet = new HashSet<>();
                                positionSet.add(anagramPairsPosition);
                                anagramsPairMap.put(anagramPair, positionSet);
                            }
                        }
                    }
                }
            }

            lastString = lastString.substring(1);
            return lastString;
        });

        return anagramsPairMap;
    }

    @Test
    public void testFibonacci() {
        int n = 8;

        List<Integer> fibonacciList = TrainingUnitTests.fibonacci(n);

        int[] array = fibonacciList.stream().mapToInt(i -> i).toArray();
        int[] result = new int[] {0,1,1,2,3,5,8,13};

        Assert.assertTrue(Arrays.equals(array, result));
    }

    public static List<Integer> fibonacci(int n) {
        // Write your code here

        List<Integer> fibonacciList = new ArrayList<>();

        for (int i=0; i<n; i++) {
            if (i<2) {
                fibonacciList.add(i);
            } else {
                Integer next = fibonacciList.get(i-2) + fibonacciList.get(i-1);
                fibonacciList.add(next);
            }
        }

        return fibonacciList;
    }

    @Test
    public void testCreateBST() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        TrainingUnitTests.createBST(list);
    }

    // The variable named 'counter' counts calls to insert.
    static long count = 0L;

    /*
     * Complete the 'createBST' function below.
     *
     * The function accepts INTEGER_ARRAY keys as parameter.
     */
    public static void createBST(List<Integer> keys) {
        BinaryTree tree = new BinaryTree();

        // Write your code here
        for (Integer key : keys) {
            if (tree.root != null) {

                insert(tree.root, key);
            } else {
                tree.addRootNode(new Node(key));
            }

            System.out.println(count);
        }

        Assert.assertTrue(true);
    }

    public static Node insert(Node current, Integer key) {
        if (current == null) {
            return new Node(key);
        }

        if (key < current.value) {
            count = count + 1;
            current.left = insert(current.left, key);
        } else if (key > current.value) {
            count = count + 1;
            current.right = insert(current.right, key);
        }

        return current;
    }

    public static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
            right = null;
            left = null;
        }
    }

    public static class BinaryTree {
        Node root;

        public BinaryTree() {}

        public void addRootNode(Node node) {
            this.root = node;
        }
    }

    @Test
    public void readFile() {
        String filename = "test";
        String outputFilename = "gifs_" + filename;

        Set<String> nameSet = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String temp = line.substring(line.indexOf("GET")+4, line.length());
                temp = temp.substring(0, temp.indexOf(" "));
                temp = temp.substring(temp.lastIndexOf("/")+1, temp.length());

                if (temp.length()>0 && temp.toLowerCase().indexOf("gif")>=0) {
                    nameSet.add(temp);
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilename))) {

            for (String name:nameSet) {
                bw.write(name);
                bw.newLine();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
