package com.example.lab5.config;

import com.example.lab5.entity.Course;
import com.example.lab5.entity.Operator;
import com.example.lab5.repo.CourseRepository;
import com.example.lab5.repo.OperatorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seed(CourseRepository courseRepo, OperatorRepository operatorRepo){
        return args -> {
            if(courseRepo.count()==0){
                courseRepo.save(new Course(null, "Java Standard Edition", "SE", 200));
                courseRepo.save(new Course(null, "Java Enterprise", "EE", 350));
                courseRepo.save(new Course(null, "Spring Boot", "SB", 300));
            }
            if(operatorRepo.count()==0){
                operatorRepo.save(new Operator(null, "Едиль", "Бакенов", "Продажа"));
                operatorRepo.save(new Operator(null, "Жорже", "Мендеш", "Рекрутинг"));
                operatorRepo.save(new Operator(null, "Ержан", "Болатов", "Маркетинг"));
                operatorRepo.save(new Operator(null, "Сержан", "Арманов", "ИТ"));
                operatorRepo.save(new Operator(null, "Флорентино", "Перес", "Администрация"));
            }
        };
    }
}
