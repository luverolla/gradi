package io.luverolla.gradi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.repositories.UserRepository;

@SpringBootApplication
public class GradiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GradiApplication.class, args);
    }

    @Bean
    public CommandLineRunner addAdminIfNotExists(UserRepository repo)
    {
    	return (String[] args) ->
    	{
    		BCryptPasswordEncoder penc = new BCryptPasswordEncoder();
    		
    		if(repo.count() == 0)
    		{
    			User admin = new User();
    			admin.setCode("00000");
    			admin.setRole(User.Role.ADMIN);
    			admin.setName("Administrator");
    			admin.setSurname("");
    			admin.setEmail("admin@contoso");
    			admin.setPassword(penc.encode("admin"));
    			
    			repo.save(admin);
    			
    			User a = new User();
        		a.setCode("A13ZZ");
        		a.setRole(User.Role.USER);
        		a.setName("Mario");
        		a.setSurname("Rossi");
        		a.setEmail("mario.rossi@contoso");
        		a.setPassword(penc.encode("mario.rossi"));
        		
        		User b = new User();
        		b.setCode("80B42");
        		b.setRole(User.Role.EDITOR);
        		b.setName("Johann");
        		b.setSurname("Van Hoek");
        		b.setEmail("johann.vanhoek@contoso");
        		b.setPassword(penc.encode("johann.vanhoek"));
        		
        		repo.save(a);
        		repo.save(b);
    		}
    	};
    }
}
