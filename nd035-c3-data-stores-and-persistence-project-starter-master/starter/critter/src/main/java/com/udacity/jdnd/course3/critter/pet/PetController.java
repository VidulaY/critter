package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    PetService petService;

    @Autowired
    CustomerService customerService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = convertToEntity(petDTO);
        Pet petCreated = petService.savePet(pet);
        return convertToDto(petCreated);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.findPetById(petId);
        return convertToDto(pet);
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> pets = petService.findAllPets();
        List<PetDTO> petDTOS = new ArrayList<>();
        for(Pet pet : pets)
            petDTOS.add(convertToDto(pet));
        return petDTOS;
    }


    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        Customer customer = customerService.getCustomerById(ownerId);
        List<Pet> pets  = customer.getPets();
        List<PetDTO> petDTOS = new ArrayList<>();
        for(Pet pet : pets) {
            petDTOS.add(convertToDto(pet));
        }
        return petDTOS;
    }

    private PetDTO convertToDto(Pet pet) {
        PetDTO petDTO = modelMapper.map(pet, PetDTO.class);
        Long ownerId = pet.getCustomer().getId();
        petDTO.setOwnerId(ownerId);
        return petDTO;
    }


    private Pet convertToEntity(PetDTO petDTO) {
        Pet pet = modelMapper.map(petDTO, Pet.class);
        Long ownerId = petDTO.getOwnerId();
        if (ownerId != null) {
            Customer customer = customerService.getCustomerById(ownerId);
            pet.setCustomer(customer);
        }
        return pet;
    }

}
