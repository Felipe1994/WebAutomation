package automation.core.base;

/*
 * @author Luiz Felipe Alves de Sousa
 * @version 1.00
 * @since 09/01/2020
 * 
 * Classe Abstrata que serve como base para outras classes do tipo Page.
 * Respons�vel por criar objetos que controlam p�ginas web,
 * atrav�s de m�todos de escritas, cliques, navega��o e manipula��o
 * de componentes Web.

*/

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import automation.core.driver.DriverManager;
import automation.core.utils.Utils;
import automation.logging.log4j.Log4JSetup;

public abstract class WebBasePage {

	private WebDriver driver;
	private JavascriptExecutor executor;
	private TakesScreenshot screenShot;
	private Actions actions;
	private WebDriverWait wait;
	private Logger log = Log4JSetup.getLogger(WebBasePage.class);

	/*
	 * *********************************
	 * 
	 * Construtores
	 * 
	 ***********************************/
	/**
	 * Construtor <b>padr�o</b> responsavel por criar uma inst�ncia WebBasePage com
	 * todos os seus atributos configurados, com base num driver pr�-definido no
	 * arquivo de propriedades (setup.properties).
	 * 
	 */
	public WebBasePage() {
		setDriver(DriverManager.driverBuilder());
	}

	/**
	 * Construtor que recebe um objeto WebDriver vindo de outras classes que fazem
	 * parte da hierarquia da classe WebBasePage. Com base neste driver todos os
	 * seus atributos ser�o configurados.
	 * 
	 * @param WebDriver - Driver externo de outra WebBasePage.
	 */
	public WebBasePage(WebDriver driver) {
		setDriver(driver);
	}

	/*
	 * *********************************
	 * 
	 * Getter And Setters
	 * 
	 ***********************************/
	/**
	 * Responsavel por retornar um objeto do tipo WebDriver.
	 * 
	 * @return WebDriver - Atributo driver.
	 */
	public WebDriver getDriver() {
		return driver;
	}

	/**
	 * Responsavel por realizar os setup de todos os atributos da WebBasePage, desde
	 * waits, executores java scripts e etc. Por padr�o com o tempo de espera
	 * definido em 30 segundos.
	 * 
	 * @param WebDriver - Driver devidamente criado e configurado.
	 */
	public void setDriver(WebDriver driver) {
		this.driver = driver;
		this.executor = (JavascriptExecutor) driver;
		this.actions = new Actions(driver);
		this.screenShot = (TakesScreenshot) driver;
		setWaitTime(30);
		log.info("Page est� configurada e pronta para uso.");
	}

	/**
	 * Responsavel por configurar o tempo de aguarde por objetos web.
	 * 
	 * @param int - Tempo em segundos.
	 */
	public void setWaitTime(int time) {
		this.wait = new WebDriverWait(driver, time);
		log.info("Ajustando timeout para o limite de  [ " + time + " segundos]");
	}

	/*
	 * *********************************
	 * 
	 * Busca por elementos Web
	 * 
	 ***********************************/
	/**
	 * Responsavel por buscar na p�gina Web um elemento com base no tipo do seletor
	 * CSS.
	 * 
	 * @param String - Padr�o de idenfica��o utilizando CSS ("#id .class tags...).
	 * @return WebElement - Elemento encontrado ou Exception de NullPointer ou
	 *         NoSuchElement.
	 */
	public WebElement find(String cssSelector) {
		return find(By.cssSelector(cssSelector));
	}

	/**
	 * Responsavel por buscar na p�gina Web um elemento com base no tipo do seletor
	 * By.
	 * 
	 * @param By - Padr�o de idenfica��o utilizando By (By.id(), By.xpath(),
	 *           By.className()).
	 * @return WebElement - Elemento encontrado ou Exception de NullPointer ou
	 *         NoSuchElement.
	 */
	private WebElement find(By element) {
		try {
			log.info("Realizando localiza��o do elemento web.");
			return driver.findElement(element);
		} catch (NoSuchElementException e) {
			log.error("N�o foi poss�vel localizar o elemento, verifique o parametro de identifica��o.");
			return null;
		}
	}

	/**
	 * Responsavel por buscar na p�gina Web um conjunto de elementos com base no
	 * tipo do seletor By.
	 * 
	 * @param By - Padr�o de idenfica��o utilizando By (By.id(), By.xpath(),
	 *           By.className()).
	 * @return List<WebElement> - Elementos encontrados ou Exception de NullPointer
	 *         ou NoSuchElement.
	 */
	public List<WebElement> findElements(By element) {
		try {
			log.info("Realizando localiza��o de v�rios elementos web.");
			return driver.findElements(element);
		} catch (NoSuchElementException e) {
			log.error("N�o foi poss�vel localizar os elemento, verifique o Selector informado.");
			return null;
		}
	}

