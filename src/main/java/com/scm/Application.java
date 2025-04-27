package com.scm;

import com.scm.Entities.Providers;
import com.scm.Entities.User;
import com.scm.Helpers.AppConstant;
import com.scm.Repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class Application implements CommandLineRunner {


	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository ;

    public Application(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }





    public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User admin = new User();
		admin.setName("admin");
		admin.setEmail("admin@gmail.com");
		admin.setEmailVerified(true);
		admin.setPassword(passwordEncoder.encode("admin"));
		admin.setProvider(Providers.SELF);
		admin.setUserId(UUID.randomUUID().toString());
		admin.setRole_list(List.of(AppConstant.ROLE_USER));
		admin.setEnable(true);
		admin.setAbout("This is dummy user created for temporary");
		admin.setPhoneVerified(true);
		admin.setPhoneNum("0000000000000");

		userRepository.findByEmail("admin@gmail.com").ifPresentOrElse(user ->{} ,()-> {
					userRepository.save(admin);
			System.out.println("Admin Dummy Created Successfully ");
				});



	}
}
