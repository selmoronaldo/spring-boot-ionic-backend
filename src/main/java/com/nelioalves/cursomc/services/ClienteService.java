package com.nelioalves.cursomc.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cidade;
import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.domain.Endereco;
import com.nelioalves.cursomc.domain.dto.ClienteDTO;
import com.nelioalves.cursomc.domain.dto.ClienteNewDTO;
import com.nelioalves.cursomc.domain.enums.TipoCliente;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.repositories.EnderecoRepository;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null); // sem essa opção poderia ser considerado um update
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos()); //Selmo
		return obj;
	}
	
	@Transactional
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId()); // pegando os dados antes de alterar
		updateData(newObj, obj);
		return repo.save(newObj);
	}

	@Transactional
	public void delete(Integer id) {
		find(id); // criticando antes de deletar
		
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há entidades relacionadas");
		}		
	}
	
	// DTO
	// Obs. 1) Lista todas as categorias e também com os produtos associados a ela - sem o DTO.
	// 2) Use o padrão DTO para poder escolher os campos desejados. Como por exemplo categoria sem os produtos
	public List<Cliente> findAll() {
		return repo.findAll();
	}
	
	// O findPage abaixo chama o repo.findAll
	public Page<Cliente> findPage(Integer numberPage, Integer linesPerPage, String orderBy, String direction) {
		
		PageRequest pageRequest = PageRequest.of(numberPage, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return repo.findAll(pageRequest);
	}
	
	// DTO
	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null, null);
	}
	
	// Sobrecarga
	public Cliente fromDTO(ClienteNewDTO objND) {
		Cliente cli = new Cliente(null, objND.getNome(), objND.getEmail(), objND.getCpfOuCnpj(), TipoCliente.toEnum(objND.getTipo()),
				pe.encode(objND.getSenha()));
		
//		Cidade cid = cidadeRepository.findById(objND.getCidadeId()).get(); //sai esse
		Cidade cid = new Cidade(objND.getCidadeId(), null, null); //nova alteração 
				
		Endereco end = new Endereco(null, objND.getLogradouro(), objND.getNumero(), objND.getComplemento(), objND.getBairro(), objND.getCep(), cli, cid);
		
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objND.getTelefone1()); //1º telefone obrigatório
		if (objND.getTelefone2() != null) {
			cli.getTelefones().add(objND.getTelefone2());
		}
		if (objND.getTelefone3() != null) {
			cli.getTelefones().add(objND.getTelefone3());
		}
		
		return cli;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}


}
