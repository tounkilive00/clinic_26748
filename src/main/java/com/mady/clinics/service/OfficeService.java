package com.mady.clinics.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mady.clinics.domain.Office;
import com.mady.clinics.repository.OfficeRepository;

@Service 
public class OfficeService {
    
    @Autowired 
    private OfficeRepository officeRepository;

    // Add your service methods here to interact with the OfficeRepository
      // For example, you can create methods to find offices by name or number, save new offices, etc.  
      public Office saveOffice(Office office){
        return officeRepository.save(office);
      }
     
    // method to delete an office by number
    public void deleteOffice(int officeNumber){
        Optional<Office> office = officeRepository.findByOfficeNumber(officeNumber);
        if(office.isPresent()){
            officeRepository.delete(office.get());
        }
        else{
            throw new RuntimeException("Office not found with number: " + officeNumber);
        }
    }  

    // get all offices 
    public List<Office> getAllOffices(){
        return officeRepository.findAll();
    }

    // get office by number
    public Office getOfficeById(int officeNumber) { // Add a method to get an office by number
        Optional<Office> office = officeRepository.findByOfficeNumber(officeNumber);
        if (office.isPresent()) {
            return office.get();
        } else {
            throw new RuntimeException("Office not found with number: " + officeNumber);
        }
    }

    // get doctor by ID
    public Office getOfficeById(UUID officeId) { // Add a method to get an office by ID
        Optional<Office> office = officeRepository.findById(officeId);
        if (office.isPresent()) {
            return office.get();
        } else {
            throw new RuntimeException("Office not found with id: " + officeId);
        }
    }
}
