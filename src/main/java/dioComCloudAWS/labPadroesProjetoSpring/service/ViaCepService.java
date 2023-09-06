package dioComCloudAWS.labPadroesProjetoSpring.service;

import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import dioComCloudAWS.labPadroesProjetoSpring.model.Endereco;

@FeignClient(name = "viacep", url ="https://viacep.com.br/ws")
public interface ViaCepService {

	
	//outra:@GetMapping("/{cep}/json/")
	// uma forma de fazer: "
	@RequestMapping(method = RequestMethod.GET, value = "/{cep}/json/")
	Endereco consultarCep(@PathVariable("cep") String cep);
}
