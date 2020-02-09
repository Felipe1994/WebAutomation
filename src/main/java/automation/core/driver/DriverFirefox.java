package automation.core.driver;

/*
 * @author Luiz Felipe Alves de Sousa
 * @version 1.00
 * @since 10/01/2020
 * 
 * Classe respons�vel por configurar e gerar instancias de drivers
 * respons�veis por acessar o navegador Firefox.
*/

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import automation.core.utils.Utils;
import automation.logging.log4j.Log4JSetup;

public class DriverFirefox implements BrowserInterface {

	private FirefoxDriver driver;
	private Logger log = Log4JSetup.getLogger(DriverFirefox.class);

	// ******************************
	// Construtor
	// ******************************

	/**
	 * Responsavel por criar e configurar corretamente o objeto do tipo
	 * FirefoxDriver.
	 */
	public DriverFirefox() {

		System.setProperty("webdriver.gecko.driver", Utils.binDriverPath("firefox"));
		System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");

		FirefoxOptions firefoxOptions = setupBrowser();

		driver = new FirefoxDriver(firefoxOptions);
		log.info("Firefox iniciado com sucesso.");
	}

	// ******************************
	// Configura��o
	// ******************************

	/**
	 * M�todo privado respons�vel pelo retorno das configura��es do browser para
	 * determinar comportamentos, diret�rios de download, modos de execu��o e etc.
	 * 
	 * @return FirefoxOptions - Conjunto de configura��es do browser.
	 */
	private FirefoxOptions setupBrowser() {

		String downloadFolderPath = Utils.binDownloadFolderPath();
		boolean headless = !Utils.getProp("browser.mode.headless").isEmpty();

		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("browser.download.folderList", 2);
		profile.setPreference("browser.download.dir", downloadFolderPath);
		profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
		profile.setPreference("browser.download.manager.closeWhenDone", true);
		profile.setPreference("browser.download.manager.focusWhenStarting", false);
		profile.setPreference("browser.download.manager.showWhenStarting", false);
		profile.setPreference("browser.download.manager.showAlertOnComplete", false);
		profile.setPreference("browser.download.manager.useWindow", false);
		profile.setPreference("browser.helperApps.alwaysAsk.force", false);
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
				"application/msword, application/csv, application/ris, text/csv, image/png, "
						+ "application/pdf, text/html, text/plain, application/zip, application/x-zip, "
						+ "application/x-zip-compressed, application/download, application/octet-stream");
		profile.setPreference("services.sync.prefs.sync.browser.download.manager.showWhenStarting", false);
		profile.setPreference("pdfjs.disabled", true);

		// Configura��es das op��es do perfil para inserir no navegador
		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.setProfile(profile);
		firefoxOptions.setHeadless(headless);

		return firefoxOptions;
	}

	/**
	 * Retorno do objeto Driver devidamente configurado
	 */
	public WebDriver getDriver() {
		return driver;
	}

}
