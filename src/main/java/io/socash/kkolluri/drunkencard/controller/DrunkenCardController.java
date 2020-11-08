package io.socash.kkolluri.drunkencard.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.socash.kkolluri.drunkencard.Service.DrunkenCardService;
import io.socash.kkolluri.drunkencard.exception.DidntDealException;
import io.socash.kkolluri.drunkencard.exception.GameFinalizedException;
import io.socash.kkolluri.drunkencard.exception.InsufficientPlayersException;
import io.socash.kkolluri.drunkencard.games.ICardGame;
import io.socash.kkolluri.drunkencard.model.Player;

@RestController
@RequestMapping("/drunkenGame")
public class DrunkenCardController {
	@Autowired
	DrunkenCardService service;
	Logger logger = LoggerFactory.getLogger(DrunkenCardController.class);
	
	@PostMapping
	public ResponseEntity<Integer> createGame(@RequestBody String nickName) {
		int id = service.createGame(nickName);
		logger.info("New game requested by " + nickName + "is created with id of " + id);
		if (id == -1) {
			return new ResponseEntity<Integer>(id, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Integer>(id, HttpStatus.OK);	
	}
	
	@PutMapping
	@RequestMapping("/{id}/join")
	public ResponseEntity<List<Player>> joinGame(@PathVariable int id, @RequestBody String nickName) {
		List<Player> players = service.joinGame(id, nickName);
		if (players == null) {
			logger.info("Player " + nickName + " unable to join game with id: " + id);
			return new ResponseEntity<List<Player>>(HttpStatus.BAD_REQUEST);
		}
		logger.info("Player " + nickName + " joined game with id: " + id);
		return new ResponseEntity<List<Player>>(players,HttpStatus.OK);
	}
	
	@PutMapping
	@RequestMapping("/{id}/deal")
	public ResponseEntity<List<Player>> deal(@PathVariable int id) {
		try {
			List<Player> result = service.deal(id);
			logger.info("Cards dealt for game with id: " + id);
			return new ResponseEntity<List<Player>>(result, HttpStatus.OK);
		} catch (InsufficientPlayersException e) {
			return new ResponseEntity<List<Player>>(HttpStatus.FAILED_DEPENDENCY);
		} catch (GameFinalizedException e) {
			return new ResponseEntity<List<Player>>(HttpStatus.METHOD_NOT_ALLOWED);
		}
	}
	
	@PutMapping
	@RequestMapping("/{id}/decide") 
	public ResponseEntity<String> decideWinner(@PathVariable int id){
		try {
			String winner = service.decideWinner(id);
			logger.info("Deciding winner for game with id: " + id);
			return new ResponseEntity<String>(winner, HttpStatus.OK);
		} catch (DidntDealException e) {
			return new ResponseEntity<String>("Please deal cards",HttpStatus.FAILED_DEPENDENCY);
		} catch (GameFinalizedException e) {
			return new ResponseEntity<String>("Game already closed",HttpStatus.METHOD_NOT_ALLOWED);
		}
	}
	
	
}
