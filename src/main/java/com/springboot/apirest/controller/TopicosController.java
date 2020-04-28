package com.springboot.apirest.controller;

import java.net.URI;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public List<TopicoDTO> index(String nomeCurso) {
		
		if (nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDTO.convert(topicos);
		}
		
		List<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso);

		return TopicoDTO.convert(topicos);
	}
	
	@PostMapping
	public ResponseEntity<TopicoDTO> store(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		
		Topico topico = form.convert(cursoRepository);
		topicoRepository.save(topico);
		
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDTO(topico));
	}
	
	@GetMapping("/{id}")
	public ShowTopicoDTO show(@PathVariable("id") Long id) {
		
		Topico topico = topicoRepository.getOne(id);
		
		return new ShowTopicoDTO(topico);
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<TopicoDTO> update(@PathVariable Long id, @RequestBody @Valid UpdateTopicoForm form) {
		
		Topico topico = form.update(id, topicoRepository);
		
		return ResponseEntity.ok(new TopicoDTO(topico));
	}
}
