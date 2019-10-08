package de.ollie.library.rest.v1.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import de.ollie.library.rest.v1.dto.ResultPageDTO;
import de.ollie.library.service.RackService;
import de.ollie.library.service.so.RackSO;
import de.ollie.library.service.so.ResultPageSO;
import de.ollie.library.rest.v1.converter.BookDTOConverter;
import de.ollie.library.service.BookService;
import de.ollie.library.rest.v1.dto.BookDTO;
import de.ollie.library.service.so.BookSO;

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
	@Autowired
	private RackDTOConverter rackDTOConverter;
	@Autowired
	private RackService rackService;
	@Autowired
	private BookDTOConverter bookDTOConverter;
	@Autowired
	private BookService bookService;

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

	@GetMapping
	public ResponseEntity<ResultPageDTO<RackDTO>> findAll() {
		try {
			List<RackDTO> dtos = new ArrayList<>();
			ResultPageSO<RackSO> result = this.rackService.findAll();
			for (RackSO so : result.getResults()) {
				dtos.add(this.rackDTOConverter.convertSOToDTO(so));
			}
			return ResponseEntity.ok() //
					.body(new ResultPageDTO<RackDTO>() //
							.setCurrentPage(result.getCurrentPage()) //
							.setResultsPerPage(result.getResultsPerPage()) //
							.setResults(dtos) //
							.setTotalResults(result.getTotalResults()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
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

	@GetMapping("/{id}/books")
	public ResponseEntity<ResultPageDTO<BookDTO>> findBooksForRack(@PathVariable("id") long rackId) {
		try {
			List<BookDTO> dtos = new ArrayList<>();
			ResultPageSO<BookSO> result = this.bookService.findBooksForRack(rackId);
			for (BookSO so : result.getResults()) {
				dtos.add(this.bookDTOConverter.convertSOToDTO(so));
			}
			return ResponseEntity.ok() //
					.body(new ResultPageDTO<BookDTO>() //
							.setCurrentPage(result.getCurrentPage()) //
							.setResultsPerPage(result.getResultsPerPage()) //
							.setResults(dtos) //
							.setTotalResults(result.getTotalResults()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}