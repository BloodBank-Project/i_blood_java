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

import com.patient.bean.PatientBean;
import com.patient.entity.PatientEntity;
import com.patient.patientExceptions.PatientDeleteException;
import com.patient.patientExceptions.PatientDetailsNotFoundException;
import com.patient.patientExceptions.PatientIdNotFoundException;
import com.patient.patientExceptions.PatientSaveException;
import com.patient.patientExceptions.PatientUpdateException;
import com.patient.service.PatientService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/patient")
public class PatientController {

	@Autowired
	private PatientService patientService;

	@PostMapping("/save")
	public ResponseEntity<PatientEntity> savePatient(@RequestBody PatientEntity patinet) {
		log.info("Save PatientDetails", patinet);
		try {
			PatientEntity patient = patientService.savePatient(patinet);
			ResponseEntity<PatientEntity> responseEntity = new ResponseEntity(patient, HttpStatus.CREATED);
			log.info("saved Succeefully");
			return responseEntity;
		} catch (PatientSaveException e) {
			log.error("saved Unsuuccessfully");
			return new ResponseEntity<PatientEntity>(HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<PatientEntity> deletePatient(@PathVariable Long id) {
		log.info("Delete patient", id);
		try {
			patientService.deletePatient(id);
			ResponseEntity<PatientEntity> responseEntity = new ResponseEntity(HttpStatus.OK);
			log.info("deleted Succeefully");
			return responseEntity;
		} catch (PatientDeleteException e) {
			log.error("not deleted");
			return new ResponseEntity<PatientEntity>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<PatientEntity> getByPatientId(@PathVariable Long id) {
		log.info(" Going to get fetch Patient Id ");
		try {
			PatientEntity byPatientId = patientService.getByPatientId(id);
			ResponseEntity<PatientEntity> responseEntity = new ResponseEntity<>(byPatientId, HttpStatus.OK);
			log.info("Getting Patient Is Successed");
			return responseEntity;
		} catch (PatientIdNotFoundException e) {
			log.error(" Getting Of PatientId Unsuccessfull");
			return new ResponseEntity<PatientEntity>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(path = "/all")
	public ResponseEntity<List<PatientEntity>> getAllPatients() {
		log.info("Fetch allpatientdetilas", log);
		try {
			List<PatientEntity> list = patientService.getAllPatients();
			ResponseEntity<List<PatientEntity>> responseEntity = new ResponseEntity(list, HttpStatus.OK);
			log.info("fetched Successfully");
			return responseEntity;
		} catch (PatientDetailsNotFoundException e) {
			log.error("not fetched");
			return new ResponseEntity<List<PatientEntity>>(HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@PutMapping("/update")
	public ResponseEntity<PatientEntity> updatePatient(@RequestBody PatientEntity patient) {
		log.info("update Patient", patient);
		try {
			Optional<PatientEntity> update = patientService.updatePatient(patient);
			ResponseEntity<PatientEntity> responseEntity = new ResponseEntity(update, HttpStatus.OK);
			log.info("Updated Successfully");
			return responseEntity;
		} catch (PatientUpdateException e) {
			log.error("Not Updated");
			return new ResponseEntity<PatientEntity>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(path = "/user/{id}")
	public ResponseEntity<PatientBean> getUserDetails(@PathVariable Long id) {
		log.info("fetch patient()", id);
		try {
			PatientBean patientBean = patientService.getByUserId(id);
			ResponseEntity<PatientBean> responseEntity = new ResponseEntity(patientBean, HttpStatus.OK);
			log.info("Successfully fetched User Details");
			return responseEntity;
		} catch (PatientIdNotFoundException e) {
			log.error("User details are not fetched");
			return new ResponseEntity<PatientBean>(HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping(path = "/userId/{id}")
	public ResponseEntity<Long> findByUserId(@PathVariable("id") Long id) {
		log.info("Finding Patient Details by User ID: {}", id);
		try {
			Long patientId = patientService.findPatientIdByUserId(id);
			log.info("Patient Details Found Successfully for User ID: {}", id);
			return new ResponseEntity<Long>(patientId, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error occurred while finding Patient Details by User ID: {}", id);
			return new ResponseEntity<Long>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
