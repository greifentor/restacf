package rest.acf.generator.openapi;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * A container for the open API model root element.
 *
 * @author ollie (17.11.2019)
 */
@Accessors(chain = true)
@Data
public class OpenAPIModel {

	private String title;
	private String description;
	private String termsOfService;
	private ContactModel contact;
	private LicenseModel license;
	private String version;

}