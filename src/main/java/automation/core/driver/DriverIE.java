package automation.core.driver;

/*
 * @author Luiz Felipe Alves de Sousa
 * @version 1.00
 * @since 10/01/2020
 * 
 * Classe respons�vel por configurar e gerar instancias de drivers
 * respons�veis por acessar o navegador Internet Explorer.
*/

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;

import automation.core.utils.Utils;
import automation.logging.log4j.Log4JSetup;

public class DriverIE implements BrowserInterface {

	private InternetExplorerDriver driver;
	private Logger log = Log4JSetup.getLogger(DriverIE.class);

	// ******************************
	// Construtor
	// ******************************

	/**
	 * Responsavel por criar e configurar corretamente o objeto do tipo
	 * IEDriver.
	 */
	public DriverIE() {

		System.setProperty("webdriver.ie.driver", Utils.binDriverPath("ie"));
		InternetExplorerOptions capability = setupBrowser();
		driver = new InternetExplorerDriver(capability);

		log.info("Internet Explorer iniciado com sucesso.");
	}

	// ******************************
	// Configura��o
	// ******************************

	/**
	 * M�todo privado respons�vel pelo retorno das configura��es do browser para
	 * determinar comportamentos, diret�rios de download, modos de execu��o e etc.
	 * 
	 * @return InternetExplorerOptions - Conjunto de configura��es do browser.
	 */
	private static InternetExplorerOptions setupBrowser() {
		String site = Utils.getProp("env.app." + Utils.getProp("env.app"));

		InternetExplorerOptions capability = new InternetExplorerOptions();
		capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		capability.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
		capability.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, site);

		return capability;
	}

	/**
	 * Retorno do objeto Driver devidamente configurado
	 */
	public WebDriver getDriver() {
		return driver;
	}
}
