package rest.acf;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import archimedes.acf.checker.ModelChecker;
import archimedes.acf.checker.ModelCheckerDomainSetForAllColumns;
import archimedes.acf.event.CodeFactoryListener;
import archimedes.gui.checker.ModelCheckerMessageListFrameListener;
import archimedes.legacy.acf.event.CodeFactoryProgressionEvent;
import archimedes.legacy.acf.event.CodeFactoryProgressionEventProvider;
import archimedes.legacy.acf.event.CodeFactoryProgressionListener;
import archimedes.legacy.acf.gui.StandardCodeFactoryProgressionFrameUser;
import archimedes.legacy.checkers.ModelCheckerNoComplexPrimaryKey;
import archimedes.model.CodeFactory;
import archimedes.model.DataModel;
import baccara.gui.GUIBundle;
import de.ollie.archimedes.alexandrian.service.so.DatabaseSO;
import de.ollie.archimedes.alexandrian.service.so.SchemeSO;
import de.ollie.archimedes.alexandrian.service.so.TableSO;
import rest.acf.generator.ApplicationClassGenerator;
import rest.acf.generator.ApplicationPropertiesGenerator;
import rest.acf.generator.InitialDBXMLGenerator;
import rest.acf.generator.converter.DataModelToSOConverter;
import rest.acf.generator.converter.ModelToJavaSourceCodeConverter;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.persistence.CRUDRepositoryInterfaceGenerator;
import rest.acf.generator.persistence.DBOConverterClassGenerator;
import rest.acf.generator.persistence.DBOJPAClassGenerator;
import rest.acf.generator.persistence.PersistenceAdapterClassGenerator;
import rest.acf.generator.rest.DTOClassGenerator;
import rest.acf.generator.rest.DTOConverterClassGenerator;
import rest.acf.generator.rest.ResultPageDTOClassGenerator;
import rest.acf.generator.rest.TemplateRESTControllerClassGenerator;
import rest.acf.generator.service.PersistenceExceptionClassGenerator;
import rest.acf.generator.service.PersistencePortInterfaceGenerator;
import rest.acf.generator.service.ResultPageSOClassGenerator;
import rest.acf.generator.service.SOClassGenerator;
import rest.acf.generator.service.ServiceImplClassGenerator;
import rest.acf.generator.service.ServiceInterfaceGenerator;
import rest.acf.generator.utils.ClassSourceModelUtils;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.InterfaceSourceModel;

/**
 * A basic class for all REST code generators.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 17.06.2019 - Added.
 */
