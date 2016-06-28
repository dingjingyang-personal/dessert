package com.dessert.sys.common.test;


import com.google.common.base.*;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.*;
import com.google.common.primitives.Ints;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by admin-ding on 2016/5/30.
 */
public class GoogleGuavaDemo {

    public static void main(String[] args) {

    }


    @Test
    public void TestLoadingCache() {

        LoadingCache<String, String> cacheBuilder = CacheBuilder.newBuilder().build(new CacheLoader<String, String>() {
            @Override
            public String load(String s) throws Exception {
                String str = "hello" + s;
                return str;
            }
        });

        System.out.println("xiaoming" + cacheBuilder.apply("xdddd"));
    }

    @Test
    public void testcallableCache() throws ExecutionException {
        Cache<String, String> cache = CacheBuilder.newBuilder().maximumSize(1000).build();
        String reStr = cache.get("xiaoming", new Callable<String>() {
            @Override
            public String call() throws Exception {
                String str = "heello" + "xiaoming";
                return str;
            }
        });

        System.out.println(reStr);

    }

    @Test
    public void TableTest() {
        Table<String, Integer, String> aTable = HashBasedTable.create();

        for (char a = 'A'; a <= 'C'; ++a) {
            for (Integer b = 1; b <= 3; ++b) {
                aTable.put(Character.toString(a), b, String.format("%c%d", a, b));
            }
        }

        System.out.println(aTable.column(2));
        System.out.println(aTable.row("B"));
        System.out.println(aTable.get("B", 2));

        System.out.println(aTable.contains("D", 1));
        System.out.println(aTable.containsColumn(3));
        System.out.println(aTable.containsRow("C"));
        System.out.println(aTable.columnMap());
        System.out.println(aTable.rowMap());

        System.out.println(aTable.remove("B", 3));
    }

    @Test
    public void logMapTest() {
        Map<Integer, String> logfileMap = Maps.newHashMap();
        logfileMap.put(1, "a.log");
        logfileMap.put(2, "b.log");
        logfileMap.put(3, "c.log");
        System.out.println("logfileMap:" + logfileMap);
    }

    @Test
    public void BimapTest() {
        BiMap<Integer, String> logfileMap = HashBiMap.create();
        logfileMap.put(1, "a.log");
        logfileMap.put(2, "b.log");
        logfileMap.put(3, "c.log");
        logfileMap.put(4, "c.log");
        System.out.println("logfileMap:" + logfileMap);
        BiMap<String, Integer> filelogMap = logfileMap.inverse();
        System.out.println("filelogMap:" + filelogMap);
    }



    @Test
    public void testThrowables() {
        try {
            throw new Exception();
        } catch (Throwable t) {
            String ss = Throwables.getStackTraceAsString(t);
            System.out.println("s----------s:" + ss);
            Throwables.propagate(t);
        }
    }

    @Test
    public void call() throws IOException {
        try {
            throw new IOException();
        } catch (Throwable t) {
            Throwables.propagateIfInstanceOf(t, IOException.class);
            throw Throwables.propagate(t);
        }
    }

    @Test
    public void equalTest() {
        System.out.println(Objects.equal("a", "a"));
        System.out.println(Objects.equal(null, "a"));
        System.out.println(Objects.equal("a", null));
        System.out.println(Objects.equal(null, null));
    }


    @Test
    public void Preconditions() throws Exception {

        getPersonByPrecondition(8, "peida");

        try {
            getPersonByPrecondition(-9, "peida");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            getPersonByPrecondition(8, "");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            getPersonByPrecondition(8, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getPersonByPrecondition(int age, String neme) throws Exception {
        Preconditions.checkNotNull(neme, "neme为null");
        Preconditions.checkArgument(neme.length() > 0, "neme为\'\'");
        Preconditions.checkArgument(age > 0, "age 必须大于0");
        System.out.println("a person age:" + age + ",neme:" + neme);
    }

    @Test
    public void testOptional() throws Exception {
        Optional<Integer> possible = Optional.of(6);
        if (possible.isPresent()) {
            System.out.println("possible isPresent:" + possible.isPresent());
            System.out.println("possible value:" + possible.get());
        }
    }

    @Test
    public void testinter() throws Exception {
        Class integer = Integer.class.getDeclaredClasses()[0];
        Field field = integer.getDeclaredField("cache");
        field.setAccessible(true);
        Integer[] array = (Integer[]) field.get(integer);
        array[130] = array[131];
        System.out.printf("%d", 1 + 1);
    }

    @Test
    public void mapTest() {

        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        String str = mapJoiner.join(map);
        System.out.println(str);//结果如:key3=value3,key2=value2,key1=value1

        Map<String, String> join = Splitter.on("&").withKeyValueSeparator("=").split("id=123&name=green");


        int[] numbers = {1, 2, 3, 4, 5};
        String numbersAsString = Joiner.on(";").join(Ints.asList(numbers));
        System.out.println(numbersAsString);
        String numbersAsStringDirectly = Ints.join(";", numbers);


    }

    @Test
    public void collectionTest() throws FileNotFoundException {

        String concat = Stream.of("a","b","c","d").reduce(":",String::concat);

        concat =Stream.of("A","b","c","D","e","F").filter(x->x.compareTo("Z")>0).reduce(String::concat).get();

        double mianValue  = Stream.of(-1.5,1.0,-3.0,-2.0).reduce(Double.MAX_VALUE,Double::min);

        int sum = Stream.of(1,2,3,4).reduce(1,Integer::sum);
        sum = Stream.of(1,2,3,4).reduce(Integer::sum).get();





        System.out.println(concat);




    }


    @Test
    public void testLimitAndSkip() {
        List<Person> persons = new ArrayList();
        for (int i = 1; i <= 10000; i++) {
            Person person = new Person(i, "name" + i);
            persons.add(person);
        }
        List<String> personList2 = persons.stream().
                map(Person::getName).limit(10).skip(3).collect(Collectors.toList());
//        System.out.println(personList2);
    }



}




 class Person {
    public int no;
    private String name;
    public Person (int no, String name) {
        this.no = no;
        this.name = name;
    }
    public String getName() {
        System.out.println(name);
        return name;
    }
}