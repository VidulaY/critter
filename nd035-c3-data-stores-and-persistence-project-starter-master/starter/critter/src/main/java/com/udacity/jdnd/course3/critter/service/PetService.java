package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PetService {
    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerService customerService;

    public Pet savePet(Pet pet){
        pet = petRepository.save(pet);
        customerService.addPetToCustomer(pet, pet.getCustomer());
        return pet;
    }

    public List<Pet> findAllPets(){
        Iterable<Pet> iterable = petRepository.findAll();
        List<Pet> result = new ArrayList<>();
        iterable.forEach(result::add);
        return result;
    }

    public Pet findPetById(Long id){
        return petRepository.findById(id).get();
    }


}
