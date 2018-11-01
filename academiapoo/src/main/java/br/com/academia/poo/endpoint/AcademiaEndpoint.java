package br.com.academia.poo.endpoint;

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
import org.springframework.web.bind.annotation.RestController;

import br.com.academia.poo.error.ResourceNotFoundException;
import br.com.academia.poo.model.Academia;
import br.com.academia.poo.repository.AcademiaRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/academia")
public class AcademiaEndpoint {

	private static AcademiaRepository academias;

	@Autowired
	public AcademiaEndpoint(AcademiaRepository academias) {

		this.academias = academias;
	}

	// metodo get
	@GetMapping
	@CrossOrigin(origins="http://localhost:4200/")
	@ApiOperation(value = "Mostra uma lista de academias já cadastradas", response = Academia.class, notes = "Essa operação mostra um registro da academias cadastradas.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna uma lista de academias com uma mensagem de sucesso", response = Academia.class),
			@ApiResponse(code = 500, message = "Caso tenhamos algum erro, não retornamos nada", response = Academia.class)
	})
	public ResponseEntity<?> listAllAcademias() {
		return new ResponseEntity<>(academias.findAll(), HttpStatus.OK);
	}

	// metodo get
	@GetMapping(path = "/{id}")
	@CrossOrigin(origins="http://localhost:4200/")
	@ApiOperation(value = "Mostra uma academia específica", response = Academia.class, notes = "Essa operação mostra os registros de uma academia específica.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna uma academia com uma mensagem de sucesso", response = Academia.class),
			@ApiResponse(code = 500, message = "Caso tenhamos algum erro, não retornamos nada", response = Academia.class)
	})
	public ResponseEntity<?> getAcademiaById(@PathVariable("id") Long id) {
		verificarAcademiaExiste(id);
		Academia academia = academias.findById(id).get();
		return new ResponseEntity<>(academia, HttpStatus.OK);
	}

	// metodo post
	@PostMapping
	@CrossOrigin(origins="http://localhost:4200/")
	@ApiOperation(value = "Cadastra uma nova academia na lista", response = Academia.class, notes = "Essa operação cadastra os registros de uma academia específica.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna uma academia com uma mensagem de sucesso", response = Academia.class),
			@ApiResponse(code = 500, message = "Caso tenhamos algum erro, não retornamos nada", response = Academia.class)
	})
	public ResponseEntity<?> saveAcademia(@RequestBody Academia academia) {
		return new ResponseEntity<>(academias.save(academia), HttpStatus.OK);
	}

	// metodo delete
	@DeleteMapping(path = "{id}")
	@ApiOperation(value = "Deleta da lista uma academia cadastrada", response = Academia.class, notes = "Essa operação deleta os registros de uma academia específica da lista.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna uma academia com uma mensagem de sucesso", response = Academia.class),
			@ApiResponse(code = 500, message = "Caso tenhamos algum erro, não retornamos nada", response = Academia.class)
	})
	public ResponseEntity<?> deleteAcademia(@PathVariable Long id) {
		verificarAcademiaExiste(id);
		academias.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// metodo put
	@PutMapping(path = "/{id}")
	@ApiOperation(value = "Altera os dados cadastrados de uma academia", response = Academia.class, notes = "Essa operação altera os registros de uma academia específica.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna uma academia com uma mensagem de sucesso", response = Academia.class),
			@ApiResponse(code = 500, message = "Caso tenhamos algum erro, não retornamos nada", response = Academia.class)
	})
	public ResponseEntity<?> updateEquipamento(@RequestBody Academia academia) {
		verificarAcademiaExiste(academia.getId());
		Academia e = academia;
		academias.save(e);
		return new ResponseEntity<>(e, HttpStatus.OK);
	}

	private void verificarAcademiaExiste(Long id) {
		if (!academias.findById(id).isPresent())
			throw new ResourceNotFoundException("Academia não encontrado pelo ID: " + id);
	}
}
