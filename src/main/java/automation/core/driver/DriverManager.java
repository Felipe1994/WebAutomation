package automation.core.driver;

/*
 * @author Luiz Felipe Alves de Sousa
 * @version 1.00
 * @since 10/01/2020
 * 
 * Classe Est�tica que possui a responsabilidade de criar qualquer driver especificado no arquivo de propriedades.
 * Sem a necessidade de ser reescrita no caso da inclus�o de novas classes respons�veis por criar drivers.
*/

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import automation.core.utils.Utils;
import automation.logging.log4j.Log4JSetup;

public class DriverManager {

	private static Logger log = Log4JSetup.getLogger(DriverManager.class);

	// ******************************
	// Builder
	// ******************************

	/**
	 * M�todo p�blico e est�tico capaz de decidir qual driver deve ser criado com
	 * base no setup de propriedades.
	 * 
	 * @return WebDriver - Objeto do tipo WebDriver devidamente configurado e pronto
	 *         para uso.
	 */
	public static WebDriver driverBuilder() {
		String browser = Utils.getProp("browser.name");
		String browserClass = Utils.getProp(String.format("browser.option.%s", browser));
		String browseRootFolder = "automation.core.driver.";

		BrowserInterface driver = null;

		try {
			Class<?> forName = Class.forName(browseRootFolder + browserClass);
			driver = (BrowserInterface) forName.newInstance();
			log.info("Driver criado com sucesso.");
		} catch (ClassNotFoundException e) {
			log.error("Verifique no properties se as op��es de Browser est�o corretas.");
			e.printStackTrace();
		} catch (InstantiationException e) {
			log.error("Falha ao instanciar a classe do driver escolhido. [" + browser + "]");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			log.error("Verifique no properties se as op��es de Browser est�o corretas.");
			e.printStackTrace();
		}

		WebDriver webDriver = driver.getDriver();
		if (Utils.getProp("browser.mode.headless").isEmpty()) {
			webDriver.manage().window().maximize();
			log.info("TAMANHO DA TELA " + webDriver.manage().window().getSize());
		}

		if (!(webDriver instanceof DriverIE)) {
			String url = Utils.getProp("env.app." + Utils.getProp("env.app"));
			webDriver.get(url);
		}

		return webDriver;
	}

	/**
	 * Respons�vel por encerrar corretamente a sess�o do driver
	 * 
	 * @param driver
	 */
	public static void finishDriver(WebDriver driver) {
		try {
			driver.quit();
			log.info("Encerrando sess�o do Driver.");
		} catch (Exception e) {
			log.error("N�o foi poss�vel encerrar o Driver");
		}

	}

	// ******************************
	// Demonstra��o
	// ******************************
	public static void main(String[] args) {
		WebDriver driver = driverBuilder();
		finishDriver(driver);
	}

}
