package com.patient.controller;

import java.util.List;
import java.util.Optional;

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

import com.patient.bean.PatientRequestBean;
import com.patient.entity.PatientRequestEntity;
import com.patient.patientRequestExceptions.PatientRequestDeleteException;
import com.patient.patientRequestExceptions.PatientRequestDetailsNotFoundException;
import com.patient.patientRequestExceptions.PatientRequestIdNotFoundException;
import com.patient.patientRequestExceptions.PatientRequestSaveException;
import com.patient.patientRequestExceptions.PatientRequestUpdateException;
import com.patient.service.PatientRequestService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/patientRequest")
public class PatientRequestController {
	@Autowired
	private PatientRequestService patientRequestService;

	@PostMapping("/save")
	public ResponseEntity<PatientRequestEntity> savePatientRequest(@RequestBody  PatientRequestEntity patientRequest) {
		log.info("Going to Save Details");
		try {
		patientRequestService.savePatientRequest(patientRequest);
		ResponseEntity<PatientRequestEntity> responseEntity = new ResponseEntity(patientRequest, HttpStatus.CREATED);
		log.info("saved Successfully");
		return responseEntity;
		}
		catch (PatientRequestSaveException e) {
			log.error("Error Occured While Saving");
			return new ResponseEntity<PatientRequestEntity>(HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<PatientRequestEntity> deletePatientRequest(@PathVariable Long id) {
		log.info("Going To Delete Patient");
		try {
			patientRequestService.deletePatientRequest(id);
			ResponseEntity<PatientRequestEntity> responseEntity = new ResponseEntity(HttpStatus.OK);
			log.info("Deleted Successfully");
			return responseEntity;
		}catch(PatientRequestDeleteException e) {
			log.error("Error Occured While Deleting");
			return new ResponseEntity<PatientRequestEntity>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(path = "/allPatients")
	public ResponseEntity<List<PatientRequestBean>> getAllPatientRequest() {
		log.info("Going to Fetch Data ");
		try {
		List<PatientRequestBean> list = patientRequestService.getAllPatientRequest();
		ResponseEntity<List<PatientRequestBean>> responseEntity = new ResponseEntity(list, HttpStatus.OK);
		log.info("Fetched Successfully");
		return responseEntity;
		}
		catch (PatientRequestDetailsNotFoundException e) {
			log.error("Error Ocuured while Fetching");
			return new ResponseEntity<List<PatientRequestBean>>(HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<PatientRequestBean> getPatientRequest(@PathVariable Long id){
		log.info("Going To Get Patient Details");
		try {
			PatientRequestBean patientRequest = patientRequestService.getPatientRequest(id);
		ResponseEntity<PatientRequestBean> patient=new ResponseEntity<>(patientRequest,HttpStatus.OK);
		log.info("Patient Details Fetch Successfully");
		return patient;
		}
		catch(PatientRequestIdNotFoundException e) {
			log.error("Error occured while Fetching patient");
			return new ResponseEntity<PatientRequestBean>(HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@PutMapping("/update")
	public ResponseEntity<Optional<PatientRequestEntity>> updatePatientRequest(@RequestBody PatientRequestEntity patientRequest) {
		log.info("Going to Update Patient Details");
		try {
		Optional<PatientRequestEntity> updatePatientRequest = patientRequestService.updatePatientRequest(patientRequest);
		ResponseEntity<Optional<PatientRequestEntity>> responseEntity = new ResponseEntity<>(updatePatientRequest,
				HttpStatus.OK);
		log.info("Updated Successfully");
		return responseEntity;}
		catch(PatientRequestUpdateException e) {
			log.error("Error Occured While Updating");
			return new ResponseEntity<Optional<PatientRequestEntity>>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/status/accepted")
	public ResponseEntity<PatientRequestEntity> updateBloodQuantity(@RequestBody  PatientRequestEntity requestEntity) {
		log.info("Updating PatientRequest Status As Accepted");
		try {
			patientRequestService.updateStatusAsAccepted(requestEntity);
			log.info("PatientRequest Status Updated Successfully");
			return new ResponseEntity<PatientRequestEntity>(HttpStatus.OK);
		} catch (PatientRequestUpdateException e) {
			log.error("Failed to Update PatientRequests", e);
			return new ResponseEntity<PatientRequestEntity>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/status/rejected")
	public ResponseEntity<PatientRequestEntity> statusReject(@RequestBody  PatientRequestEntity requestEntity) {
		log.info("Updating PatientRequest Status As Rejected");
		try {
			patientRequestService.updateStatusAsRejected(requestEntity);
			log.info("PatientRequests Status Updated Successfully");
			return new ResponseEntity<PatientRequestEntity>(HttpStatus.OK);
		} catch (PatientRequestUpdateException e) {
			log.error("Failed to Update PatientRequests", e);
			return new ResponseEntity<PatientRequestEntity>(HttpStatus.NOT_FOUND);
		}
	} 
	
	@GetMapping("/count/requests")
	public ResponseEntity<Long> patientsCount() {
		log.info("Fetching total count of patient requests");
	    try {
	        Long count = patientRequestService.patientRequestsCount();
	        log.info("Total count of patient requests fetched successfully: {}", count);
	        return new ResponseEntity<Long>(count, HttpStatus.OK);
	    } catch (Exception e) {
	        log.error("Error occurred while fetching total count of patient requests: {}", e.getMessage());
	        return new ResponseEntity<Long>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }	}
}