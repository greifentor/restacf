package rest.acf.generator.converter;

import static org.junit.Assert.assertThat;

import java.sql.Types;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import de.ollie.archimedes.alexandrian.service.TypeSO;

/**
 * Unit tests for class "TypeConverter".
 *
 * @author ollie
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TypeConverterTest {

	@InjectMocks
	private TypeConverter unitUnderTest;

	@Test
	public void typeSOToTypeSourceModel_PassANullValue_ReturnsANullValue() {
		assertThat(this.unitUnderTest.typeSOToTypeString(null), nullValue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfARRAY_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.ARRAY);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO);
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfBIGINT_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "long";
		TypeSO typeSO = new TypeSO().setSqlType(Types.BIGINT);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfBINARY_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.BINARY);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO);
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfBIT_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.BIT);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO);
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfBLOB_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.BLOB);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO);
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfBOOLEAN_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "boolean";
		TypeSO typeSO = new TypeSO().setSqlType(Types.BOOLEAN);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfCHAR1_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "char";
		TypeSO typeSO = new TypeSO().setSqlType(Types.CHAR).setLength(1);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfCHAR50_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		TypeSO typeSO = new TypeSO().setSqlType(Types.CHAR).setLength(50);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfCLOB_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.CLOB);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO);
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfDATALINK_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.DATALINK);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO);
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfDATE_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "LocalDate";
		TypeSO typeSO = new TypeSO().setSqlType(Types.DATE);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfDECIMAL_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "double";
		TypeSO typeSO = new TypeSO().setSqlType(Types.DECIMAL);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfDISTINCT_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.DISTINCT);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO);
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfDOUBLE_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "double";
		TypeSO typeSO = new TypeSO().setSqlType(Types.DOUBLE);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfFLOAT_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "float";
		TypeSO typeSO = new TypeSO().setSqlType(Types.FLOAT);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfINTEGER_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "int";
		TypeSO typeSO = new TypeSO().setSqlType(Types.INTEGER);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfJAVA_OBJECT_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.JAVA_OBJECT);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO);
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfLONGNVARCHAR1_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "char";
		TypeSO typeSO = new TypeSO().setSqlType(Types.LONGNVARCHAR).setLength(1);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfLONGNVARCHAR50_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		TypeSO typeSO = new TypeSO().setSqlType(Types.LONGNVARCHAR).setLength(50);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfLONGVARBINARY_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.LONGVARBINARY);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO);
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfLONGVARCHAR1_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "char";
		TypeSO typeSO = new TypeSO().setSqlType(Types.LONGVARCHAR).setLength(1);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfLONGVARCHAR50_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		TypeSO typeSO = new TypeSO().setSqlType(Types.LONGVARCHAR).setLength(50);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

}