package com.springboot.apirest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.apirest.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long>{

	Curso findByNome(String nomeCurso);

}