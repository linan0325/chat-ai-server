package com.nan.chatai.utils;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public  class test {

    static  int total=10;

    private int data;
    public test(){

        this(10);

    }
    private test(int data){

        this.data=data;
    }

    @Override
    public String toString() {
        return "super.toString()";
    }



    public void display(){

        int total=5;
        System.out.println(this.total);

        class dec{
            public void dect(){
                data--;
            }
        }
        dec d = new dec();
        d.dect();
        System.out.println("data="+data);
    }


    static class Shape{};
    static class qu extends  Shape{};
    static class tr extends Shape{};


    public static void main() {
        System.out.println("main");
    }

    public static void main(String[] args) {
//        int data=0;
//        test t=new test();
//        t.display();
//        System.out.println("data="+data);
//        int a=9, b=2;
//        float f;
//        f= a/b;
//        System.out.println(f);
//        f=f/2;
//        System.out.println(f);
//        f=a+b/f;
//        System.out.println(f);
//        Optional<test> t = Optional.of(new test());
//        System.out.println(t.get().toString());

        List<String> strings = Arrays.asList("dog", "over", "good");
//        System.out.println(strings.stream().reduce(new String(),(x1,x2)->{
//            if(x1.equals("dog")) return x1;return x2;
//        }));

//        strings.stream().reduce((x1,x2)-> x1.length()==3? x1:x2).ifPresent(System.out::println);
//
//        LocalDate of = LocalDate.of(2015, 4, 4);
//        System.out.println(of.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
//        System.out.println(of.format(DateTimeFormatter.ofPattern("E,MMM dd, yyyy")));
//        System.out.println(of.format(DateTimeFormatter.ofPattern("MM/dd/yy")));
//
//
//        Set<String> le=new LinkedHashSet<String>();
//        le.add("3");
//        le.add("1");
//        le.add("2");
//        le.add("3");
//        le.add("3");
//        le.add("1");
//        le.forEach(s -> System.out.println(s+"-"));
//
//        Shape shape=new qu();
//        qu q = new qu();
//
//        tr t=(tr) shape;
//
//        try {
//            BufferedReader c = new BufferedReader(new FileReader("c"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

//        Supplier<String> i =()->"Car";
//        Consumer<String> c =x-> System.out.println(x.toLowerCase());
//        Consumer<String> d =x-> System.out.println(x.toUpperCase());
//        c.andThen(d).accept(i.get());
//        System.out.println();

//        Runnable r =()-> System.out.println("HI");
//        new Thread(r).start();

        test t = new test();
        t.display();

        //Integer i = Integer.parseUnsignedInt(4);
        Float aFloat = new Float(3.14);
        //Character c = new Character("c");
        Boolean aFalse = new Boolean("false");
        Double aDouble = new Double("17.4D");

        System.out.println(aFloat);
        System.out.println(aFalse);
        System.out.println(aDouble);


        System.out.println(Stream.of("green","yellow","blue")
        .max((s1,s2)-> s1.compareTo(s2)).filter(s -> s.endsWith("n")).orElse("yellow"));

        boolean b =false;
        System.out.println((b =true)?"true":"f");
        int c=0;
        System.out.println((0==c++)?"true":"f");
        String e="1";
        System.out.println(("1" !=e)?"true":"f");
        int a=0;
        System.out.println((a!=0)?"true":"f");
        Double d=null;
        System.out.println((d instanceof Double)?"true":"f");




    }
}
