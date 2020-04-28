package com.springboot.apirest.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.springboot.apirest.controller.dto.ShowTopicoDTO;
import com.springboot.apirest.controller.dto.TopicoDTO;
import com.springboot.apirest.controller.form.TopicoForm;
import com.springboot.apirest.controller.form.UpdateTopicoForm;
import com.springboot.apirest.model.Topico;
import com.springboot.apirest.repository.CursoRepository;
import com.springboot.apirest.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
	@GetMapping
	public Page<TopicoDTO> index(@RequestParam(required = false) String nomeCurso,
			@RequestParam int page,@RequestParam int size, @RequestParam String orderBy) {
		
		Pageable paginacao = PageRequest.of(page, size, Direction.ASC, orderBy);
		
		if (nomeCurso == null) {
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDTO.convert(topicos);
		}
		
		Page<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso, paginacao);

		return TopicoDTO.convert(topicos);
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<TopicoDTO> store(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		
		Topico topico = form.convert(cursoRepository);
		topicoRepository.save(topico);
		
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDTO(topico));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ShowTopicoDTO> show(@PathVariable("id") Long id) {
		
		Optional<Topico> topico = topicoRepository.findById(id);
		
		if(topico.isPresent()) {
			
			return ResponseEntity.ok(new ShowTopicoDTO(topico.get()));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<TopicoDTO> update(@PathVariable Long id, @RequestBody @Valid UpdateTopicoForm form) {
		
		Optional<Topico> optional = topicoRepository.findById(id);
		
		if (optional.isPresent()) {
			
			Topico topico = form.update(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDTO(topico));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		Optional<Topico> optional = topicoRepository.findById(id);
		
		if (optional.isPresent()) {
			
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}
}