package me.adjoined.gulimall.playground;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Person {

    private String name;
    private int age;
    private String nationality;

    public Person(String name, int age, String nationality) {
        this.name = name;
        this.age = age;
        this.nationality = nationality;
    }

    public Person(String name, int age) {
        this(name, age, "");
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getNationality() {
        return nationality;
    }
}

class User {
    private String name;
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }
}

public class OptionalTest {
    public static void main(String[] args) {


        Person sara = new Person("Sara", 4, "Norwegian");
        Person viktor = new Person("Viktor", 40, "Serbian");
        Person eva = new Person("Eva", 42, "Norwegian");
        Person anna = new Person("Anna", 5, "China");
        List<Person> p = Arrays.asList(sara, viktor, eva, anna);



        String s = "agc";
        System.out.println(Optional.ofNullable(s).orElse("bbb"));


        Person throne = Optional.ofNullable(sara)
                .filter(per -> per.getName() == "Sara1")
                .orElseGet(() -> {
                    return new Person("Tian", 39, "Canada");
                });
        System.out.println(throne.getName());

        Optional.ofNullable(anna)
                .ifPresent(per -> System.out.println(per.getName()));

        System.out.println("---map---");
        Optional.ofNullable(anna)
                .map(Person::getName).ifPresent(System.out::println);

        System.out.println("---flatMap---");
        User u = new User();
        System.out.println(Optional.of(u).flatMap(User::getName).orElse("yoyo"));


        System.out.println("----Stream----");
        System.out.println("---odd even---");
        System.out.println(getString(Arrays.asList(1, 2, 4, 3, 7, 9, 8)));


        System.out.println("---name to string---");
        System.out.println(namesToString(p));

        System.out.println("---nationality groups---");
        for (Map.Entry<String, List<Person>> entry : groupByNationality(p).entrySet()) {
            System.out.println("Key=" + entry.getKey());
            entry.getValue().forEach(per -> System.out.println(per.getName()));
        }

        System.out.println("---kids name---");
        getKidNames(p).forEach(System.out::println);

        System.out.println("---sum---");
        System.out.println(calculate(Arrays.asList(1,2,3,4,5)));

        System.out.println("---oldest person---");
        System.out.println(getOldestPerson(p).getName());

        System.out.println("---flat---");
        List<List<String>> collection = Arrays.asList(Arrays.asList("Viktor", "Farcic"), Arrays.asList("John", "Doe", "Third"));
        transform(collection).forEach(System.out::println);

        System.out.println("---uppercase---");
        mapToUppercase("My", "name", "is", "John", "Doe").forEach(System.out::println);

        System.out.println("---number letter---");
        System.out.println(getTotalNumberOfLettersOfNamesLongerThanFive("william", "jones", "aaron", "seppe", "frank", "gilliam"));

    }

    public static int getTotalNumberOfLettersOfNamesLongerThanFive(String... names) {
        return (int) Stream.of(names).filter(name -> name.length() > 5).count();
    }

    public static Collection<String> mapToUppercase(String... names) {
        return Stream.of(names).map(String::toUpperCase).collect(Collectors.toList());
    }

    public static List<String> transform(List<List<String>> collection) {
        return collection.stream().flatMap(c -> c.stream()).collect(Collectors.toList());
    }

    public static Person getOldestPerson(List<Person> people) {
        return people.stream().max(Comparator.comparing(Person::getAge)).get();
    }

    public static int calculate(List<Integer> numbers) {
//        return numbers.stream().mapToInt(n -> n).sum();
        return numbers.stream().reduce(0, (total, number) -> total + number);
    }

    public static Set<String> getKidNames(List<Person> people) {
        return people.stream()
                .filter(person -> person.getAge() < 18) // Filter kids (under age of 18)
                .map(Person::getName) // Map Person elements to names
                .collect(Collectors.toSet()); // Collect values to a Set
    }

    public static Map<Boolean, List<Person>> partitionAdults(List<Person> people) {
        return people.stream()
                .collect(Collectors.partitioningBy(p -> p.getAge() >= 18));
    }


    public static Map<String, List<Person>> groupByNationality(List<Person> people) {
        return people.stream() // Convert collection to Stream
                .collect(Collectors.groupingBy(Person::getNationality)); // Group people by nationality
    }


    public static String namesToString(List<Person> people) {
        return people.stream()
                .map(Person::getName)
                .collect(Collectors.joining(", ", "Names: ", "."));
    }

    public static String getString(List<Integer> list) {
        return list.stream().map(i -> {
            if (i % 2 == 0) return "e" + i;
            return "o" + i;
        }).collect(Collectors.joining(","));
    }
}
