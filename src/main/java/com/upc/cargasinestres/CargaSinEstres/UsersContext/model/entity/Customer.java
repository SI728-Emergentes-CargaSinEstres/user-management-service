package com.upc.cargasinestres.CargaSinEstres.UsersContext.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {
    /**
     * The id of the client.
     * This is a primary key.
     * This id is generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the client.
     */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /**
     * The last name of the client.
     */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /**
     *  The phone number of the client.
     */
    @Column(name = "phone_number", length = 9, nullable = false)
    private String phoneNumber;

    /**
     * The email of the client.
     */
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * The password of the client.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * The date of birth
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;
}
