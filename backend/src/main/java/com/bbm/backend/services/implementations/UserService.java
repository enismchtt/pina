package com.bbm.backend.services.implementations;

import com.bbm.backend.models.User;
import com.bbm.backend.repositories.UserRepository;
import com.bbm.backend.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public User authenticate(String username, String password) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Invalid username or password"));

		if (!PasswordUtil.matches(password, user.getPassword())) {
			throw new RuntimeException("Invalid username or password");
		}

		return user;
	}

	public void changePassword(String username, String oldPassword, String newPassword) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found"));

//		if (!PasswordUtil.matches(oldPassword, user.getPassword())) {
//			throw new RuntimeException("Old password is incorrect");
//		}

		user.setPassword(PasswordUtil.encode(newPassword));
		userRepository.save(user);
	}

	public void banUser(Long id) {
		if(userRepository.existsById(id)){
			User user = userRepository.findById(id).get();
			if(!user.isBanned()){
				user.setBanned(true);
				userRepository.save(user);
			}
		}
		return;

	}
	public List<User> findAll() {
		return userRepository.findAll();
	}

	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}
	public boolean existsByEmail(String username) {
		return userRepository.existsByEmail(username);
	}

	public void unbanUser(Long id) {
		if(userRepository.existsById(id)){
			User user = userRepository.findById(id).get();
			if(user.isBanned()){
				user.setBanned(false);
				userRepository.save(user);
			}
		}
	}

}