	/**
	 * Responsavel por buscar na p�gina Web elementos do tipo ComboBox
	 * 
	 * @param By - Padr�o de idenfica��o utilizando By (By.id(), By.xpath(),
	 *           By.className()).
	 * @return Select - Objeto do tipo Select para manipula��o de combobox.
	 */
	private Select findComboBox(By element) {
		WebElement webElement = find(element);
		log.info("Encontrando combo box de op��es.");
		return new Select(webElement);
	}

	/*
	 * *********************************
	 * 
	 * Manipula��o de Janelas
	 * 
	 ***********************************/
	/**
	 * Respons�vel por alterar o foco de qual janela do browser deve ser controlada.
	 * 
	 * @param String - O t�tulo do nome da janela desejada.
	 */
	public void switchWindow(String window) {
		driver.switchTo().window(window);
		log.info("Alterando de Janela.");
	}

	/**
	 * Respons�vel por alterar o foco de qual janela do browser deve ser controlada.
	 * 
	 * @param int - Ind�ce da janela desejada (Iniciando a partir do 0).
	 */
	public void switchTab(int index) {
		driver.switchTo().window((String) driver.getWindowHandles().toArray()[index]);
		log.info("Alterando de Janela com base no in�dice [" + index + "].");
	}

	/**
	 * Respons�vel por atualizar a p�gina Web.
	 */
	public void refreshPage() {
		driver.navigate().refresh();
		log.info("Recarregando a p�gina.");
	}

	/**
	 * Respons�vel por verificar se o titulo da P�gina � o esperado.
	 * 
	 * @param BrowserTitle
	 */
	public void validateBrowserTitle(String BrowserTitle) {
		try {
			assertEquals(BrowserTitle, driver.getTitle());
			log.info("T�tulo do navegador validado com sucesso.");
		} catch (AssertionError e) {
			log.error("Falha ao validar o titulo do Browser");
			e.printStackTrace();
		}
	}

	/**
	 * Respons�vel por direcionar o driver para o Gerenciador para ser encerrado
	 * corretamente.
	 */
	public void exitBrowser() {
		takeScreenshot("Last Run " + Utils.getDate());
		DriverManager.finishDriver(getDriver());
	}

	/*
	 * *********************************
	 * 
	 * Elementos HTML n�o comuns
	 * 
	 ***********************************/
	public void moveSlider(By element) {
		WebElement slider = find(element);
		int sliderWidth = slider.getSize().getWidth();
		int xCoord = slider.getLocation().getX();
		actions.moveToElement(slider).click().dragAndDropBy(slider, xCoord + sliderWidth, 0).build().perform();
		log.info("Ajustando slider.");
	}

	/*
	 * *********************************
	 * 
	 * CSS Estilo e atributos de Elemento
	 * 
	 ***********************************/
	/**
	 * Respons�vel por adicionar ao elemento web identificado uma borda vermelha
	 * para visualizar durante a automa��o.
	 * 
	 * @param WebElement- O elemento web localizado.
	 */
	public void borderStyle(WebElement element) {
		if (element != null) {
			executor.executeScript("arguments[0].style.border = '2px solid red';", element);
		}
	}

	/**
	 * Respons�vel por obter os valores CSS de um determinado elemento web com base
	 * no atributo desejado.
	 * 
	 * @param By     - seletor do elemento desejado.
	 * @param String - codigo Css para informar o atributo que deseja resgatar o
	 *               valor.
	 * 
	 * @return String - O valor do atributo selecionado.
	 */
	public String getCssValue(By element, String elementCss) {
		log.info("Obtendo valor css do elemento web.");
		return find(element).getCssValue(elementCss);
	}

	/**
	 * Respons�vel por obter os valores CSS de um determinado elemento web com base
	 * no atributo value.
	 * 
	 * @param By - seletor do elemento desejado.
	 * 
	 * @return String - O valor do atributo value selecionado.
	 */
	public String getAttributeValue(By element) {
		return getAttributeValue(find(element));
	}

