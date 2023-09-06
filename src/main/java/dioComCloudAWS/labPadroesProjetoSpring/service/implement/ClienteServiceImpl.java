package dioComCloudAWS.labPadroesProjetoSpring.service.implement;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dioComCloudAWS.labPadroesProjetoSpring.model.Cliente;
import dioComCloudAWS.labPadroesProjetoSpring.model.ClienteRepository;
import dioComCloudAWS.labPadroesProjetoSpring.model.Endereco;
import dioComCloudAWS.labPadroesProjetoSpring.model.EnderecoRepository;
import dioComCloudAWS.labPadroesProjetoSpring.service.ClienteService;
import dioComCloudAWS.labPadroesProjetoSpring.service.ViaCepService;

@Service
public class ClienteServiceImpl implements ClienteService{
	
	//Singleton: Injetar os componentes do Spring com Autowired
	@Autowired 
	private ClienteRepository clienteRepository;
	
	@Autowired 
	private EnderecoRepository enderecoRepository;
	
	@Autowired 
	private ViaCepService viaCepService;
	
	//Strategy: Implementar os métodos definidos na interface
	
	//Facade: Abstrair integrações com subsistemas, provendo uma interface simples

	@Override
	public Iterable<Cliente> buscarTodos() {
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente.get();
	}

	@Override
	public void inserir(Cliente cliente) {
		salvarClienteComCep(cliente);
	}

	@Override
	public void atualizar(Long id, Cliente cliente) {
		 Optional<Cliente> clienteBd = clienteRepository.findById(id);
		    if (clienteBd.isPresent()) {
		        Cliente clienteExistente = clienteBd.get();
		        if (!cliente.getEndereco().equals(clienteExistente.getEndereco())) {
		            // O endereço foi alterado, consultar CEP e atualizar
		            salvarClienteComCep(cliente);
		        } else {
		            // O endereço não foi alterado, atualizar apenas os outros campos do cliente
		            clienteRepository.save(cliente);
		        }
		    }
		}

	@Override
	public void deletar(Long id) {
		//Deletar pelo ID
		clienteRepository.deleteById(id);	
	}
	
	
	private void salvarClienteComCep(Cliente cliente) {
		//Verificar se endereço existe do cliente existe (pelo CEP)
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(()->{
			//caso não exista, integrar com ViaCep e persistir o retorno
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);
		
		//Inserir cliente, vinculando o Endereço (novo ou existente)
		clienteRepository.save(cliente);
	}

}
