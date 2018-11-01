package br.com.academia.poo.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import br.com.academia.poo.model.Cliente;
import br.com.academia.poo.repository.ClienteRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/clientes")
public class ClienteEndpoint {

	private final ClienteRepository clientes;

	@Autowired
	public ClienteEndpoint(ClienteRepository clientes) {
		super();
		this.clientes = clientes;
	}

	// metodo get
	@GetMapping
	@CrossOrigin(origins="http://localhost:4200/")
	@ApiOperation(value = "Mostra uma lista de clientes já cadastrados", response = Cliente.class, notes = "Essa operação mostra um registro dos clientes cadastrados.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna uma lista de clientes com uma mensagem de sucesso", response = Cliente.class),
			@ApiResponse(code = 500, message = "Caso tenhamos algum erro, não retornamos nada", response = Cliente.class)

	})
	public ResponseEntity<?> listAllClientes() {
		return new ResponseEntity<>(clientes.findAll(), HttpStatus.OK);
	}

	
	// metodo get
	@GetMapping(path = "/{id}")
	@CrossOrigin(origins="http://localhost:4200/")
	@ApiOperation(value = "Mostra um cliente específico", response = Cliente.class, notes = "Essa operação mostra um registro de um cliente específico.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna um cliente com uma mensagem de sucesso", response = Cliente.class),
			@ApiResponse(code = 500, message = "Caso tenhamos algum erro não retornamos nada", response = Cliente.class)

	})
	public ResponseEntity<?> getClienteById(@PathVariable("id") int id) {
		verificarClienteExiste(id);
		Cliente cliente = clientes.findById(id).get();
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}

	// metodo post

	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@CrossOrigin(origins="http://localhost:4200/")
	@ApiOperation(value = "Cadastra um novo cliente na lista", response = Cliente.class, notes = "Essa operação salva um novo registro com as informações de cliente.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna um cliente com uma mensagem de sucesso", response = Cliente.class),
			@ApiResponse(code = 500, message = "Caso tenhamos algum erro vamos retornar um cliente", response = Cliente.class)

	})

	public ResponseEntity<?> saveCliente(@RequestBody Cliente cliente) {
		return new ResponseEntity<>(clientes.save(cliente), HttpStatus.OK);
	}

	
	// metodo delete
	@DeleteMapping("/{id}")
	@CrossOrigin(origins="http://localhost:4200/")
	@ApiOperation(value = "Deleta da lista um cliente cadastrado", response = Cliente.class, notes = "Essa operação deleta um cliente da lista.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna um cliente com uma mensagem de sucesso", response = Cliente.class),
			@ApiResponse(code = 500, message = "Caso tenhamos algum erro vamos retornar um cliente", response = Cliente.class)

	})
	public ResponseEntity<?> deleteCliente(@PathVariable int id) {
		verificarClienteExiste(id);
		clientes.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	
	// metodo put
	@PutMapping(value = "/{id}")
	@CrossOrigin(origins="http://localhost:4200/")
	@ApiOperation(value = "Altera os dados de um cliente já cadastrado", response = Cliente.class, notes = "Essa operação altera os dados de um cliente específico da lista.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorna um cliente com uma mensagem de sucesso", response = Cliente.class),
			@ApiResponse(code = 500, message = "Caso tenhamos algum erro vamos retornar um cliente", response = Cliente.class)

	})
	public ResponseEntity<?> updateCliente(@RequestBody Cliente cliente) {
		verificarClienteExiste(cliente.getId());
		Cliente c = cliente;
		clientes.save(cliente);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private void verificarClienteExiste(int id) {
		if (!clientes.findById(id).isPresent())
			throw new ResourceNotFoundException("cliente não encontrado pelo ID: " + id);
	}
}