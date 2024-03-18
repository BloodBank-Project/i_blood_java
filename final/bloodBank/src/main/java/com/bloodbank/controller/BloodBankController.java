package com.bloodbank.controller;

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

import com.bloodbank.entity.BloodBank;
import com.bloodbank.exception.IdNotFoundException;
import com.bloodbank.service.BloodBankService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/bloodbank")
public class BloodBankController {

	@Autowired
	private BloodBankService bloodBankService;

	@PostMapping("/save")
	public ResponseEntity<BloodBank> save(@RequestBody BloodBank bloodBank) {
		log.info("Saving Blood Bank Details: {}", bloodBank);
		try {
			bloodBankService.saveBloodBank(bloodBank);
			log.info("Blood Bank Details Saved Successfully");
			return new ResponseEntity<>(bloodBank, HttpStatus.CREATED);
		} catch (IdNotFoundException e) {
			log.error("Failed to Save Blood Bank Details");
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<BloodBank> getById(@PathVariable Long bankId) {
		log.info("Fetching Blood Bank by ID: {}", bankId);
		try {
			BloodBank bloodBank = bloodBankService.getByBankId(bankId);
			log.info("Blood Bank Details Fetched Successfully by ID: {}", bankId);
			return new ResponseEntity<>(bloodBank, HttpStatus.OK);
		} catch (IdNotFoundException e) {
			log.error("Failed to Fetch Blood Bank Details by ID: {}", bankId);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<List<BloodBank>> getAll() {
		log.info("Fetching All Blood Banks");
		try {
			List<BloodBank> banks = bloodBankService.getAllBloodBanks();
			log.info("All Blood Bank Details Fetched Successfully");
			return new ResponseEntity<>(banks, HttpStatus.OK);
		} catch (IdNotFoundException e) {
			log.error("Failed to Fetch All Blood Bank Details");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<BloodBank> delete(@PathVariable Long bankId) {
		log.info("Deleting Blood Bank Details with ID: {}", bankId);
		try {
			bloodBankService.deleteBloodBank(bankId);
			log.info("Blood Bank Details Deleted Successfully with ID: {}", bankId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (IdNotFoundException e) {
			log.error("Failed to Delete Blood Bank Details with ID: {}", bankId);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/update")
	public ResponseEntity<BloodBank> update(@RequestBody BloodBank bloodBank) {
		log.info("Updating Blood Bank Details");
		try {
			bloodBankService.updateBloodBank(bloodBank);
			log.info("Blood Bank Details Updated Successfully");
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (IdNotFoundException e) {
			log.error("Failed to Update Blood Bank Details");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/location/{location}")
	public ResponseEntity<List<BloodBank>> findByLocation(@PathVariable String location) {
		log.info("Finding Blood Banks by Location: {}", location);
		try {
			List<BloodBank> list = bloodBankService.findByBloodBankLocation(location);
			log.info("Blood Banks Fetched Successfully by Location: {}", location);
			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (IdNotFoundException e) {
			log.error("Failed to Fetch Blood Banks by Location: {}", location);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
