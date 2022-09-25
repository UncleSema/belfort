package ru.ct.belfort;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class TinkoffServiceRunner {
    public static void main(String[] args) {
        SpringApplication.run(TinkoffServiceRunner.class, args);
    }
}
