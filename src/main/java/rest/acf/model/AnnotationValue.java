package rest.acf.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A container for annotation values.
 *
 * @author Oliver.Lieshoff
 *
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class AnnotationValue {

	private String value;
	private boolean quoted = true;

	public String getPropertyContent() {
		return (quoted ? "\"" : "") + value + (quoted ? "\"" : "");
	}

}