	/**
	 * Respons�vel por obter os valores CSS de um determinado elemento web com base
	 * no atributo value.
	 * 
	 * @param WebElement - elemento j� identificado.
	 * 
	 * @return String - O valor do atributo value selecionado.
	 */
	public String getAttributeValue(WebElement element) {
		log.info("Obtendo valor do elemento web.");
		return element.getAttribute("value");
	}

	/*
	 * *********************************
	 * 
	 * Valida��o de Elementos
	 * 
	 ***********************************/
	/**
	 * Respons�vel por validar a existencia do elemento web.
	 * 
	 * @param WebElement- O elemento web desejado.
	 */
	public void elementExists(WebElement element) {
		log.info("Validando existencia de um elemento web.");
		assertTrue(element.isDisplayed());
	}

	/**
	 * Respons�vel por validar a existencia do elemento web.
	 * 
	 * @param By - O seletor do tipo By do elemento web desejado.
	 */
	public void elementExists(By element) {
		elementExists(find(element));
	}

	/**
	 * Respons�vel por validar se o elemento web est� habilitado.
	 * 
	 * @param By - O seletor do tipo By do elemento web desejado.
	 * @return boolean;
	 */
	public boolean elementIsEnable(By element) {
		WebElement e = find(element);
		return elementIsEnable(e);
	}

	/**
	 * Respons�vel por validar se o elemento web est� habilitado.
	 * 
	 * @param WebElement - O elemento web desejado.
	 * @return boolean;
	 */
	public boolean elementIsEnable(WebElement element) {
		log.info("Verificando se elemento est� haabilitado.");
		return element.isEnabled();
	}

	/**
	 * Respons�vel por validar se o elemento web est� sleecionado.
	 * 
	 * @param WebElement - O elemento web desejado.
	 */
	public void elementIsSelected(WebElement element) {
		log.info("Verificando se elemento est� selecionado.");
		assertTrue(element.isSelected());
	}

	/*
	 * *********************************
	 * 
	 * Escrever e manipular conte�dos em componentes Web
	 * 
	 ***********************************/
	/**
	 * Respons�vel por escrever conte�dos em elemento web.
	 * 
	 * @param By     - O seletor do tipo By do elemento web desejado.
	 * @param String - O conte�do a ser escrito.
	 */
	public void type(By element, String text) {
		type(find(element), text);
	}

	/**
	 * Respons�vel por enviar teclas para um elemento web.
	 * 
	 * @param WebElement - O elemento web desejado.
	 * @param Keys       - Chaves do teclado a ser enviado (Tab, Enter, Esc,
	 *                   etc...).
	 */
	public void type(WebElement element, Keys keys) {
		log.info("Enviando conte�do para o componente web.");
		element.sendKeys(keys);
	}

	/**
	 * Respons�vel por escrever conte�dos em elementos web.
	 * 
	 * @param WebElement - O elemento web desejado.
	 * @param String     - O conte�do a ser escrito.
	 */
	public void type(WebElement element, String text) {
		borderStyle(element);
		elementExists(element);
		elementIsEnable(element);
		clear(element);
		element.sendKeys(text);
	}

	/**
	 * Respons�vel por remover conte�dos em elementos web.
	 * 
	 * @param WebElement - O elemento web desejado.
	 */
	public void clear(WebElement element) {
		log.info("Limpando o conte�do presente em um componente web.");
		element.clear();
	}

	/**
	 * Respons�vel por remover conte�dos em elementos web utilizando backspace.
	 * 
	 * @param WebElement - O elemento web desejado.
	 */
	public void clearValuesWithBackSpace(WebElement elemento) {
		log.info("Realizando limpeza dos valores de um elemento web");
		while (getAttributeValue(elemento).length() > 0) {
			elemento.sendKeys(Keys.BACK_SPACE);
		}
	}

	/**
	 * Respons�vel por obter o conte�do de texto em elementos web.
	 * 
	 * @param WebElement - O elemento web desejado.
	 * @return String - texto do elemento.
	 */
	public String getText(WebElement element) {
		log.info("Obtendo texto do elemento web.");
		return element.getText();
	}

	/**
	 * Respons�vel por obter o conte�do de texto em elementos web.
	 * 
	 * @param By - O seletor do tipo By do elemento web desejado.
	 * @return String - Texto do elemento.
	 */
	public String getText(By element) {
		return getText(find(element));
	}

