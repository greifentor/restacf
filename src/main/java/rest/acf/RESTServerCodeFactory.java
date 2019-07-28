package rest.acf;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import archimedes.acf.checker.ModelChecker;
import archimedes.acf.event.CodeFactoryListener;
import archimedes.gui.checker.ModelCheckerMessageListFrameListener;
import archimedes.model.CodeFactory;
import archimedes.model.DataModel;
import baccara.gui.GUIBundle;
import de.ollie.archimedes.alexandrian.service.DatabaseSO;
import de.ollie.archimedes.alexandrian.service.SchemeSO;
import de.ollie.archimedes.alexandrian.service.TableSO;
import rest.acf.generator.ApplicationClassGenerator;
import rest.acf.generator.ApplicationPropertiesGenerator;
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
import rest.acf.generator.rest.RESTControllerClassGenerator;
import rest.acf.generator.service.PersistencePortInterfaceGenerator;
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
public class RESTServerCodeFactory implements CodeFactory {

	private static final Logger LOG = Logger.getLogger(RESTServerCodeFactory.class);

	private DataModel dataModel = null;
	private GUIBundle guiBundle = null;
	private List<CodeFactoryListener> listeners = new ArrayList<>();

	@Override
	public void addCodeFactoryListener(CodeFactoryListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public boolean generate(String path) {
		LOG.info("Started code generation");
		new File(path).mkdirs();
		DBOJPAClassGenerator jpaClassGenerator = new DBOJPAClassGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter(),
				new TypeConverter());
		DBOConverterClassGenerator dboConverterClassGenerator = new DBOConverterClassGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter(),
				new TypeConverter());
		DTOClassGenerator dtoClassGenerator = new DTOClassGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter(),
				new TypeConverter());
		DTOConverterClassGenerator dtoConverterClassGenerator = new DTOConverterClassGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter(),
				new TypeConverter());
		SOClassGenerator soClassGenerator = new SOClassGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter(),
				new TypeConverter());
		CRUDRepositoryInterfaceGenerator crudRepositoryGenerator = new CRUDRepositoryInterfaceGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter(),
				new TypeConverter());
		PersistencePortInterfaceGenerator persistencePortGenerator = new PersistencePortInterfaceGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter(),
				new TypeConverter());
		PersistenceAdapterClassGenerator persistenceAdapterGenerator = new PersistenceAdapterClassGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter(),
				new TypeConverter());
		RESTControllerClassGenerator restControllerClassGenerator = new RESTControllerClassGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter(),
				new TypeConverter());
		ServiceInterfaceGenerator serviceGenerator = new ServiceInterfaceGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter(),
				new TypeConverter());
		ServiceImplClassGenerator serviceImplClassGenerator = new ServiceImplClassGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter(),
				new TypeConverter());
		DatabaseSO databaseSO = new DataModelToSOConverter().convert(this.dataModel);
		String basePackageName = this.dataModel.getBasePackageName();
		createApplicationClass(databaseSO, path, basePackageName);
		createApplicationProperties(databaseSO, path);
		for (SchemeSO scheme : databaseSO.getSchemes()) {
			for (TableSO table : scheme.getTables()) {
				ClassSourceModel csm = jpaClassGenerator.generate(table, "rest-acf");
				csm.getPackageModel().setPackageName(
						csm.getPackageModel().getPackageName().replace("${base.package.name}", basePackageName));
				String p = path + "/" + csm.getPackageModel().getPackageName().replace(".", "/");
				new File(p).mkdirs();
				String code = new ModelToJavaSourceCodeConverter().classSourceModelToJavaSourceCode(csm);
				code = code.replace("${base.package.name}", basePackageName);
				try {
					Files.write(Paths.get(p + "/" + csm.getName() + ".java"), code.getBytes(),
							StandardOpenOption.CREATE_NEW);
					System.out.println(p + "/" + csm.getName() + ".java");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (TableSO table : scheme.getTables()) {
				ClassSourceModel csm = dboConverterClassGenerator.generate(table, "rest-acf");
				csm.getPackageModel().setPackageName(
						csm.getPackageModel().getPackageName().replace("${base.package.name}", basePackageName));
				String p = path + "/" + csm.getPackageModel().getPackageName().replace(".", "/");
				new File(p).mkdirs();
				String code = new ModelToJavaSourceCodeConverter().classSourceModelToJavaSourceCode(csm);
				code = code.replace("${base.package.name}", basePackageName);
				try {
					Files.write(Paths.get(p + "/" + csm.getName() + ".java"), code.getBytes(),
							StandardOpenOption.CREATE_NEW);
					System.out.println(p + "/" + csm.getName() + ".java");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (TableSO table : scheme.getTables()) {
				ClassSourceModel csm = soClassGenerator.generate(table, "rest-acf");
				csm.getPackageModel().setPackageName(
						csm.getPackageModel().getPackageName().replace("${base.package.name}", basePackageName));
				String p = path + "/" + csm.getPackageModel().getPackageName().replace(".", "/");
				new File(p).mkdirs();
				String code = new ModelToJavaSourceCodeConverter().classSourceModelToJavaSourceCode(csm);
				code = code.replace("${base.package.name}", basePackageName);
				try {
					Files.write(Paths.get(p + "/" + csm.getName() + ".java"), code.getBytes(),
							StandardOpenOption.CREATE_NEW);
					System.out.println(p + "/" + csm.getName() + ".java");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (TableSO table : scheme.getTables()) {
				ClassSourceModel csm = dtoClassGenerator.generate(table, "rest-acf");
				csm.getPackageModel().setPackageName(
						csm.getPackageModel().getPackageName().replace("${base.package.name}", basePackageName));
				String p = path + "/" + csm.getPackageModel().getPackageName().replace(".", "/");
				new File(p).mkdirs();
				String code = new ModelToJavaSourceCodeConverter().classSourceModelToJavaSourceCode(csm);
				code = code.replace("${base.package.name}", basePackageName);
				try {
					Files.write(Paths.get(p + "/" + csm.getName() + ".java"), code.getBytes(),
							StandardOpenOption.CREATE_NEW);
					System.out.println(p + "/" + csm.getName() + ".java");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (TableSO table : scheme.getTables()) {
				ClassSourceModel csm = dtoConverterClassGenerator.generate(table, "rest-acf");
				csm.getPackageModel().setPackageName(
						csm.getPackageModel().getPackageName().replace("${base.package.name}", basePackageName));
				String p = path + "/" + csm.getPackageModel().getPackageName().replace(".", "/");
				new File(p).mkdirs();
				String code = new ModelToJavaSourceCodeConverter().classSourceModelToJavaSourceCode(csm);
				code = code.replace("${base.package.name}", basePackageName);
				try {
					Files.write(Paths.get(p + "/" + csm.getName() + ".java"), code.getBytes(),
							StandardOpenOption.CREATE_NEW);
					System.out.println(p + "/" + csm.getName() + ".java");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (TableSO table : scheme.getTables()) {
				ClassSourceModel csm = persistenceAdapterGenerator.generate(table, "rest-acf");
				csm.getPackageModel().setPackageName(
						csm.getPackageModel().getPackageName().replace("${base.package.name}", basePackageName));
				String p = path + "/" + csm.getPackageModel().getPackageName().replace(".", "/");
				new File(p).mkdirs();
				String code = new ModelToJavaSourceCodeConverter().classSourceModelToJavaSourceCode(csm);
				code = code.replace("${base.package.name}", basePackageName);
				try {
					Files.write(Paths.get(p + "/" + csm.getName() + ".java"), code.getBytes(),
							StandardOpenOption.CREATE_NEW);
					System.out.println(p + "/" + csm.getName() + ".java");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (TableSO table : scheme.getTables()) {
				ClassSourceModel csm = serviceImplClassGenerator.generate(table, "rest-acf");
				csm.getPackageModel().setPackageName(
						csm.getPackageModel().getPackageName().replace("${base.package.name}", basePackageName));
				String p = path + "/" + csm.getPackageModel().getPackageName().replace(".", "/");
				new File(p).mkdirs();
				String code = new ModelToJavaSourceCodeConverter().classSourceModelToJavaSourceCode(csm);
				code = code.replace("${base.package.name}", basePackageName);
				try {
					Files.write(Paths.get(p + "/" + csm.getName() + ".java"), code.getBytes(),
							StandardOpenOption.CREATE_NEW);
					System.out.println(p + "/" + csm.getName() + ".java");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (TableSO table : scheme.getTables()) {
				ClassSourceModel csm = restControllerClassGenerator.generate(table, "rest-acf");
				csm.getPackageModel().setPackageName(
						csm.getPackageModel().getPackageName().replace("${base.package.name}", basePackageName));
				String p = path + "/" + csm.getPackageModel().getPackageName().replace(".", "/");
				new File(p).mkdirs();
				String code = new ModelToJavaSourceCodeConverter().classSourceModelToJavaSourceCode(csm);
				code = code.replace("${base.package.name}", basePackageName);
				try {
					Files.write(Paths.get(p + "/" + csm.getName() + ".java"), code.getBytes(),
							StandardOpenOption.CREATE_NEW);
					System.out.println(p + "/" + csm.getName() + ".java");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (TableSO table : scheme.getTables()) {
				InterfaceSourceModel ism = crudRepositoryGenerator.generate(table, "rest-acf");
				if (ism != null) {
					ism.getPackageModel().setPackageName(
							ism.getPackageModel().getPackageName().replace("${base.package.name}", basePackageName));
					String p = path + "/" + ism.getPackageModel().getPackageName().replace(".", "/");
					new File(p).mkdirs();
					String code = new ModelToJavaSourceCodeConverter().interfaceSourceModelToJavaSourceCode(ism);
					code = code.replace("${base.package.name}", basePackageName);
					try {
						Files.write(Paths.get(p + "/" + ism.getName() + ".java"), code.getBytes(),
								StandardOpenOption.CREATE_NEW);
						System.out.println(p + "/" + ism.getName() + ".java");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			for (TableSO table : scheme.getTables()) {
				InterfaceSourceModel ism = persistencePortGenerator.generate(table, "rest-acf");
				if (ism != null) {
					ism.getPackageModel().setPackageName(
							ism.getPackageModel().getPackageName().replace("${base.package.name}", basePackageName));
					String p = path + "/" + ism.getPackageModel().getPackageName().replace(".", "/");
					new File(p).mkdirs();
					String code = new ModelToJavaSourceCodeConverter().interfaceSourceModelToJavaSourceCode(ism);
					code = code.replace("${base.package.name}", basePackageName);
					try {
						Files.write(Paths.get(p + "/" + ism.getName() + ".java"), code.getBytes(),
								StandardOpenOption.CREATE_NEW);
						System.out.println(p + "/" + ism.getName() + ".java");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			for (TableSO table : scheme.getTables()) {
				InterfaceSourceModel ism = serviceGenerator.generate(table, "rest-acf");
				if (ism != null) {
					ism.getPackageModel().setPackageName(
							ism.getPackageModel().getPackageName().replace("${base.package.name}", basePackageName));
					String p = path + "/" + ism.getPackageModel().getPackageName().replace(".", "/");
					new File(p).mkdirs();
					String code = new ModelToJavaSourceCodeConverter().interfaceSourceModelToJavaSourceCode(ism);
					code = code.replace("${base.package.name}", basePackageName);
					try {
						Files.write(Paths.get(p + "/" + ism.getName() + ".java"), code.getBytes(),
								StandardOpenOption.CREATE_NEW);
						System.out.println(p + "/" + ism.getName() + ".java");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return false;
	}

	private void createApplicationClass(DatabaseSO databaseSO, String path, String basePackageName) {
		ApplicationClassGenerator applicationClassGenerator = new ApplicationClassGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter(),
				new TypeConverter());
		ClassSourceModel csm = applicationClassGenerator.generate(databaseSO, "rest-acf");
		csm.getPackageModel().setPackageName(
				csm.getPackageModel().getPackageName().replace("${base.package.name}", basePackageName));
		String p = path + "/" + csm.getPackageModel().getPackageName().replace(".", "/");
		new File(p).mkdirs();
		String code = new ModelToJavaSourceCodeConverter().classSourceModelToJavaSourceCode(csm);
		code = code.replace("${base.package.name}", basePackageName);
		try {
			Files.write(Paths.get(p + "/" + csm.getName() + ".java"), code.getBytes(), StandardOpenOption.CREATE_NEW);
			System.out.println(p + "/" + csm.getName() + ".java");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createApplicationProperties(DatabaseSO databaseSO, String path) {
		String p = path + "/../resources";
		String fileName = "application.properties";
		new File(p).mkdirs();
		String code = new ApplicationPropertiesGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter())
						.generate(databaseSO);
		try {
			Files.write(Paths.get(p + "/" + fileName), code.getBytes(), StandardOpenOption.CREATE_NEW);
			System.out.println(p + "/" + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public GUIBundle getGUIBundle() {
		return this.guiBundle;
	}

	@Override
	public ModelChecker[] getModelCheckers() {
		return new ModelChecker[0];
	}

	@Override
	public String getName() {
		return "REST Server Code Factory";
	}

	@Override
	public String[] getResourceBundleNames() {
		return new String[] { "archimedes" };
	}

	@Override
	public String getVersion() {
		return "0.0.1";
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
	public void setGUIBundle(GUIBundle guiBundle) {
		this.guiBundle = guiBundle;
	}

	@Override
	public void setModelCheckerMessageListFrameListeners(ModelCheckerMessageListFrameListener... listeners) {
		// ???
	}

}