package me.alair.simplex.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

	@CrossOrigin
	@RequestMapping(value = "/simplex", method = RequestMethod.POST)
	public ResponseEntity<String> simplex(@RequestBody FuncaoOtima funcaoObjetivo) {

		String resposta = venttselSimplexService.executaSimplex(funcaoObjetivo);

		return ResponseEntity.status(HttpStatus.OK).body(resposta);
	}

}