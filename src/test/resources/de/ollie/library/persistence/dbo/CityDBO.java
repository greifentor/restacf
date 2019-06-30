package ${base.package.name}.persistence.dbo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="City")
public CityDBO {

	private long id;
	private String name;

}