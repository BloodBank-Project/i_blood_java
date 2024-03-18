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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bloodBank.bean.DonorDetailsBean;
import com.project.bloodBank.donorDetailsExceptions.DonorDetailsDeleteException;
import com.project.bloodBank.donorDetailsExceptions.DonorDetailsFetchException;
import com.project.bloodBank.donorDetailsExceptions.DonorDetailsIdNotFoundException;
import com.project.bloodBank.donorDetailsExceptions.DonorDetailsSaveException;
import com.project.bloodBank.donorDetailsExceptions.DonorDetailsUpdateException;
import com.project.bloodBank.entity.DonorDetailsEntity;
import com.project.bloodBank.serviceImplementation.DonorDetailsServiceImplementation;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/details")
@Slf4j
public class DonorDetailsController {

	@Autowired
	private DonorDetailsServiceImplementation detailsServiceImplementation;

	@PostMapping("/save")
	public ResponseEntity<DonorDetailsEntity> saveDonation(@RequestBody DonorDetailsEntity donorEntity) {
		log.info("Saving Donation: {}", donorEntity);
		try {
			detailsServiceImplementation.saveDonation(donorEntity);
			log.info("Donation Details Saved Successfully");
			return new ResponseEntity<DonorDetailsEntity>(HttpStatus.CREATED);
		} catch (DonorDetailsSaveException e) {
			log.error("Failed to Save Donation", e);
			return new ResponseEntity<DonorDetailsEntity>(HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@GetMapping("/get/{donationId}")
	public ResponseEntity<DonorDetailsBean> getByDonationId(@PathVariable Long donationId) {
		log.info("Fetching Donation with ID: {}", donationId);
		try {
			DonorDetailsBean byDonorId = detailsServiceImplementation.getByDonationId(donationId);
			log.info("Donation Found Successfully");
			return new ResponseEntity<DonorDetailsBean>(byDonorId, HttpStatus.OK);
		} catch (DonorDetailsIdNotFoundException e) {
			log.error("Failed to Fetch Donation with ID: {}", donationId, e);
			return new ResponseEntity<DonorDetailsBean>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/update")
	public ResponseEntity<DonorDetailsEntity> updateDonorDetails(@RequestBody DonorDetailsEntity donorEntity) {
		log.info("Updating Donation: {}", donorEntity);
		try {
			detailsServiceImplementation.updateDonation(donorEntity);
			log.info("Donation Updated Successfully");
			return new ResponseEntity<DonorDetailsEntity>(HttpStatus.OK);
		} catch (DonorDetailsUpdateException e) {
			log.error("Failed to Update Donation", e);
			return new ResponseEntity<DonorDetailsEntity>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/delete/{donationId}")
	public ResponseEntity<DonorDetailsEntity> deleteDonorDetails(@PathVariable Long donationId) {
		log.info("Deleting Donation with ID: {}", donationId);
		try {
			detailsServiceImplementation.deleteDonation(donationId);
			log.info("Donation Deleted Successfully");
			return new ResponseEntity<DonorDetailsEntity>(HttpStatus.OK);
		} catch (DonorDetailsDeleteException e) {
			log.error("Failed to Delete Donation with ID: {}", donationId, e);
			return new ResponseEntity<DonorDetailsEntity>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<List<DonorDetailsBean>> donationsList() {
		log.info("Fetching All Donations");
		try {
			List<DonorDetailsBean> allDonars = detailsServiceImplementation.getAllDonations();
			log.info("Donations Fetched Successfully");
			return new ResponseEntity<List<DonorDetailsBean>>(allDonars, HttpStatus.OK);
		} catch (DonorDetailsFetchException e) {
			log.error("Failed to Fetch All Donations", e);
			return new ResponseEntity<List<DonorDetailsBean>>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/status/accepted")
	public ResponseEntity<DonorDetailsEntity> updateBloodQuantity(@RequestBody DonorDetailsEntity donorEntity) {
		log.info("Updating Donation Status As Accepted");
		try {
			detailsServiceImplementation.updateStatusAsAccepted(donorEntity);
			log.info("Donations Status Updated Successfully");
			return new ResponseEntity<DonorDetailsEntity>(HttpStatus.OK);
		} catch (DonorDetailsUpdateException e) {
			log.error("Failed to Update Donations", e);
			return new ResponseEntity<DonorDetailsEntity>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/status/rejected")
	public ResponseEntity<DonorDetailsEntity> statusReject(@RequestBody DonorDetailsEntity donorEntity) {
		log.info("Updating Donation Status As Rejected");
		try {
			detailsServiceImplementation.updateStatusAsRejected(donorEntity);
			log.info("Donations Status Updated Successfully");
			return new ResponseEntity<DonorDetailsEntity>(HttpStatus.OK);
		} catch (DonorDetailsUpdateException e) {
			log.error("Failed to Update Donations", e);
			return new ResponseEntity<DonorDetailsEntity>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/count/donations")
	public ResponseEntity<Long> getDonationsCount() {
		try {
			Long count = detailsServiceImplementation.donationsCount();
			return new ResponseEntity<Long>(count, HttpStatus.OK);
		} catch (DonorDetailsFetchException e) {
			return new ResponseEntity<Long>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/total/accepted")
	public ResponseEntity<Long> getTotalAcceptedStatus() {
		try {
			Long count = detailsServiceImplementation.totalAcceptedStatus();
			return new ResponseEntity<Long>(count, HttpStatus.OK);
		} catch (DonorDetailsFetchException e) {
			return new ResponseEntity<Long>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/total/rejected")
	public ResponseEntity<Long> getTotalRejectedStatus() {
		try {
			Long count = detailsServiceImplementation.totalRejectedStatus();
			return new ResponseEntity<Long>(count, HttpStatus.OK);
		} catch (DonorDetailsFetchException e) {
			return new ResponseEntity<Long>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/total/pending")
	public ResponseEntity<Long> getTotalPendingStatus() {
		try {
			Long count = detailsServiceImplementation.totalPendingStatus();
			return new ResponseEntity<Long>(count, HttpStatus.OK);
		} catch (DonorDetailsFetchException e) {
			return new ResponseEntity<Long>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/total/bloodBanks")
	public ResponseEntity<Long> getTotalBloodBanks() {
		try {
			Long count = detailsServiceImplementation.bloodBanksCount();
			return new ResponseEntity<>(count, HttpStatus.OK);
		} catch (DonorDetailsFetchException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
