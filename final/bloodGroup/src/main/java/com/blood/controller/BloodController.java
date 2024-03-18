package com.blood.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blood.entity.Blood;
import com.blood.exception.BloodServiceException;
import com.blood.exception.NoDataFoundException;
import com.blood.service.BloodService;

@RestController
@RequestMapping("/blood")
public class BloodController {
	private static Logger log = LoggerFactory.getLogger(Blood.class.getSimpleName());

	@Autowired
	private BloodService bloodService;

	@PostMapping("/save")
	public ResponseEntity<Blood> save(@RequestBody Blood blood) {
		log.info("Attempting to save Blood Group: {}", blood);
		try {
			bloodService.saveBloodGroup(blood);
			log.info("Blood group saved successfully.");
			return new ResponseEntity<>(blood, HttpStatus.CREATED);
		} catch (BloodServiceException e) {
			log.error("Failed to save blood group: {}", e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Blood> getById(@PathVariable Integer bloodId) {

		log.info("Attempting to get Blood Group with ID: {}", bloodId);
		try {
			Blood blood = bloodService.getByBloodId(bloodId);
			log.info("Successfully retrieved Blood group with ID: {}", bloodId);
			return new ResponseEntity<>(blood, HttpStatus.OK);
		} catch (NoDataFoundException e) {
			log.error("Failed to retrieve Blood group with ID: {}", bloodId);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(path = "/all")
	public ResponseEntity<List<Blood>> getAll() {

		log.info("Attempting to get all Blood Groups");
		try {
			List<Blood> bloodGroups = bloodService.getAllBloodGroups();
			log.info("Successfully fetched all Blood Groups");
			return new ResponseEntity<>(bloodGroups, HttpStatus.OK);
		} catch (BloodServiceException e) {
			log.error("Failed to fetch all Blood Groups: {}", e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
