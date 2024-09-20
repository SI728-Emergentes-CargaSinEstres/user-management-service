package com.upc.cargasinestres.CargaSinEstres.UsersContext.repository;

import com.upc.cargasinestres.CargaSinEstres.UsersContext.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * The IClientRepository interface extends JpaRepository for CRUD operations on Client entities.
 * It provides methods for querying clients based on email, password, and other criteria.
 * @author Grupo1
 * @version 1.0
 * */
public interface ICustomerRepository extends JpaRepository<Customer, Long> {
    /**
     * Retrieves an Optional<Client> based on the provided email and password.
     *
     * @param email The email of the client.
     * @param password The password of the client.
     * @return An Optional containing the client if found, otherwise an empty Optional.
     */
    @Query("SELECT c FROM Customer c WHERE c.email = :email AND c.password = :password")
    Optional<Customer> existsCustomer(@Param("email") String email, @Param("password") String password);

    /**
     * Retrieves a list of clients based on the provided email and password.
     *
     * @param email The email of the client.
     * @param password The password of the client.
     * @return A List of clients matching the specified email and password for login.
     */
    Customer findByEmailAndPassword(String email, String password);

    /**
     * Retrieves an Optional<Client> based on the provided phone number. This is used to avoid duplicate clients with the same phone number
     *
     * @param phoneNumber The phone number of the client.
     * @return An Optional containing the client if found, otherwise an empty Optional.
     */
    Optional<Customer> findByPhoneNumber(String phoneNumber);

    /**
     * Retrieves an Optional<Client> based on the provided email. This is used to avoid duplicate clients with the same email
     *
     * @param email The email of the client.
     * @return An Optional containing the client if found, otherwise an empty Optional.
     */
    Optional<Customer> findByEmail(String email);

}

