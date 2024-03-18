package com.project.bloodBank.controller;

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
import com.project.bloodBank.bean.DonorBean;
import com.project.bloodBank.donorExceptions.DonorDeleteException;
import com.project.bloodBank.donorExceptions.DonorFetchException;
import com.project.bloodBank.donorExceptions.DonorIdNotFoundException;
import com.project.bloodBank.donorExceptions.DonorSaveException;
import com.project.bloodBank.donorExceptions.DonorUpdateException;
import com.project.bloodBank.entity.DonorEntity;
import com.project.bloodBank.serviceImplementation.DonorServiceImplementation;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/donor")
@Slf4j
public class DonorController {

	@Autowired
	private DonorServiceImplementation donorServiceImplementation;

	@PostMapping("/save")
	public ResponseEntity<DonorEntity> saveDonorRecord(@RequestBody DonorEntity donorEntity) {
		log.info("Saving Donor");
		try {
			DonorEntity donor = donorServiceImplementation.saveDonor(donorEntity);
			log.info("Donor Saved Successfully");
			return new ResponseEntity<DonorEntity>(donor, HttpStatus.CREATED);
		} catch (DonorSaveException e) {
			log.error("Saving of Donor Unsuccessful");
			return new ResponseEntity<DonorEntity>(HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@GetMapping("/get/{donorId}")
	public ResponseEntity<DonorEntity> getByDonorId(@PathVariable Long donorId) {
		log.info("Getting Donor by Id");
		try {
			DonorEntity byDonorId = donorServiceImplementation.getByDonorId(donorId);
			log.info("Donor with Id {} found", donorId);
			return new ResponseEntity<DonorEntity>(byDonorId, HttpStatus.OK);
		} catch (DonorIdNotFoundException e) {
			log.error("Donor with Id {} not found", donorId);
			return new ResponseEntity<DonorEntity>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/update")
	public ResponseEntity<DonorEntity> updateDonorRecord(@RequestBody DonorEntity donorEntity) {
		log.info("Updating Donor");
		try {
			donorServiceImplementation.updateDonor(donorEntity);
			log.info("Donor Updated Successfully");
			return new ResponseEntity<DonorEntity>(HttpStatus.OK);
		} catch (DonorUpdateException e) {
			log.error("Updating of Donor Unsuccessful");
			return new ResponseEntity<DonorEntity>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<DonorEntity> deleteDonorRecord(@PathVariable Long donorId) {
		log.info("Deleting Donor");
		try {
			donorServiceImplementation.deleteDonor(donorId);
			log.info("Donor Deleted Successfully");
			return new ResponseEntity<DonorEntity>(HttpStatus.OK);
		} catch (DonorDeleteException e) {
			log.error("Deleting of Donar Unsuccessful");
			return new ResponseEntity<DonorEntity>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<List<DonorEntity>> donorsList() {
		log.info("Fetching all Donors");
		try {
			List<DonorEntity> allDonors = donorServiceImplementation.getAllDonors();
			log.info("Donors Fetched Successfully");
			return new ResponseEntity<List<DonorEntity>>(allDonors, HttpStatus.OK);
		} catch (DonorFetchException e) {
			log.error("Fetching of Donors Unsuccessful");
			return new ResponseEntity<List<DonorEntity>>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<DonorBean> getUserDetails(@PathVariable Long userId) {

		log.info("Fetching User Details for Id {}", userId);
		try {
			DonorBean donorBean = donorServiceImplementation.getByUserId(userId);
			log.info("User Details Fetched Successfully");
			return new ResponseEntity<DonorBean>(donorBean, HttpStatus.OK);
		} catch (DonorIdNotFoundException e) {
			log.error("Fetching User Details Unsuccessful");
			return new ResponseEntity<DonorBean>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(path = "/userId/{id}")
	public ResponseEntity<Long> findByUserId(@PathVariable("id") Long id) {
		log.info("Finding Donor ID by User ID: {}", id);
		try {
			Long donorId = donorServiceImplementation.findDonorIdByUserId(id);
			log.info("Donor ID Found Successfully for User ID: {}", id);
			return new ResponseEntity<Long>(donorId, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error occurred while finding Donor ID by User ID: {}", id);
			return new ResponseEntity<Long>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
