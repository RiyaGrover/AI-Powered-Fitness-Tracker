package com.fitness.UserService.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "habits")
public class Habit {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String userId;

        private String name;   // ✅ Lombok will generate getName() & setName()

        private String description;

        private String frequency; // daily, weekly, custom

        private Boolean completed = false;
        private Integer goalCount;
        private Integer active;
        private Integer progressCount = 0;
}
