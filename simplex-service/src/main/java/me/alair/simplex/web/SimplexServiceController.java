package me.alair.simplex.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import me.alair.simplex.service.VenttselSimplex;
import me.alair.simplex.service.funcoes.FuncaoOtima;

@RestController
public class SimplexServiceController {

	@Autowired
	VenttselSimplex venttselSimplexService;

	@RequestMapping(value = "/simplex", method = RequestMethod.POST)
	public String simplex(@RequestBody FuncaoOtima funcaoObjetivo) {

		venttselSimplexService.executaSimplex(funcaoObjetivo);

		return funcaoObjetivo.toString();
	}

}