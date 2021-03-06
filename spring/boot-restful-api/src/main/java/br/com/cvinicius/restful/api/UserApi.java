package br.com.cvinicius.restful.api;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.cvinicius.restful.domain.User;
import br.com.cvinicius.restful.domain.UserRepository;

@RestController
public class UserApi {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> getAll(){
		
		return ResponseEntity.ok(this.userRepository.findAll());
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<User> getById(@PathVariable Integer id){
		
		Optional<User> user = this.userRepository.findById(id);
		
		return user.map(u -> ResponseEntity.ok(u))
				   .orElse(ResponseEntity.notFound().build());
	}
	
	@PutMapping("/users/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody User user){
		
		user.setId(id);
		
		userRepository.save(user);
		
		return ResponseEntity.ok(user);
	}
	
	@PostMapping("/users")
	public ResponseEntity<?> save(@RequestBody User user){
		
		User newUser = userRepository.save(user);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
													.path("/{id}")
													.buildAndExpand(newUser.getId())
													.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Integer id){
		
		this.userRepository.deleteById(id);
		
		return ResponseEntity.ok().build();
	}
}