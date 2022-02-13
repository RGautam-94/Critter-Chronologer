package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.Repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.Repository.PetRepository;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PetService {

    private PetRepository petRepository;
    private CustomerRepository customerRepository;

    @Autowired
    public PetService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    public Pet savePet(Pet pet) {
        Pet savedPet = petRepository.save(pet);
        Customer customer = savedPet.getCustomer();
        if (customer != null) {
            customer.getPets().add(savedPet);
            customerRepository.save(customer);
        }
        return savedPet;
    }

    public Pet getPetById(Long petId) {
        return petRepository.findById(petId).orElseThrow(NotFoundException::new);
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public List<Pet> getPetsByCustomerId(Long customerId) {
        return petRepository.getAllByCustomerId(customerId);
    }

}
