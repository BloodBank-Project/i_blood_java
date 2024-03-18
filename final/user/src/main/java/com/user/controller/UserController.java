package com.user.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.user.bean.UserBean;
import com.user.entity.EmailRequest;
import com.user.entity.User;
import com.user.exception.InvalidCredentialsException;
import com.user.exception.NoUsersFoundException;
import com.user.exception.PasswordUpdateException;
import com.user.exception.UserDeleteException;
import com.user.exception.UserNotFoundException;
import com.user.exception.UserSaveException;
import com.user.exception.UserUpdateException;
import com.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/save")
	public ResponseEntity<User> saveUser(@RequestBody User user) {

		log.info("User Details {}", user);
		try {
			User save = userService.saveUser(user);
			ResponseEntity<User> repResponseEntity = new ResponseEntity<>(save, HttpStatus.CREATED);
			log.info("saved Successfully");
			return repResponseEntity;
		} catch (UserSaveException e) {
			log.error("saved UnSuccessfully");
			return new ResponseEntity<User>(HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserBean> getByUserId(@PathVariable Long id) {
		log.info("fetching user:{}", id);
		try {
			UserBean user = userService.getByUserId(id);
			ResponseEntity<UserBean> responseEntity = new ResponseEntity<UserBean>(user, HttpStatus.OK);
			log.info("Details Got Successfully");
			return responseEntity;
		} catch (UserNotFoundException e) {
			log.error("Details got UnSuccessfull");
			return new ResponseEntity<UserBean>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllUsers() {
		log.info("fetching users : {}");
		try {
			List<User> users = userService.getAllUsers();
			ResponseEntity<List<User>> responseEntity = new ResponseEntity<List<User>>(users, HttpStatus.OK);
			log.info("fetched Successfully");
			return responseEntity;
		} catch (NoUsersFoundException e) {
			log.error("fetched UnSuccessfully");
			return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<User> deleteUserById(@PathVariable Long id) {
		log.info("deleted record");
		try {
			userService.deleteUserById(id);
			ResponseEntity<User> responseEntity = new ResponseEntity<User>(HttpStatus.OK);
			log.info("deleted Successsfully");
			return responseEntity;
		} catch (UserDeleteException e) {
			log.error("Deleted Unsuucessfully");
			return new ResponseEntity<User>(HttpStatus.NOT_ACCEPTABLE);
		}

	}

	@PutMapping("/update")
	public ResponseEntity<Optional<User>> updateUser(@RequestBody User user) {
		log.info("updated record");
		try {
			Optional<User> updateRecord = userService.updateUser(user);
			ResponseEntity<Optional<User>> responseEntity = new ResponseEntity<Optional<User>>(updateRecord,
					HttpStatus.OK);
			log.info("updated Successfully");
			return responseEntity;
		} catch (UserUpdateException e) {
			log.error("not updated");
			return new ResponseEntity<Optional<User>>(HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@PutMapping("/login")
	public ResponseEntity<Optional<User>> userLogin(@RequestBody User user) {

		String email = user.getEmail();
		String password = user.getPassword();

		log.info("Going to Login");
		try {
			Optional<User> optional = userService.userLogin(email, password);
			ResponseEntity<Optional<User>> responseEntity = new ResponseEntity<Optional<User>>(optional, HttpStatus.OK);
			log.info("loggged in Successfully");
			return responseEntity;
		} catch (InvalidCredentialsException e) {
			log.error("error occured while login");
			return new ResponseEntity<Optional<User>>(HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@PutMapping("/updatePassword")
	public ResponseEntity<Optional<User>> updateUserPassword(@RequestBody User user) {

		String email = user.getEmail();
		String password = user.getPassword();
		log.info(" going to update password ");
		try {
			Optional<User> optional = userService.updateUserPassword(email, password);
			ResponseEntity<Optional<User>> responseEntity = new ResponseEntity<Optional<User>>(optional, HttpStatus.OK);
			log.info("Succcessfully updated");
			return responseEntity;
		} catch (PasswordUpdateException e) {
			log.error("Password is not updated");
			return new ResponseEntity<Optional<User>>(HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@PutMapping("/updateLocation")
	public ResponseEntity<Optional<User>> updateUserLocation(@RequestBody User user) {

		String email = user.getEmail();
		Long bloodBankId = user.getBloodBankId();
		log.info(" going to update password ");
		try {
			Optional<User> optional = userService.updateUserBloodBankId(email, bloodBankId);
			ResponseEntity<Optional<User>> responseEntity = new ResponseEntity<Optional<User>>(optional, HttpStatus.OK);
			log.info("Succcessfully updated");
			return responseEntity;
		} catch (PasswordUpdateException e) {
			log.error("Password is not updated");
			return new ResponseEntity<Optional<User>>(HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@PutMapping("/updateLocationAndGroup")
	public ResponseEntity<Optional<User>> updateUserLocationAndGroup(@RequestBody User user) {

		String email = user.getEmail();
		Long bloodBankId = user.getBloodBankId();
		Long bloodGroupId = user.getBloodGroupId();

		log.info(" going to update password ");
		try {
			Optional<User> optional = userService.updateUserBloodBankIdBlood(email, bloodBankId, bloodGroupId);
			ResponseEntity<Optional<User>> responseEntity = new ResponseEntity<Optional<User>>(optional, HttpStatus.OK);
			log.info("Succcessfully updated");
			return responseEntity;
		} catch (PasswordUpdateException e) {
			log.error("Password is not updated");
			return new ResponseEntity<Optional<User>>(HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@PostMapping("/send-reset-email")
	public ResponseEntity<EmailRequest> sendResetEmail(@RequestBody EmailRequest emailRequest) {
		System.out.println(emailRequest.getEmail());
		userService.sendPasswordResetEmail(emailRequest.getEmail());
		return new ResponseEntity<EmailRequest>(emailRequest, HttpStatus.OK);
	}

	@GetMapping("/generateOtp")
	public ResponseEntity<User> generateOtpAndSendEmail(@RequestParam String email) {
		System.out.println("generate otp");
		try {
			log.info("Generate otp by using email");
			User user = userService.forgetPassword(email);
			if (user != null) {
				log.info("Generate otp by using email is done");
				return new ResponseEntity<User>(user, HttpStatus.OK);
			} else {

				return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {

			log.error("email id is not valid");
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);

		}
	}

	@PostMapping("/verify")
	public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String enteredOtp) {
		try {
			log.info("verify the otp by using email");
			if (userService.verifyOtp(email, enteredOtp)) {
				String jsonString = "{\"message\":\"Verified Successfully\"}";
				log.info("verify the  otp by using email is done");

				return ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(jsonString);
			} else {
				log.info("Sending  the invalid otp");
				String jsonString = "{message:Invalid OTP}";
				System.out.println("jsonString" + jsonString);

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json")
						.body(jsonString);
			}
		} catch (Exception e) {
			String jsonString = "{\"message\":\"wrong otp\"}";
			log.error("error handled");

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/json")
					.body(jsonString);

		}
	}
}
