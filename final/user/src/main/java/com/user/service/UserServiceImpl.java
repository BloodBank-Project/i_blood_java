package com.user.service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.user.bean.BloodBankBean;
import com.user.bean.BloodBean;
import com.user.bean.UserBean;
import com.user.entity.OTPEntity;
import com.user.entity.User;
import com.user.exception.EmailNotFoundException;
import com.user.exception.InvalidCredentialsException;
import com.user.exception.InvalidOtpException;
import com.user.exception.PasswordUpdateException;
import com.user.exception.UserDeleteException;
import com.user.exception.UserNotFoundException;
import com.user.exception.UserSaveException;
import com.user.exception.UserUpdateException;
import com.user.repository.OtpRepository;
import com.user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private OtpRepository otpRepository;

	@Override
	public User saveUser(User user) {

		if (!userRepository.findByEmail(user.getEmail()).isPresent()) {
			userRepository.save(user);
		} else {
			throw new UserSaveException("already registered");
		}
		return user;
	}

	@Override
	public UserBean getByUserId(Long id) {
		Optional<User> optional = userRepository.findById(id);

		if (optional.isEmpty()) {
			try {
				optional.orElseThrow(() -> new UserNotFoundException("record not found"));
			} catch (UserNotFoundException e) {
				throw e;
			}
		}

		User user = optional.get();

		String bloodURL = "http://localhost:8081/" + user.getBloodGroupId();

		String bloodBankURL = "http://localhost:8082/" + user.getBloodBankId();

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> httpEntity = new HttpEntity<>(headers);

		ResponseEntity<BloodBankBean> responseEntity1 = restTemplate.exchange(bloodBankURL, HttpMethod.GET, httpEntity,
				BloodBankBean.class);

		ResponseEntity<BloodBean> responseEntity2 = restTemplate.exchange(bloodURL, HttpMethod.GET, httpEntity,
				BloodBean.class);

		BloodBankBean bloodBankBean = responseEntity1.getBody();

		BloodBean bloodBean = responseEntity2.getBody();

		UserBean userBean = UserBean.builder().userId(user.getUserId()).firstName(user.getFirstName())
				.lastName(user.getLastName()).email(user.getEmail()).password(user.getPassword())
				.dateOfBirth(user.getDateOfBirth()).gender(user.getGender()).address(user.getAddress())
				.contactNumber(user.getContactNumber()).alternateContactNumber(user.getAlternateContactNumber())
				.type(user.getType()).bloodBankBean(bloodBankBean).bloodBean(bloodBean).build();

		return userBean;
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public void deleteUserById(Long id) {

		Optional<User> optional = userRepository.findById(id);

		if (optional.isEmpty()) {
			try {
				optional.orElseThrow(() -> new UserDeleteException("record not present"));
			} catch (UserDeleteException e) {
				throw e;
			}
		} else {
			userRepository.deleteById(id);
		}
	}

	@Override
	public Optional<User> updateUser(User user) {
		Optional<User> optional = userRepository.findById(user.getUserId());
		if (optional.isEmpty()) {
			try {
				optional.orElseThrow(() -> new UserUpdateException("record not found"));
			} catch (UserUpdateException e) {
				throw e;
			}
		}
		userRepository.save(user);
		return optional;
	}

	@Override
	public Optional<User> updateUserPassword(String email, String password) {
		Optional<User> optional = userRepository.findByEmail(email);
		if (optional.isPresent()) {
			User user = optional.get();
			if (user.getPassword().equals(password)) {
				throw new PasswordUpdateException("previously used password is not applicable");
			} else {
				user.setPassword(password);
				userRepository.save(user);
			}
		} else {
			throw new PasswordUpdateException("email not correct");
		}
		return optional;
	}

	@Override
	public Optional<User> updateUserBloodBankId(String email, Long bloodBankId) {
		Optional<User> optional = userRepository.findByEmail(email);
		if (optional.isPresent()) {
			User user = optional.get();
			user.setBloodBankId(bloodBankId);
			userRepository.save(user);
		} else {
			throw new PasswordUpdateException("email not correct");
		}
		return optional;
	}

	@Override
	public Optional<User> userLogin(String email, String password) {
		Optional<User> optional1 = userRepository.findByEmail(email);
		if (optional1.isPresent()) {
			User user = optional1.get();
			if (user.getPassword().equals(password)) {
				System.out.println("login done");
			} else {
				throw new InvalidCredentialsException("password not correct");
			}

		} else {
			throw new InvalidCredentialsException("email not correct");
		}
		return optional1;
	}

	@Override
	public Optional<User> updateUserBloodBankIdBlood(String email, Long bloodBankId, Long bloodId) {
		Optional<User> optional = userRepository.findByEmail(email);
		if (optional.isPresent()) {
			User user = optional.get();
			user.setBloodBankId(bloodBankId);
			user.setBloodGroupId(bloodId);
			userRepository.save(user);
		} else {
			throw new PasswordUpdateException("email not correct");
		}
		return optional;
	}
	@Override
	public String generateOtp() {
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		return String.valueOf(otp);
	}

	@Override
	public void sendOtpEmail(String toEmail, String otp) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject("Your OTP");
		message.setText("Your OTP is: " + otp);

		javaMailSender.send(message);
	}

	public User forgetPassword(String email) {
		// log.info("Checking if email is present or not");
		Optional<User> user = userRepository.findByEmail(email);
		System.out.println("email" + email);
		if (user.isPresent()) {
			// log.info("Email is valid");
			String otp = generateOtp();

			Timestamp expirationTime = Timestamp.from(Instant.now().plus(Duration.ofMinutes(5)));

			sendOtpEmail(email, otp);
			saveOtp(email, otp, expirationTime);
			return user.get();
		} else {
			// log.info("Email is not valid");
			throw new EmailNotFoundException("Email not found");
		}
	}

	@Override
	public void saveOtp(String email, String otp, Timestamp expirationTime) {
		Optional<OTPEntity> existingOtp = otpRepository.findByEmail(email);

		if (existingOtp.isPresent()) {
			existingOtp.get().setOtp(otp);
			existingOtp.get().setExpirationTime(expirationTime);
			otpRepository.save(existingOtp.get());
		} else {
			OTPEntity newOtp = new OTPEntity();
			newOtp.setEmail(email);
			newOtp.setOtp(otp);
			newOtp.setExpirationTime(expirationTime);
			otpRepository.save(newOtp);
		}
	}

	@Override
	public boolean verifyOtp(String email, String enteredOtp) {

		OTPEntity otpEntity = otpRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("OTP not found"));

		Timestamp expirationTime = otpEntity.getExpirationTime();

		LocalDateTime expirationLocalDateTime = expirationTime.toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime();

		if (expirationLocalDateTime.isBefore(LocalDateTime.now())) {
			return false;
		}

		else if (!enteredOtp.equals(otpEntity.getOtp())) {
			throw new InvalidOtpException("Entered Correct otp");
		} else {
			return true;
		}

	}

	@Override
	public void sendPasswordResetEmail(String email) {
		// TODO Auto-generated method stub

	}}