	/**
	 * Respons�vel por validar o conte�do de texto em elementos web.
	 * 
	 * @param WebElement - O elemento web desejado.
	 * @param String     - O texto que deve ser comparado ao texto do elemento.
	 */
	public void validateText(WebElement element, String text) {
		try {
			assertEquals(element.getText().toLowerCase(), text.toLowerCase());
			log.info("O texto do elemento foi validado com sucesso. [" + text + "]");
		} catch (AssertionError e) {
			log.error("Falha ao validar o texto do elemento");
			e.printStackTrace();
		}
	}

	/*
	 * *********************************
	 * 
	 * Clicar e Interagir em componentes Web
	 * 
	 ***********************************/
	/**
	 * Respons�vel por clicar em elementos web com base no ind�ce informado.
	 * 
	 * @param By  - O seletor do tipo By do elemento web desejado.
	 * @param int - Indice do Elemento.
	 * 
	 */
	public void clickIndex(By element, int index) {
		List<WebElement> e1 = driver.findElements(element);
		WebElement e = e1.get(index);
		click(e);
	}

	/**
	 * Respons�vel por clicar em elementos ap�s validar se o elemento est�
	 * devidamente habilitado.
	 * 
	 * @param WebElement - O elemento web desejado.
	 */
	public WebElement click(WebElement element) {
		elementIsEnable(element);
		scrollToElement(element);
		waitToBeClickable(element);
		borderStyle(element);
		element.click();
		return element;
	}

	/**
	 * Respons�vel por clicar em <b>todos</b> os elementos web dentro de uma lista.
	 * 
	 * @param List<WebElement> - Lista de elementos web desejados.
	 */
	public void clickElements(List<WebElement> elements) {
		for (WebElement e : elements) {
			log.info("Realizando cliques em uma lista de diversos elementos.");
			borderStyle(e);
			click(e);
		}
	}

	/**
	 * Respons�vel por enviar um submit num componente web.
	 * 
	 * @param By - O seletor do tipo By do elemento web desejado
	 */
	public void submit(By element) {
		WebElement e = find(element);
		e.submit();
		log.info("Realizando um submit no elemento [" + getText(e) + "].");
	}

	/*
	 * *********************************
	 * 
	 * URLs e Navega��o
	 * 
	 ***********************************/
	/**
	 * Respons�vel por direcionar o driver web para um endere�o URL v�lido.
	 * 
	 * @param String - A URL de destino de navaga��o.
	 */
	public void openUrl(String url) {
		driver.get(url);
		log.info("Navegando para URL [" + url + "]");
	}

	/**
	 * Respons�vel por direcionar o driver web para um endere�o URL v�lido.
	 * 
	 * @param String - A URL de destino de navaga��o.
	 */
	public void navigateTo(String url) {
		driver.navigate().to(url);
		log.info("Navegando para URL [" + url + "]");
	}

	/**
	 * Respons�vel por validar se a URL atual do browser � a esperada.
	 * 
	 * @param String - A URL que deve ser validada.
	 */
	public void validateCurrentUrl(String url) {
		try {
			assertEquals(url, getCurrentUrl());
			log.info("URL atual validada com sucesso.");
		} catch (AssertionError e) {
			log.error("Falha ao validar a URL atual do Browser.");
			e.printStackTrace();
		}
	}

	/**
	 * Respons�vel por obter a URL atual do browser.
	 * 
	 * @return String - A URL atual.
	 */
	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	/*
	 * ***********************************
	 * 
	 * Manipula��o de Combobox
	 * 
	 ***********************************/
	/**
	 * Respons�vel por clicar num valor espec�fico de um combo box.
	 * 
	 * @param By     - O seletor do tipo By do combobox desejado.
	 * @param String - Valor desejado para ser clicado.
	 */
	public void selectComboBox(By element, String value) throws NoSuchElementException {
		Select combo = findComboBox(element);
		try {
			combo.selectByValue(value);
			log.info("Escolhendo o valor [" + value + "] no elemento combo  box.");
		} catch (Exception e) {
			combo.selectByIndex(Integer.parseInt(value));
			log.info("Escolhendo o valor [" + value + "] no elemento combo  box.");
		}
	}

	/**
	 * Respons�vel por obter o valor do primeiro valor de um combobox.
	 * 
	 * @param By - O seletor do tipo By do combobox desejado.
	 * @return String - Valor do primeiro campo de um combobox.
	 */
	public String getTextFromFirstComboPosition(By comboBox) {
		Select combo = findComboBox(comboBox);
		log.info("Obtendo primeito texto do combo box.");
		return combo.getFirstSelectedOption().getText();
	}

