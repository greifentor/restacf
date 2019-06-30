package rest.acf.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A model for a class comment source.
 * 
 * @author ollie
 *
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class ClassCommentSourceModel {

	private String comment;

}