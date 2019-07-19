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
		assertThat(this.unitUnderTest.typeSOToTypeString(null, false), nullValue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfARRAY_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.ARRAY);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO, false);
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfBIGINT_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "long";
		TypeSO typeSO = new TypeSO().setSqlType(Types.BIGINT);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfBIGINTNullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Long";
		TypeSO typeSO = new TypeSO().setSqlType(Types.BIGINT);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfBINARY_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.BINARY);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO, false);
	}

	public void typeSOToTypeSourceModel_PassTypeSOOfBIT_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Boolean";
		TypeSO typeSO = new TypeSO().setSqlType(Types.BIT);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfBLOB_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.BLOB);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO, false);
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfBOOLEAN_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "boolean";
		TypeSO typeSO = new TypeSO().setSqlType(Types.BOOLEAN);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfBOOLEANNullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Boolean";
		TypeSO typeSO = new TypeSO().setSqlType(Types.BOOLEAN);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfCHAR1_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "char";
		TypeSO typeSO = new TypeSO().setSqlType(Types.CHAR).setLength(1);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfCHAR1Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Character";
		TypeSO typeSO = new TypeSO().setSqlType(Types.CHAR).setLength(1);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfCHAR50_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		TypeSO typeSO = new TypeSO().setSqlType(Types.CHAR).setLength(50);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfCHAR50Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		TypeSO typeSO = new TypeSO().setSqlType(Types.CHAR).setLength(50);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfCLOB_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.CLOB);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO, false);
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfDATALINK_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.DATALINK);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO, false);
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfDATE_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "LocalDate";
		TypeSO typeSO = new TypeSO().setSqlType(Types.DATE);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfDATENullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "LocalDate";
		TypeSO typeSO = new TypeSO().setSqlType(Types.DATE);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfDECIMAL_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "double";
		TypeSO typeSO = new TypeSO().setSqlType(Types.DECIMAL);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfDECIMALNullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Double";
		TypeSO typeSO = new TypeSO().setSqlType(Types.DECIMAL);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfDISTINCT_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.DISTINCT);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO, false);
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfDOUBLE_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "double";
		TypeSO typeSO = new TypeSO().setSqlType(Types.DOUBLE);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfDOUBLENullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Double";
		TypeSO typeSO = new TypeSO().setSqlType(Types.DOUBLE);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfFLOAT_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "float";
		TypeSO typeSO = new TypeSO().setSqlType(Types.FLOAT);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfFLOATNullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Float";
		TypeSO typeSO = new TypeSO().setSqlType(Types.FLOAT);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfINTEGER_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "int";
		TypeSO typeSO = new TypeSO().setSqlType(Types.INTEGER);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfINTEGERNullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Integer";
		TypeSO typeSO = new TypeSO().setSqlType(Types.INTEGER);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfJAVA_OBJECT_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.JAVA_OBJECT);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO, false);
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfLONGNVARCHAR1_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "char";
		TypeSO typeSO = new TypeSO().setSqlType(Types.LONGNVARCHAR).setLength(1);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfLONGNVARCHAR1Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Character";
		TypeSO typeSO = new TypeSO().setSqlType(Types.LONGNVARCHAR).setLength(1);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfLONGNVARCHAR50_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		TypeSO typeSO = new TypeSO().setSqlType(Types.LONGNVARCHAR).setLength(50);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfLONGNVARCHAR50Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		TypeSO typeSO = new TypeSO().setSqlType(Types.LONGNVARCHAR).setLength(50);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfLONGVARBINARY_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.LONGVARBINARY);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO, false);
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfLONGVARCHAR1_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "char";
		TypeSO typeSO = new TypeSO().setSqlType(Types.LONGVARCHAR).setLength(1);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfLONGVARCHAR1Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Character";
		TypeSO typeSO = new TypeSO().setSqlType(Types.LONGVARCHAR).setLength(1);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfLONGVARCHAR50_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		TypeSO typeSO = new TypeSO().setSqlType(Types.LONGVARCHAR).setLength(50);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfLONGVARCHAR50Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		TypeSO typeSO = new TypeSO().setSqlType(Types.LONGVARCHAR).setLength(50);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfNCHAR1_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "char";
		TypeSO typeSO = new TypeSO().setSqlType(Types.NCHAR).setLength(1);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfNCHAR1Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Character";
		TypeSO typeSO = new TypeSO().setSqlType(Types.NCHAR).setLength(1);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfNCHAR50_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		TypeSO typeSO = new TypeSO().setSqlType(Types.NCHAR).setLength(50);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfNCHAR50Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		TypeSO typeSO = new TypeSO().setSqlType(Types.NCHAR).setLength(50);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfNCLOB_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.NCLOB);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO, false);
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfNULL_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.NULL);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO, false);
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfNUMERIC_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "double";
		TypeSO typeSO = new TypeSO().setSqlType(Types.NUMERIC);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfNUMERICNullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Double";
		TypeSO typeSO = new TypeSO().setSqlType(Types.NUMERIC);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfNVARCHAR1_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "char";
		TypeSO typeSO = new TypeSO().setSqlType(Types.NVARCHAR).setLength(1);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfNVARCHAR1Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Character";
		TypeSO typeSO = new TypeSO().setSqlType(Types.NVARCHAR).setLength(1);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfNVARCHAR50_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		TypeSO typeSO = new TypeSO().setSqlType(Types.NVARCHAR).setLength(50);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfNVARCHAR50Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		TypeSO typeSO = new TypeSO().setSqlType(Types.NVARCHAR).setLength(50);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfOTHER_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.OTHER);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO, false);
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfREAL_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "double";
		TypeSO typeSO = new TypeSO().setSqlType(Types.REAL);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfREALNullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Double";
		TypeSO typeSO = new TypeSO().setSqlType(Types.REAL);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfREF_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.REF);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO, false);
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfREF_CURSOR_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.REF_CURSOR);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO, false);
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfROWID_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.ROWID);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO, false);
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfSMALLINT_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "short";
		TypeSO typeSO = new TypeSO().setSqlType(Types.SMALLINT);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfSMALLINTNullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Short";
		TypeSO typeSO = new TypeSO().setSqlType(Types.SMALLINT);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfSQLXML_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.SQLXML);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO, false);
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfSTRUCT_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.STRUCT);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO, false);
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfTIME_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "LocalTime";
		TypeSO typeSO = new TypeSO().setSqlType(Types.TIME);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfTIMENullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "LocalTime";
		TypeSO typeSO = new TypeSO().setSqlType(Types.TIME);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfTIME_WITH_TIMEZONE_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.TIME_WITH_TIMEZONE);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO, false);
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfTIMESTAMP_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "LocalDateTime";
		TypeSO typeSO = new TypeSO().setSqlType(Types.TIMESTAMP);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfTIMESTAMPNullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "LocalDateTime";
		TypeSO typeSO = new TypeSO().setSqlType(Types.TIMESTAMP);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfTIMESTAMP_WITH_TIMEZONE_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "ZonedDateTime";
		TypeSO typeSO = new TypeSO().setSqlType(Types.TIMESTAMP_WITH_TIMEZONE);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfTIMESTAMP_WITH_TIMEZONENullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "ZonedDateTime";
		TypeSO typeSO = new TypeSO().setSqlType(Types.TIMESTAMP_WITH_TIMEZONE);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfTINYINT_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "byte";
		TypeSO typeSO = new TypeSO().setSqlType(Types.TINYINT);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfTINYINTNullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Byte";
		TypeSO typeSO = new TypeSO().setSqlType(Types.TINYINT);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeSOToTypeSourceModel_PassTypeSOOfVARBINARY_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		TypeSO typeSO = new TypeSO().setSqlType(Types.VARBINARY);
		// Run
		this.unitUnderTest.typeSOToTypeString(typeSO, false);
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfVARCHAR1_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "char";
		TypeSO typeSO = new TypeSO().setSqlType(Types.VARCHAR).setLength(1);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfVARCHAR1Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "Character";
		TypeSO typeSO = new TypeSO().setSqlType(Types.VARCHAR).setLength(1);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfVARCHAR50_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		TypeSO typeSO = new TypeSO().setSqlType(Types.VARCHAR).setLength(50);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, false);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void typeSOToTypeSourceModel_PassTypeSOOfVARCHAR50Nullable_ReturnsAStringWithTheCorrectJavaType() {
		// Prepare
		String expected = "String";
		TypeSO typeSO = new TypeSO().setSqlType(Types.VARCHAR).setLength(50);
		// Run
		String returned = this.unitUnderTest.typeSOToTypeString(typeSO, true);
		// Check
		assertThat(returned, equalTo(expected));
	}

}