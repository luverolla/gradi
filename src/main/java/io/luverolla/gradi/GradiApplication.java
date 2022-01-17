package io.luverolla.gradi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;

import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.repositories.UserRepository;
import io.luverolla.gradi.exceptions.CorruptedDatabaseException;

import java.util.Optional;

@SpringBootApplication
public class GradiApplication
{
	@Value("${gradi.admin.email}")
	private String adminEmail;

	@Value("${gradi.admin.password}")
	private String adminPassword;

	@Autowired
	private PasswordEncoder encoder;

    public static void main(String[] args) {
        SpringApplication.run(GradiApplication.class, args);
    }

    @Bean
    public CommandLineRunner adminSetup(UserRepository repo)
    {
    	return (String[] args) ->
    	{
			// if admin data is not present, then database script has not been executed
			// this could mean that database is not in a consistent status, then
			// trying to create admin record will lead to unknown behaviour
			// throw exception instead
    		Optional<User> _admin = repo.findById("0000000000");
			if(_admin.isEmpty())
				throw new CorruptedDatabaseException();

			User admin = _admin.get();
			admin.setEmail(adminEmail);
			admin.setPassword(encoder.encode(adminPassword));
			repo.save(admin);
    	};
    }
}
