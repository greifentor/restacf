package de.ollie.library.service.converter;

import java.util.Optional;

import org.springframework.stereotype.Component;

/**
 * A converter for book objects.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
@Component
public class BookSOConverter {

	@Autowired
	private CitySOConverter citySOConverter;

	/**
	 * Converts the passed database object into a service object.
	 * 
	 * @param dbo The database object to convert.
	 * @return A service object with the data of the passed database object.
	 */
	public BookSO dboToSO(BookDBO dbo) {
		if (dbo == null) {
			return null;
		}
		return new BookSO().setId(dbo.getId()).setCityOfPublication(this.citySOConverter.dboToSO(dbo.getCityObPublication())).setTitle(dbo.getTitle()).setYearOfPublication(dbo.getYearOfPublication());
	}

}