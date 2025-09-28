package com.bbm.backend.startup;

import com.bbm.backend.models.Administrator;
import com.bbm.backend.repositories.AdministratorRepository;
import com.bbm.backend.repositories.UserRepository;
import com.bbm.backend.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupRunner implements CommandLineRunner{

	private final AdministratorRepository adminRepository;

	@Autowired
	public StartupRunner(AdministratorRepository adminRepository) {
		this.adminRepository = adminRepository;
	}

	@Override
	public void run(String... args) {
		if (adminRepository.findAll().isEmpty()){
			Administrator admin = new Administrator();
			admin.setUsername("admin");
			admin.setPassword(PasswordUtil.encode("admin"));
			admin.setType("admin");
			adminRepository.save(admin);
		}
	}
}
