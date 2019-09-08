package de.ollie.library.service.so;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * An object to manage paged results.
 *
 * @param <T> The type of the managed results.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
@Accessors(chain = true)
@Data
public class ResultPageSO<T> {

	private int currentPage;
	private int resultsPerPage;
	private List<T> results = new ArrayList<>();
	private int totalResults;

}