	/**
	 * Respons�vel por obter o tamanho de op��es de um combobox.
	 * 
	 * @param By - O seletor do tipo By do combobox desejado.
	 * @return Integer - Tamanho de um combobox.
	 */
	public Integer getComboBoxOptionsSize(By comboBox) {
		Select cb = findComboBox(comboBox);
		List<WebElement> options = cb.getOptions();
		log.info("Obtendo tamanho de op��es existentes no combo box.");
		return options.size();
	}

	/**
	 * Respons�vel por verificar se uma op��o est� selecionada dentro do combobox.
	 * 
	 * @param By     - O seletor do tipo By do combobox desejado.
	 * @param String - Texto que deseja verificar se est� selecionado.
	 */
	public void checkIfSelectHasOption(By element, String text) {
		boolean result = false;
		Select comboBox = findComboBox(element);
		List<WebElement> options = comboBox.getOptions();
		for (WebElement option : options) {
			if (option.getText().equals(text)) {
				result = true;
				break;
			}
		}
		assertTrue(result);
		log.info("Validando que a opcao selecionada existe no combo box [" + text + "].");
	}

	/**
	 * Respons�vel por remover a sele��o de uma op��o dentro do combobox.
	 * 
	 * @param By     - O seletor do tipo By do combobox desejado.
	 * @param String - Texto que deseja verificar se est� selecionado.
	 */
	public void deselectByVisibleText(By element, String valor) {
		Select combo = findComboBox(element);
		combo.deselectByVisibleText(valor);
		log.info("Removendo Sele��o do combo box com base no texto informado [" + valor + "].");
	}

	/**
	 * Respons�vel por obter os textos das op��es de um combobox.
	 * 
	 * @param By - O seletor do tipo By do combobox desejado.
	 * @return List<String> - Conjunto dos textos existentes no combobox.
	 */
	public List<String> getComboTexts(By element) {
		List<String> listOfTexts = new ArrayList<String>();
		Select combo = findComboBox(element);
		List<WebElement> options = combo.getOptions();
		log.info("Obtendo as op��es dos combos de Textos.");
		for (WebElement e : options) {
			log.info("Op��o: [" + e.getText() + "].");
			listOfTexts.add(e.getText());
		}
		return listOfTexts;
	}

	/**
	 * Respons�vel por obter todas as op��es selecionadas de um combobox.
	 * 
	 * @param By - O seletor do tipo By do combobox desejado.
	 * @return List<String> - Conjunto dos textos existentes no combobox.
	 */
	public List<String> getAllSelectedOptions(By element) {
		Select combo = findComboBox(element);
		List<WebElement> allSelectedOptions = combo.getAllSelectedOptions();
		List<String> values = new ArrayList<String>();
		for (WebElement options : allSelectedOptions) {
			log.info("Op��o: [" + options.getText() + "].");
			values.add(options.getText());
		}
		log.info("Obtendo todas as op��es selecionadas.");
		return values;

	}

	/*
	 * ***********************************
	 * 
	 * Frames
	 * 
	 ***********************************/
	/**
	 * Respons�vel por focalizar num web frame.
	 * 
	 * @param WebElement - O elemento web frame desejado ap�s localizado.
	 */
	public void FrameIn(WebElement element) {
		FrameOut();
		Frame(element);
	}

	/**
	 * Respons�vel por focalizar num web frame.
	 * 
	 * @param WebElement - O elemento web frame desejado ap�s localizado.
	 */
	private void Frame(WebElement element) {
		driver.switchTo().frame(element);
		log.info("Focalizando num elemento do tipo iFrame.");
	}

	/**
	 * Respons�vel por desfocalizar num web frame.
	 * 
	 * @param WebElement - O elemento web frame desejado ap�s localizado.
	 */
	public void FrameOut() {
		driver.switchTo().defaultContent();
		log.info("Desfocalizando do conte�do do iFrame Atual.");
	}

	/*
	 * ***********************************
	 * 
	 * Alertas e PopUps
	 * 
	 ***********************************/
	/**
	 * Respons�vel por obter o texto presente num pop up de alerta.
	 */
	public String getAlertText() {
		Alert alert = driver.switchTo().alert();
		log.info("Obtendo texto do pop up alerta que est� presente na tela");
		return alert.getText();
	}

	/**
	 * Respons�vel por apertar o bot�o de ok ou confirmar num pop up de alerta.
	 */
	public void acceptAlert() {
		Alert alert = driver.switchTo().alert();
		log.info("Clicando na op��o de aceitar o pop up alerta que est� presente na tela.");
		alert.accept();
	}

