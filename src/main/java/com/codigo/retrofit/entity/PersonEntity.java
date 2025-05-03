package com.codigo.retrofit.entity;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "persons")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String names;
    private String lastName;
    private String motherLastName;
    private String fullName;
    private String typeDocument;
    @Column(unique = true)
    private String numberDocument;
    private String checkDigit;
    private String status;
    private Timestamp dateCreated;
    private String userCreated;

}