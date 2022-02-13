package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.Repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.Repository.PetRepository;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    private PetRepository petRepository;
    private CustomerRepository customerRepository;

    @Autowired
    public CustomerService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }


    public Customer customerByCustomerID(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(NotFoundException::new);
    }

    public Customer saveCustomer(Customer customer) {
        List<Pet> petList = new ArrayList<>();
        if (petList != null) {
            petList = petList.stream().collect(Collectors.toList());
        }
        customer.setPets(petList);
        return customerRepository.save(customer);
    }

    public List<Customer> allCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerByPetId(long petID) {
        return petRepository.findById(petID).orElseThrow(NotFoundException::new).getCustomer();
    }

}