public class RESTServerCodeFactory
		implements CodeFactory, CodeFactoryProgressionEventProvider, StandardCodeFactoryProgressionFrameUser {

	public static final String DO_NOT_CHANGE_TAG = "GENERATED CODE!!! DO NOT CHANGE!!!";

	private static final Logger LOG = Logger.getLogger(RESTServerCodeFactory.class);
	private static final String FACTORY_NAME = "REST Server Code Factory";

	private static final String BASE_PACKAGE_NAME = "${base.package.name}";
	private static final String DOT_JAVA = ".java";
	private static final String GENERATE_CODE = "Generate Code";
	private static final String IGNORED_BY_PACKAGE = "ignored by package: ";
	private static final String REST_ACF = "rest-acf";

	private DataModel dataModel = null;
	private GUIBundle guiBundle = null;
	private List<CodeFactoryListener> listeners = new ArrayList<>();

	private List<CodeFactoryProgressionListener> progressListeners = new ArrayList<>();

	@Override
	public void addCodeFactoryListener(CodeFactoryListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public boolean generate(String path) {
		LOG.info("Started code generation");
		new File(path).mkdirs();
		DatabaseSO databaseSO = new DataModelToSOConverter().convert(this.dataModel);
		ClassCodeFactory[] classCodeFactories = new ClassCodeFactory[] {
				new DBOJPAClassGenerator(new ClassSourceModelUtils(new NameConverter(), new TypeConverter()),
						new NameConverter(), new TypeConverter()),
				new DBOConverterClassGenerator(new ClassSourceModelUtils(new NameConverter(), new TypeConverter()),
						new NameConverter(), new TypeConverter()),
				new DTOClassGenerator(new ClassSourceModelUtils(new NameConverter(), new TypeConverter()),
						new NameConverter(), new TypeConverter()),
				new DTOConverterClassGenerator(new ClassSourceModelUtils(new NameConverter(), new TypeConverter()),
						new NameConverter(), new TypeConverter()),
				new SOClassGenerator(new ClassSourceModelUtils(new NameConverter(), new TypeConverter()),
						new NameConverter(), new TypeConverter()),
				new PersistenceAdapterClassGenerator(
						new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter(),
						new TypeConverter(), databaseSO),
//				new RESTControllerClassGenerator(new ClassSourceModelUtils(new NameConverter(), new TypeConverter()),
//						new NameConverter(), new TypeConverter()),
				new ServiceImplClassGenerator(new ClassSourceModelUtils(new NameConverter(), new TypeConverter()),
						new NameConverter(), new TypeConverter(), databaseSO) };
		CRUDRepositoryInterfaceGenerator crudRepositoryGenerator = new CRUDRepositoryInterfaceGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter(),
				new TypeConverter(), databaseSO);
		PersistencePortInterfaceGenerator persistencePortGenerator = new PersistencePortInterfaceGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter(),
				new TypeConverter(), databaseSO);
		ServiceInterfaceGenerator serviceGenerator = new ServiceInterfaceGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter(),
				new TypeConverter(), databaseSO);
		String basePackageName = this.dataModel.getBasePackageName();
		Counter currentStep = new Counter();
		createApplicationClass(databaseSO, path, basePackageName, currentStep);
		createApplicationProperties(databaseSO, path, currentStep);
		createInitialDBXML(databaseSO, path, REST_ACF, currentStep);
		createPersistenceExceptionClass(databaseSO, path, REST_ACF, basePackageName, currentStep);
		createResultPageDTOClass(databaseSO, path, REST_ACF, basePackageName, currentStep);
		createResultPageSOClass(databaseSO, path, REST_ACF, basePackageName, currentStep);
		createRESTControllerClass(databaseSO, path, REST_ACF, basePackageName, currentStep);
		int tableCount = 0;
		for (SchemeSO scheme : databaseSO.getSchemes()) {
			tableCount += scheme.getTables().size();
		}
		int maxSteps = tableCount * (3 + classCodeFactories.length + 1) + 6;
		fireCodeFactoryProgressEvent(
				new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE, null, 0, 0, 1, maxSteps));
		for (SchemeSO scheme : databaseSO.getSchemes()) {
			for (TableSO table : scheme.getTables()) {
				for (ClassCodeFactory ccf : classCodeFactories) {
					ClassSourceModel csm = ccf.generate(table, REST_ACF);
					csm.getPackageModel().setPackageName(
							csm.getPackageModel().getPackageName().replace(BASE_PACKAGE_NAME, basePackageName));
					if (!isIgnoredByPackage(databaseSO, csm.getPackageModel().getPackageName())) {
						String p = path + "/" + csm.getPackageModel().getPackageName().replace(".", "/");
						new File(p).mkdirs();
						String code = new ModelToJavaSourceCodeConverter().classSourceModelToJavaSourceCode(csm);
						code = code.replace(BASE_PACKAGE_NAME, basePackageName);
						try {
							writeFile(Paths.get(p + "/" + csm.getName() + DOT_JAVA), code.getBytes());
						} catch (Exception e) {
							printStackTrace(e);
						}
						fireCodeFactoryProgressEvent(new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE,
								"generated class: " + csm.getClass().getSimpleName(), 0, currentStep.inc()));
					} else {
						fireCodeFactoryProgressEvent(new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE,
								IGNORED_BY_PACKAGE + csm.getClass().getSimpleName(), 0, currentStep.inc()));
					}
				}
			}
			for (TableSO table : scheme.getTables()) {
				InterfaceSourceModel ism = crudRepositoryGenerator.generate(table, REST_ACF);
				if (ism != null) {
					ism.getPackageModel().setPackageName(
							ism.getPackageModel().getPackageName().replace(BASE_PACKAGE_NAME, basePackageName));
					if (!isIgnoredByPackage(databaseSO, ism.getPackageModel().getPackageName())) {
						String p = path + "/" + ism.getPackageModel().getPackageName().replace(".", "/");
						new File(p).mkdirs();
						String code = new ModelToJavaSourceCodeConverter().interfaceSourceModelToJavaSourceCode(ism);
						code = code.replace(BASE_PACKAGE_NAME, basePackageName);
						try {
							writeFile(Paths.get(p + "/" + ism.getName() + DOT_JAVA), code.getBytes());
						} catch (Exception e) {
							printStackTrace(e);
						}
						fireCodeFactoryProgressEvent(new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE,
								"generated CRUD repository interface: " + ism.getClass().getSimpleName(), 0,
								currentStep.inc()));
					} else {
						fireCodeFactoryProgressEvent(new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE,
								IGNORED_BY_PACKAGE + ism.getClass().getSimpleName(), 0, currentStep.inc()));
					}
				} else {
					fireCodeFactoryProgressEvent(
							new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE, null, 0, currentStep.inc()));
				}
			}
			for (TableSO table : scheme.getTables()) {
				InterfaceSourceModel ism = persistencePortGenerator.generate(table, REST_ACF);
				if (ism != null) {
					ism.getPackageModel().setPackageName(
							ism.getPackageModel().getPackageName().replace(BASE_PACKAGE_NAME, basePackageName));
					if (!isIgnoredByPackage(databaseSO, ism.getPackageModel().getPackageName())) {
						String p = path + "/" + ism.getPackageModel().getPackageName().replace(".", "/");
						new File(p).mkdirs();
						String code = new ModelToJavaSourceCodeConverter().interfaceSourceModelToJavaSourceCode(ism);
						code = code.replace(BASE_PACKAGE_NAME, basePackageName);
						try {
							writeFile(Paths.get(p + "/" + ism.getName() + DOT_JAVA), code.getBytes());
						} catch (Exception e) {
							printStackTrace(e);
						}
						fireCodeFactoryProgressEvent(new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE,
								"generated persistence port interface: " + ism.getClass().getSimpleName(), 0,
								currentStep.inc()));
					} else {
						fireCodeFactoryProgressEvent(new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE,
								IGNORED_BY_PACKAGE + ism.getClass().getSimpleName(), 0, currentStep.inc()));
					}
				} else {
					fireCodeFactoryProgressEvent(
							new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE, null, 0, currentStep.inc()));
				}
			}
			for (TableSO table : scheme.getTables()) {
				InterfaceSourceModel ism = serviceGenerator.generate(table, REST_ACF);
				if (ism != null) {
					ism.getPackageModel().setPackageName(
							ism.getPackageModel().getPackageName().replace(BASE_PACKAGE_NAME, basePackageName));
					if (!isIgnoredByPackage(databaseSO, ism.getPackageModel().getPackageName())) {
						String p = path + "/" + ism.getPackageModel().getPackageName().replace(".", "/");
						new File(p).mkdirs();
						String code = new ModelToJavaSourceCodeConverter().interfaceSourceModelToJavaSourceCode(ism);
						code = code.replace(BASE_PACKAGE_NAME, basePackageName);
						try {
							writeFile(Paths.get(p + "/" + ism.getName() + DOT_JAVA), code.getBytes());
						} catch (Exception e) {
							printStackTrace(e);
						}
						fireCodeFactoryProgressEvent(new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE,
								"generated service interface: " + ism.getClass().getSimpleName(), 0,
								currentStep.inc()));
					} else {
						fireCodeFactoryProgressEvent(new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE,
								IGNORED_BY_PACKAGE + ism.getClass().getSimpleName(), 0, currentStep.inc()));
					}
				} else {
					fireCodeFactoryProgressEvent(
							new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE, null, 0, currentStep.inc()));
				}
			}
		}
		return false;
	}

	private void printStackTrace(Exception e) {
		e.printStackTrace();
	}

	private boolean isIgnoredByPackage(DatabaseSO databaseSO, String packageName) {
		return databaseSO.getOptionByName("IGNORE_BY_PACKAGE") //
				.map(option -> {
					for (String s : StringUtils.split(option.getValue(), ',')) {
						s = "." + s.trim() + ".";
						if (packageName.contains(s)) {
							return true;
						}
					}
					return false;
				}) //
				.orElse(false) //
		;
	}

	private void createApplicationClass(DatabaseSO databaseSO, String path, String basePackageName,
			Counter currentStep) {
		ApplicationClassGenerator applicationClassGenerator = new ApplicationClassGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter(),
				new TypeConverter());
		ClassSourceModel csm = applicationClassGenerator.generate(databaseSO, REST_ACF);
		csm.getPackageModel()
				.setPackageName(csm.getPackageModel().getPackageName().replace(BASE_PACKAGE_NAME, basePackageName));
		if (!isIgnoredByPackage(databaseSO, csm.getPackageModel().getPackageName())) {
			String p = path + "/" + csm.getPackageModel().getPackageName().replace(".", "/");
			new File(p).mkdirs();
			String code = new ModelToJavaSourceCodeConverter().classSourceModelToJavaSourceCode(csm);
			code = code.replace(BASE_PACKAGE_NAME, basePackageName);
			try {
				writeFile(Paths.get(p + "/" + csm.getName() + DOT_JAVA), code.getBytes());
			} catch (Exception e) {
				printStackTrace(e);
			}
			fireCodeFactoryProgressEvent(new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE,
					"generated application class: " + csm.getClass().getSimpleName(), 0, currentStep.inc()));
		} else {
			fireCodeFactoryProgressEvent(new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE,
					IGNORED_BY_PACKAGE + csm.getClass().getSimpleName(), 0, currentStep.inc()));
		}
	}

	private void createApplicationProperties(DatabaseSO databaseSO, String path, Counter currentStep) {
		String p = path + "/../resources";
		String fileName = "application.properties";
		new File(p).mkdirs();
		String code = new ApplicationPropertiesGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter())
						.generate(databaseSO);
		try {
			writeFile(Paths.get(p + "/" + fileName), code.getBytes());
		} catch (Exception e) {
			printStackTrace(e);
		}
		fireCodeFactoryProgressEvent(new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE,
				"generated application properties file: " + fileName, 0, currentStep.inc()));
	}

	private void createInitialDBXML(DatabaseSO databaseSO, String path, String authorName, Counter currentStep) {
		String p = path + "/../resources/db/change-log/InitialDB";
		String fileName = "InitialDB.xml";
		new File(p).mkdirs();
		String code = new InitialDBXMLGenerator(new ClassSourceModelUtils(new NameConverter(), new TypeConverter()),
				new NameConverter(), new TypeConverter()).generate(databaseSO, authorName);
		try {
			writeFile(Paths.get(p + "/" + fileName), code.getBytes());
		} catch (Exception e) {
			printStackTrace(e);
		}
		fireCodeFactoryProgressEvent(new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE,
				"generated initial DB XML file: " + fileName, 0, currentStep.inc()));
	}

	private void createPersistenceExceptionClass(DatabaseSO databaseSO, String path, String authorName,
			String basePackageName, Counter currentStep) {
		PersistenceExceptionClassGenerator persistenceExceptionClassGenerator = new PersistenceExceptionClassGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter(),
				new TypeConverter());
		ClassSourceModel csm = persistenceExceptionClassGenerator.generate(REST_ACF);
		csm.getPackageModel()
				.setPackageName(csm.getPackageModel().getPackageName().replace(BASE_PACKAGE_NAME, basePackageName));
		if (!isIgnoredByPackage(databaseSO, csm.getPackageModel().getPackageName())) {
			String p = path + "/" + csm.getPackageModel().getPackageName().replace(".", "/");
			new File(p).mkdirs();
			String code = new ModelToJavaSourceCodeConverter().classSourceModelToJavaSourceCode(csm);
			code = code.replace(BASE_PACKAGE_NAME, basePackageName);
			try {
				writeFile(Paths.get(p + "/" + csm.getName() + DOT_JAVA), code.getBytes());
			} catch (Exception e) {
				printStackTrace(e);
			}
			fireCodeFactoryProgressEvent(new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE,
					"generated persistence exception class", 0, currentStep.inc()));
		} else {
			fireCodeFactoryProgressEvent(new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE,
					IGNORED_BY_PACKAGE + csm.getClass().getSimpleName(), 0, currentStep.inc()));
		}
	}

	private void createResultPageDTOClass(DatabaseSO databaseSO, String path, String authorName, String basePackageName,
			Counter currentStep) {
		ResultPageDTOClassGenerator resultPageDTOClassGenerator = new ResultPageDTOClassGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter(),
				new TypeConverter());
		ClassSourceModel csm = resultPageDTOClassGenerator.generate(REST_ACF);
		csm.getPackageModel()
				.setPackageName(csm.getPackageModel().getPackageName().replace(BASE_PACKAGE_NAME, basePackageName));
		if (!isIgnoredByPackage(databaseSO, csm.getPackageModel().getPackageName())) {
			String p = path + "/" + csm.getPackageModel().getPackageName().replace(".", "/");
			new File(p).mkdirs();
			String code = new ModelToJavaSourceCodeConverter().classSourceModelToJavaSourceCode(csm);
			code = code.replace(BASE_PACKAGE_NAME, basePackageName);
			try {
				writeFile(Paths.get(p + "/" + csm.getName() + DOT_JAVA), code.getBytes());
			} catch (Exception e) {
				printStackTrace(e);
			}
			fireCodeFactoryProgressEvent(new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE,
					"generated result page DTO class", 0, currentStep.inc()));
		} else {
			fireCodeFactoryProgressEvent(new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE,
					IGNORED_BY_PACKAGE + csm.getClass().getSimpleName(), 0, currentStep.inc()));
		}
	}

	private void createResultPageSOClass(DatabaseSO databaseSO, String path, String authorName, String basePackageName,
			Counter currentStep) {
		ResultPageSOClassGenerator resultPageSOClassGenerator = new ResultPageSOClassGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter(),
				new TypeConverter());
		ClassSourceModel csm = resultPageSOClassGenerator.generate(REST_ACF);
		csm.getPackageModel()
				.setPackageName(csm.getPackageModel().getPackageName().replace(BASE_PACKAGE_NAME, basePackageName));
		if (!isIgnoredByPackage(databaseSO, csm.getPackageModel().getPackageName())) {
			String p = path + "/" + csm.getPackageModel().getPackageName().replace(".", "/");
			new File(p).mkdirs();
			String code = new ModelToJavaSourceCodeConverter().classSourceModelToJavaSourceCode(csm);
			code = code.replace(BASE_PACKAGE_NAME, basePackageName);
			try {
				writeFile(Paths.get(p + "/" + csm.getName() + DOT_JAVA), code.getBytes());
			} catch (Exception e) {
				printStackTrace(e);
			}
			fireCodeFactoryProgressEvent(new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE,
					"generated result page SO class", 0, currentStep.inc()));
		} else {
			fireCodeFactoryProgressEvent(new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE,
					IGNORED_BY_PACKAGE + csm.getClass().getSimpleName(), 0, currentStep.inc()));
		}
	}

	private void createRESTControllerClass(DatabaseSO databaseSO, String path, String authorName,
			String basePackageName, Counter currentStep) {
		TemplateRESTControllerClassGenerator restControllerClassGenerator = new TemplateRESTControllerClassGenerator();
		ClassSourceModelUtils utils = new ClassSourceModelUtils(new NameConverter(), new TypeConverter());
		for (SchemeSO scheme : databaseSO.getSchemes()) {
			for (TableSO table : scheme.getTables()) {
				String code = restControllerClassGenerator.generate(table, databaseSO, REST_ACF);
				String className = utils.createRESTControllerClassSourceModel(table).getName();
//		csm.getPackageModel().setPackageName(
//				csm.getPackageModel().getPackageName().replace(BASE_PACKAGE_NAME, basePackageName));
//		String p = path + "/" + csm.getPackageModel().getPackageName().replace(".", "/");
				if (!isIgnoredByPackage(databaseSO, (basePackageName + ".rest.v1.controller"))) {
					String p = path + "/" + (basePackageName + ".rest.v1.controller").replace(".", "/");
					new File(p).mkdirs();
					code = code.replace(BASE_PACKAGE_NAME, basePackageName);
					try {
						writeFile(Paths.get(p + "/" + className + DOT_JAVA), code.getBytes());
					} catch (Exception e) {
						printStackTrace(e);
					}
					fireCodeFactoryProgressEvent(new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE,
							"generated REST controller class: " + className, 0, currentStep.inc()));
				} else {
					fireCodeFactoryProgressEvent(new CodeFactoryProgressionEvent(FACTORY_NAME, GENERATE_CODE,
							IGNORED_BY_PACKAGE + className, 0, currentStep.inc()));
				}
			}
		}
	}

	private void writeFile(Path path, byte[] content) throws IOException {
		if (new File(path.toString()).exists() && !Files.readString(path).contains(DO_NOT_CHANGE_TAG)) {
			return;
		}
		Files.write(path, content);
		System.out.println(path.toString());
	}

	@Override
	public void removeCodeFactoryListener(CodeFactoryListener listener) {
		this.listeners.remove(listener);
	}

	@Override
	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	@Override
	public GUIBundle getGUIBundle() {
		return this.guiBundle;
	}

	@Override
	public ModelChecker[] getModelCheckers() {
		return new ModelChecker[] { //
				new ModelCheckerDomainSetForAllColumns(this.getGUIBundle()), //
				new ModelCheckerNoComplexPrimaryKey(this.getGUIBundle()) //
		};
	}

	@Override
	public String getName() {
		return "REST server code factory";
	}

	@Override
	public String[] getResourceBundleNames() {
		return new String[] { //
				"rest-server" //
		};
	}

	@Override
	public String getVersion() {
		return "1.1.1";
	}

	@Override
	public void setGUIBundle(GUIBundle guiBundle) {
		this.guiBundle = guiBundle;
	}

	@Override
	public void setModelCheckerMessageListFrameListeners(ModelCheckerMessageListFrameListener... l) {
		// NOP
	}

	@Override
	public void addCodeFactoryProgressionListener(CodeFactoryProgressionListener listener) {
		if (listener != null) {
			this.progressListeners.add(listener);
		}
	}

	@Override
	public void removeCodeFactoryProgressionListener(CodeFactoryProgressionListener listener) {
		if (listener != null) {
			this.progressListeners.remove(listener);
		}
	}

	protected void fireCodeFactoryProgressEvent(CodeFactoryProgressionEvent event) {
		this.progressListeners //
				.forEach(l -> {
					try {
						l.progressionDetected(event);
					} catch (Exception e) {
						printStackTrace(e);
					}
				});
	}

}

class Counter {

	private int value = 0;

	public Counter() {
		super();
	}

	public int inc() {
		return ++this.value;
	}

}