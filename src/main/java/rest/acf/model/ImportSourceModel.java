package rest.acf.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A model for an import source.
 * 
 * @author ollie
 *
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class ImportSourceModel {

	private PackageSourceModel packageModel;

}