package de.ollie.library.rest.v1.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.ollie.library.rest.v1.converter.RackDTOConverter;
import de.ollie.library.rest.v1.dto.RackDTO;
import de.ollie.library.service.RackService;
import de.ollie.library.service.so.RackSO;

/**
 * A REST controller for racks.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
@RestController
@RequestMapping("api/v1/racks")
public class RackRESTController {

	private final Logger logger = LogManager.getLogger(RackRESTController.class);
	private final RackDTOConverter rackDTOConverter;
	private final RackService rackService;

	public RackRESTController(RackDTOConverter rackDTOConverter, RackService rackService) {
		super();
		this.rackDTOConverter = rackDTOConverter;
		this.rackService = rackService;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable("id") long id) {
		try {
			logger.debug("deleting rack with id: " + id);
			if (!this.rackService.delete(id)) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<RackDTO> findById(@PathVariable("id") long id) {
		try {
			Optional<RackSO> so = this.rackService.findById(id);
			if (so.isEmpty()) {
				logger.debug("no rack found for id: " + id);
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok().body(this.rackDTOConverter.convertSOToDTO(so.get()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping
	public ResponseEntity save(@RequestBody RackDTO dto) {
		RackSO so = this.rackDTOConverter.convertDTOToSO(dto);
		try {
			logger.debug("saving rack: " + so);
			this.rackService.save(so);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok().build();
	}

}