package com.nelioalves.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.domain.dto.CategoriaDTO;
import com.nelioalves.cursomc.services.CategoriaService;


@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaService service;
	
	// service.find
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Categoria> find(@PathVariable Integer id) {
		
		Categoria obj = service.find(id);	
		return ResponseEntity.ok().body(obj);
	}	
	
	// service.insert	// <Void>, corpo vazio
	// Valid (para reconhecer a validação feita na class DTO (NotEmpty e Size))
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> insert(@Valid @RequestBody   CategoriaDTO objDTO) {		
		Categoria obj = service.fromDTO(objDTO);
		obj = service.insert(obj);		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}	
	
	// service.update	// <Void>, corpo vazio
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)	// tem o value, igual ao .GET
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDTO, @PathVariable Integer id) {
		Categoria obj = service.fromDTO(objDTO);
		obj.setId(id); // redundante - colocando só por garantia
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	// service.delete	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)	
	public ResponseEntity<Void> delete(@RequestBody Categoria obj, @PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	// service.findAll
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<CategoriaDTO>> findAll() {
		
		List<Categoria> list = service.findAll();
		
		// Convertendo List para List:
		// Tem que ter um "constructor CategoriaDTO" que pegue os dados necessários do "constructor Categoria"
		// stream (collection) - map (mapear obj, chamando função) - collect (transforma o obj em uma lista)
		List<CategoriaDTO> listDTO = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(listDTO);
	}	
	
	// service.findPage
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<CategoriaDTO>> findPage(
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage, 
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy, 
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		
		Page<Categoria> list = service.findPage(page, linesPerPage, orderBy, direction);
		
		// Come é um Page, e não uma List, pode tirar o "steam" e o "collect"
		Page<CategoriaDTO> listDTO = list.map(obj -> new CategoriaDTO(obj));
		
		return ResponseEntity.ok().body(listDTO);
	}	

}
