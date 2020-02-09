package automation.core.driver;

/*
 * @author Luiz Felipe Alves de Sousa
 * @version 1.00
 * @since 10/01/2020
 * 
 * Interface padr�o e obrigat�ria para todas as classes respons�veis por criar driver web.
 * Esta interface � utilizada no gerenciamento para decidir qual tipo de driver deve ser criado.
*/

import org.openqa.selenium.WebDriver;

public interface BrowserInterface {

	/**
	 * M�todo respons�vel por obter um driver web capaz de realizar automa��es
	 * utilizando Selenium.
	 * 
	 * @return WebDriver - Driver web devidamente configurado e pronto para uso.
	 */
	public WebDriver getDriver();
}