	/**
	 * Respons�vel por apertar o bot�o de cancelar ou recusar num pop up de alerta.
	 */
	public void dismissAlert() {
		Alert alert = driver.switchTo().alert();
		log.info("Clicando na op��o de cancelar o pop up alerta que est� presente na tela.");
		alert.dismiss();
	}

	/**
	 * Respons�vel por escrever num pop up de alerta.
	 * 
	 * @param String - Conte�do a ser enviado no Alerta
	 */
	public void WriteOnAlert(String content) {
		Alert alert = driver.switchTo().alert();
		log.info("Enviando texto para o pop up alerta que est� presente na tela [" + content + "].");
		alert.sendKeys(content);
	}

	/*
	 * ***********************************
	 * 
	 * Esperas
	 * 
	 ***********************************/
	/**
	 * Respons�vel por aguardar a presen�a de um elemento na p�gina web, por�m n�o
	 * significa que o elemento esteja v�sivel na tela.
	 * 
	 * @param By - O seletor do tipo By do elemento desejado.
	 * @return WebElement - O elemento desejado.
	 */
	public WebElement waitPresenceOfElement(By element) {
		try {
			return wait.until(ExpectedConditions.presenceOfElementLocated(element));
		} catch (TimeoutException e) {
			log.error("Tempo de espera para detectar a presen��a do elemento foi excedido");
			return null;
		}
	}

	/**
	 * Respons�vel por aguardar a presen�a de um texto espec�fico dentro de elemento
	 * na p�gina web.
	 * 
	 * @param String     - Conte�do a ser enviado no Alerta
	 * @param WebElement - O elemento desejado.
	 * @return Boolean.
	 */
	public Boolean waitTextToBePresent(WebElement element, String text) {
		try {
			return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
		} catch (TimeoutException e) {
			log.error("Tempo de espera para detectar a presen�a do texto [" + text + "] foi excedido.");
			return null;
		}
	}

	/**
	 * Respons�vel por aguardar se o elemento est� habilitado a receber eventos de
	 * clique.
	 * 
	 * @param WebElement - O elemento desejado.
	 * @return WebElement - O elemento desejado.
	 */
	public WebElement waitToBeClickable(WebElement element) {
		try {
			log.info("Aguardando elemento habilitar para receber cliques.");
			return wait.until(ExpectedConditions.elementToBeClickable(element));
		} catch (TimeoutException e) {
			log.error("Tempo de espera para clicar no elemento foi excedido.");
			return null;
		}
	}

