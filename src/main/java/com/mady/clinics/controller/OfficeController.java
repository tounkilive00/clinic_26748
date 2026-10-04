package com.mady.clinics.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mady.clinics.domain.Office;
import com.mady.clinics.service.OfficeService;

@RestController
@RequestMapping("/api/offices")
public class OfficeController {

    @Autowired
    private OfficeService officeService;

    @PostMapping("/save")
    public ResponseEntity<Office> saveOffice(@RequestBody Office office) {
        Office saved = officeService.saveOffice(office);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping("/{officeNumber}")
    public ResponseEntity<String> deleteOffice(@PathVariable int officeNumber) {
        officeService.deleteOffice(officeNumber);
        return new ResponseEntity<>("Office deleted successfully", HttpStatus.OK);
    }

    // delete office by number 
    @DeleteMapping("/delete/{officeNumber}")
    public ResponseEntity<String> deleteOfficeById(@PathVariable int officeNumber) {
        officeService.deleteOffice(officeNumber);
        return new ResponseEntity<>("Office deleted successfully", HttpStatus.OK);
    }

    // get all offices
    @GetMapping
    public ResponseEntity<List<Office>> getAllOffices() {
        return new ResponseEntity<>(officeService.getAllOffices(), HttpStatus.OK);
    }

    // get office by number
    @GetMapping("/{officeNumber}")
    public ResponseEntity<Office> getOfficeByNumber(@PathVariable int officeNumber) {   
    return new ResponseEntity<>(officeService.getOfficeById(officeNumber), HttpStatus.OK);
}

    // update office by number (supports PUT /api/offices/{officeNumber} and POST /api/offices/update/{officeNumber})
    @PutMapping("/{officeNumber}")
    public ResponseEntity<Office> updateOffice(@PathVariable int officeNumber, @RequestBody Office office) {
        Office existingOffice = officeService.getOfficeById(officeNumber);
        existingOffice.setOfficeName(office.getOfficeName());
        existingOffice.setOfficeNumber(office.getOfficeNumber());
        Office updated = officeService.saveOffice(existingOffice);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PostMapping("/update/{officeNumber}")
    public ResponseEntity<Office> updateOfficePost(@PathVariable int officeNumber, @RequestBody Office office) {
        return updateOffice(officeNumber, office);
    }

    // Quiz B3 endpoint: GET /api/offices/vacant
    @GetMapping("/vacant")
    public ResponseEntity<List<Office>> getVacantOffices() {
        return new ResponseEntity<>(officeService.getVacantOffices(), HttpStatus.OK);
    }

    @GetMapping("/busiest")
    public ResponseEntity<?> getBusiestOffice() {
        java.util.Optional<Object[]> busiest = officeService.getBusiestOffice();
        if (busiest.isEmpty()) {
            return new ResponseEntity<>("No appointments yet", HttpStatus.OK);
        }
        return new ResponseEntity<>(busiest.get(), HttpStatus.OK);
    }
}