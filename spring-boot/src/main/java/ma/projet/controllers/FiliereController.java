package ma.projet.controllers;

import java.util.List;

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

import ma.projet.entities.Filiere;
import ma.projet.services.FiliereService;

@RestController
@RequestMapping("/api/v1/filieres")
public class FiliereController {

	@Autowired
	private FiliereService service;

	@GetMapping
	public List<Filiere> findAllFiliere() {
		return service.findAll();
	}

	@PostMapping
	public Filiere createFiliere(@RequestBody Filiere filiere) {
		filiere.setId(0);
		return service.create(filiere);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> findById(@PathVariable int id) {
		Filiere filiere = service.findById(id);
		if (filiere == null) {
			return new ResponseEntity<Object>("Filiere avec ID = " + id + " n'existe pas", HttpStatus.BAD_REQUEST);
		} else {
			return ResponseEntity.ok(filiere);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> updateFiliere(@PathVariable int id, @RequestBody Filiere newFiliere) {
		Filiere oldFiliere = service.findById(id);
		if (oldFiliere == null) {
			return new ResponseEntity<Object>("Filiere avec ID = " + id + " n'existe pas", HttpStatus.BAD_REQUEST);
		} else {
			newFiliere.setId(id);
			return ResponseEntity.ok(service.update(newFiliere));
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteFiliere(@PathVariable int id) {
		Filiere filiere = service.findById(id);
		if (filiere == null) {
			return new ResponseEntity<Object>("Filiere avec ID = " + id + " n'existe pas", HttpStatus.BAD_REQUEST);
		} else {
			service.delete(filiere);
			return ResponseEntity.ok("filière supprimée");
		}
	}
	
}