	/**
	 * Respons�vel por aguardar o elemento desejado ficar habilitado.
	 * 
	 * @param WebElement - O elemento desejado.
	 */
	public void waitElementToBeEnable(WebElement element) {
		int timeout = 0;
		while (timeout < 5000) {
			try {
				log.info("Aguardando elemento habilitar...");
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			timeout += 250;
			if (element.isEnabled()) {
				log.info("Elemento est� habilitado.");
				break;
			}
		}
	}

	/**
	 * Respons�vel por aguardar por um elemento web ser selecionado.
	 * 
	 * @param element
	 */
	public void waitElementToBeSelected(WebElement element) {
		try {
			wait.until(ExpectedConditions.elementToBeSelected(element));
		} catch (TimeoutException e) {
			log.error("Tempo de espera para o elemento ser selecionado foi excedido.");
			e.printStackTrace();
		}
	}

	/**
	 * Respons�vel por aguardar a URL do browser carregar para a URL informada.
	 * 
	 * @param url - A URL desejada que o browser deva direcionar.
	 */
	public void waitUrlToBe(String url) {
		try {
			wait.until(ExpectedConditions.urlToBe(url));
		} catch (TimeoutException e) {
			log.error("Tempo de espera para carregamento da URL foi excedido.");
			e.printStackTrace();
		}
	}

	/**
	 * Respons�vel por aguardar que um elemento esteja presente, por�m n�o v�sivel
	 * na tela.
	 * 
	 * @param element - elemento web desejado.
	 */
	public void waitInvisibilityOf(By element) {
		try {
			wait.until(ExpectedConditions.invisibilityOf(find(element)));
		} catch (TimeoutException e) {
			log.error("Tempo de espera para o elemento desaparecer foi excedido.");
			e.printStackTrace();
		}
	}

	/**
	 * Respons�vel por aguardar um elemento Web existente ficar vis�vel na tela do
	 * browser.
	 * 
	 * @param By - O seletor do tipo By do elemento desejado.
	 * @return WebElement - Retorna o elemento que se tornou vis�vel.
	 */
	public WebElement waitVisibilityOfElement(By elemento) {
		try {
			return wait.until(ExpectedConditions.visibilityOf(find(elemento)));
		} catch (TimeoutException e) {
			log.error("Tempo de espera para o elemento estar vis�vel foi excedido.");
			return null;
		}
	}

	/**
	 * Respons�vel por aguardar um elemento Web existente ficar vis�vel na tela do
	 * browser.
	 * 
	 * @param WebElement - O elemento desejado.
	 * @return WebElement - Retorna o elemento que se tornou vis�vel.
	 */
	public WebElement waitVisibilityOfElement(WebElement elemento) {
		try {
			return wait.until(ExpectedConditions.visibilityOf(elemento));
		} catch (TimeoutException e) {
			log.error("Tempo de espera para o elemento estar vis�vel foi excedido.");
			return null;
		}
	}

	/*
	 * ***********************************
	 * 
	 * Execu��o de Scripts JS
	 * 
	 ***********************************/
	/**
	 * Respons�vel por realizar um clique num objeto web atrav�s de scripts Js.
	 * 
	 * @param element - O seletor do tipo By do elemento desejado
	 */
	public void click(By element) {

		try {
			WebElement e = find(element);
			borderStyle(e);
			executor.executeScript("arguments[0].click();", e);
			log.info("Clicando num componente usando script JS.");
		} catch (NoSuchElementException e) {
			log.error("Falha ao tentar clicar usando script JS.");
		}
	}

	/**
	 * Resposns�vel por realizar um clique num objeto web, utilizando como base um
	 * texto, atrav�s de um scripts Js.
	 * 
	 * @param list - Lista de Web Elementos que potencialmente possuem o texto alvo.
	 * @param text - Texto que serve como parametro para identifica��o do elemento
	 *             que ser� clicado.
	 */
	public void clickByText(List<WebElement> list, String text) {
		try {
			for (WebElement child : list) {
				scrollToElement(child);
				if (child.getText().contains(text)) {
					borderStyle(child);
					child.click();
					break;
				}
			}
		} catch (NoSuchElementException e) {
			log.error("Falha ao tentar clicar por Texto no elemento [" + text + "].");
		}
		log.info("Clicando num componente da p�gina que cont�m o texto [" + text + "].");
	}

	/**
	 * Respons�vel por realizar um clique num objeto web, utilizando como base um
	 * texto, atrav�s de um scripts Js. Esta fun��o conta com o parametro de
	 * informar um web elemento pai, para localizar dentro dele uma lista de
	 * potenciais elementos que possam conter o texto alvo de clique.
	 * 
	 * @param elementoPai - Componente Web que armazena os poss�veis componentes que
	 *                    contenham o texto especificado.
	 * @param ElementTag  - Tags dos elementos filhos que ser�o agrupados em lista
	 *                    para verificar seus textos.
	 * @param text        - Que ser� utilizado como validador para determinar se o
	 *                    clique ser� executado.
	 */
	public void clickByText(By element, String ElementTag, String text) {
		WebElement e = find(element);
		List<WebElement> listOfChildElements = e.findElements(By.tagName(ElementTag));
		clickByText(listOfChildElements, text);
	}

	/**
	 * Respons�vel por realizar um clique num objeto web, utilizando como base um
	 * texto, atrav�s de um scripts Js. Esta fun��o agrupa componentes web com a
	 * mesma Tag.
	 * 
	 * @param ElementTag - Tag do elemento alvo para receber o evento de clique.
	 * @param text       - Que ser� utilizado como validado para determinar se o
	 *                   clique ser� executado.
	 */
	public void clickByText(String ElementTag, String text) {
		List<WebElement> listOfChildElements = findElements(By.tagName(ElementTag));
		clickByText(listOfChildElements, text);
	}

	/**
	 * Respons�vel por realizar uma rolagem na tela de acordo com a quantidade de
	 * pixels
	 * 
	 * @param amount - Quantidade em pixels do espa�o que a rolagem de tela ir�
	 *               ocorrer.
	 */
	public void scroll(int amount) {
		executor.executeScript("window.scrollBy(0, " + amount + ")");
		log.info("Scroll p�gina...");
	}

	/**
	 * Respons�vel por modificar o nome de um componente web do tipo iFrame.
	 * 
	 * @param frame - componente iframe identificado.
	 * @param name  - Valor que quer atualizar no atributo name no componente frame.
	 */
	public void setName(WebElement frame, String name) {
		executor.executeScript("arguments[0].setAttribute('name'," + name + ");", frame);
		log.info("Alterando o atributo name de um WebElement.");
	}

	/**
	 * Respons�vel por executar qualquer script na aplica��o Web.
	 * 
	 * @param script
	 */
	public void jsScript(String script) {
		executor.executeScript(script);
		log.info("Executando Script JS [" + script + "].");
	}

	/**
	 * Obt�m o valor do atributo value de um componente web atrav�s de Js.
	 * 
	 * @param elementId - Identificador do componente web desejado para obter o
	 *                  valor.
	 * @return String - Texto do atributo value.
	 */
	public String getCssValue(String elementId) {
		String value = (String) executor
				.executeScript("" + "if (document.getElementById('" + elementId + "').style.display == 'none'){   }");
		log.info("Obtendo CSS Value do elemento: [" + elementId + "].");
		return value;
	}

	/**
	 * Respons�vel por realizar rolagem na tela para que o elemento web desejado
	 * esteja vis�vel na tela.
	 * 
	 * @param element - O seletor do tipo By do componente web desejado.
	 */
	public void scrollToElement(By element) {
		scrollToElement(find(element));
	}

	/**
	 * Respons�vel por realizar rolagem na tela para que o elemento wweb desejado
	 * esteja vis�vel na tela.
	 * 
	 * @param element - Componente Web desejado j� identificado.
	 */
	public void scrollToElement(WebElement element) {
		try {
			executor.executeScript("arguments[0].scrollIntoView(true);", element);
			log.info("Scroll at� o componente [" + element.getText() + "].");
		} catch (Exception e) {
			log.error("Falha ao rolar at� o elemento solicitado.");
			e.printStackTrace();
		}
	}

	/**
	 * Respons�vel por escrever, utilizando script Js, em determinado componente
	 * web.
	 * 
	 * @param element - O seletor do tipo By do elemento web desejado.
	 * @param text    - Script JS que ser� executado naquele componente Web.
	 */
	public void typeScript(By element, String text) {
		WebElement e = find(element);
		executor.executeScript("arguments[0].value=" + text + ";", e);
		log.info("Escrevendo no component o texto [" + text + "].");
	}

	/**
	 * Respons�vel por executar um script Js em determinado componente web.
	 * 
	 * @param frame  - O seletor do tipo By do elemento web desejado.
	 * @param script - Script JS que ser� executado naquele componente Web.
	 */
	public void ExecuteScript(String script, WebElement frame) {
		executor.executeScript(script, frame);
		log.info("Executando Script JS [" + script + "].");
	}

	/**
	 * Respons�vel por executar um script Js onde a tela web desce at� o final da
	 * p�gina.
	 */
	public void scrollPageDown() {
		try {
			executor.executeScript("window.scrollTo(0,document.body.scrollHeight)");
			Thread.sleep(2000);
			log.info("Scroll page down...");
		} catch (Exception e) {
			log.error("Falha ao tentar Scrollar a p�gina.");
			e.printStackTrace();
		}
	}

	/*
	 * ***********************************
	 * 
	 * Screenshot
	 * 
	 ***********************************/

	/**
	 * Respons�vel por tirar um print da tela da execu��o do teste automatizado.
	 */
	public void takeScreenshot(String screenshotName) {
		if (!screenshotName.toLowerCase().contains(".png")) {
			screenshotName = String.format("%s.png", screenshotName);
		}

		String screenshotPath = String.format("%s./%s", Utils.getProp("file.screenshot"), screenshotName);

		try {
			File screenshotAs = screenShot.getScreenshotAs(OutputType.FILE);
			File destFile = new File(screenshotPath);
			FileUtils.copyFile(screenshotAs, destFile);
			log.info("Screenshot salvo com sucesso.");
		} catch (IOException e) {
			log.info(String.format(
					"Falha ao capturar evid�ncia, verifique se o nome do arquivo est� correto. Arquivo [%s].",
					screenshotName));
			e.printStackTrace();
		}
	}

	/**
	 * Respons�vel por retornar os bytes de um screenshot tirado durante a execu��o
	 * dos testes. PAra anexar no relat�rio do cucumber.
	 * 
	 * @return Bytes para enriquecer o relat�rio do cucumber
	 */
	public byte[] takeScreenshot() {
		return screenShot.getScreenshotAs(OutputType.BYTES);
	}
}
