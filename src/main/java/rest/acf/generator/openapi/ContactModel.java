package rest.acf.generator.openapi;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * A container for the open API model contact element
 *
 * @author ollie (17.11.2019)
 */
@Accessors(chain = true)
@Data
public class ContactModel {

	private String name;
	private String url;
	private String email;

}