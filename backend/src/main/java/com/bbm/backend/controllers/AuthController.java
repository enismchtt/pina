package com.bbm.backend.controllers;

import com.bbm.backend.dto.CourierDTO;
import com.bbm.backend.dto.UserDTO;
import com.bbm.backend.models.Courier;
import com.bbm.backend.models.Restaurant;
import com.bbm.backend.util.JwtService;
import com.bbm.backend.models.User;
import com.bbm.backend.services.implementations.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

	private final UserService userService;
	private final JwtService jwtService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
		try {
			User user = userService.authenticate(loginRequest.get("username"), loginRequest.get("password"));
			if(user.isBanned()){
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is banned.");
			}
			switch (user.getType()){
				case "courier":
					Courier courier = (Courier) user;
					if(!courier.isApproved())
						return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not approved.");
					break;
				case "restaurant":
					Restaurant restaurant = (Restaurant) user;
					if(!restaurant.isApproved())
						return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not approved.");
					break;
			}
			String token = jwtService.generateToken(user);

			Map<String, Object> response = new HashMap<>();
			response.put("id", user.getId());
			response.put("accountType", user.getType());
			response.put("token", token);

			return ResponseEntity.ok(response);
		} catch (RuntimeException e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
	}

	@PostMapping("/check-username")
	public ResponseEntity<String> checkUsername(@RequestBody CourierDTO dto) {
		if (userService.existsByUsername(dto.getUsername())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
		}
		if (userService.existsByEmail(dto.getEmail())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists.");
		}

		return ResponseEntity.ok("No problem.");
	}
	@PostMapping("/check-password")
	public ResponseEntity<?> checkPassword(@RequestBody CourierDTO dto) {
		try {
			userService.authenticate(dto.getUsername(), dto.getPassword());
			return ResponseEntity.ok("Password is correct.");
		} catch (RuntimeException e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
	}
	@PostMapping("/change-password")
	public ResponseEntity<?> changePassword(@RequestBody Map<String, String> payload) {
		try {
			String username = payload.get("username");
			String oldPassword = payload.get("oldPassword");
			String newPassword = payload.get("newPassword");

			userService.changePassword(username, oldPassword, newPassword);
			return ResponseEntity.ok("Password changed successfully.");
		} catch (RuntimeException e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		} catch (Exception e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", "An unexpected error occurred while changing the password.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@GetMapping
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		return ResponseEntity.ok(
				userService.findAll().stream().map(this::toDTO).collect(Collectors.toList())
		);
	}

	private UserDTO toDTO(User user) {
		UserDTO dto = new CourierDTO();
		dto.setId(user.getId());
		dto.setUsername(user.getUsername());
		dto.setPassword(user.getPassword());
		dto.setEmail(user.getEmail());
		dto.setPhoneNumber(user.getPhoneNumber());
		dto.setType(user.getType());
		return dto;
	}
}
