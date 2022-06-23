package com.bezkoder.spring.datajpa.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.datajpa.model.Newspaper;
import com.bezkoder.spring.datajpa.repository.NewspaperRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class NewspaperController {

	@Autowired
	NewspaperRepository NewspaperRepository;

	@GetMapping("/newspapers")
	public ResponseEntity<List<Newspaper>> getAllNewspapers(@RequestParam(required = false) String title) {
		try {
			List<Newspaper> newspapers = new ArrayList<Newspaper>();

			if (title == null)
				NewspaperRepository.findAll().forEach(newspapers::add);
			else
				NewspaperRepository.findByTitleContaining(title).forEach(newspapers::add);

			if (newspapers.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(newspapers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/newspapers/{id}")
	public ResponseEntity<Newspaper> getNewspaperById(@PathVariable("id") long id) {
		Optional<Newspaper> NewspaperData = NewspaperRepository.findById(id);

		if (NewspaperData.isPresent()) {
			return new ResponseEntity<>(NewspaperData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/newspapers")
	public ResponseEntity<Newspaper> createNewspaper(@RequestBody Newspaper Newspaper) {
		try {
			Newspaper _Newspaper = NewspaperRepository
					.save(new Newspaper(Newspaper.getTitle(), Newspaper.getDescription(), false));
			return new ResponseEntity<>(_Newspaper, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/newspapers/{id}")
	public ResponseEntity<Newspaper> updateNewspaper(@PathVariable("id") long id, @RequestBody Newspaper Newspaper) {
		Optional<Newspaper> NewspaperData = NewspaperRepository.findById(id);

		if (NewspaperData.isPresent()) {
			Newspaper _Newspaper = NewspaperData.get();
			_Newspaper.setTitle(Newspaper.getTitle());
			_Newspaper.setDescription(Newspaper.getDescription());
			_Newspaper.setPublished(Newspaper.isPublished());
			return new ResponseEntity<>(NewspaperRepository.save(_Newspaper), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/newspapers/{id}")
	public ResponseEntity<HttpStatus> deleteNewspaper(@PathVariable("id") long id) {
		try {
			NewspaperRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/newspapers")
	public ResponseEntity<HttpStatus> deleteAllNewspapers() {
		try {
			NewspaperRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/newspapers/published")
	public ResponseEntity<List<Newspaper>> findByPublished() {
		try {
			List<Newspaper> newspapers = NewspaperRepository.findByPublished(true);

			if (newspapers.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(newspapers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
