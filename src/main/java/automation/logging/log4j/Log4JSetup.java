package automation.logging.log4j;

/*
 * @author Luiz Felipe Alves de Sousa
 * @version 1.00
 * @since 10/01/2020
 * 
 * Classe est�tica respons�vel por realizar criar um objeto capaz de realizar os logs
 * dos eventos ocorridos durante o desenvolvimento.
*/

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import automation.core.utils.Utils;

public class Log4JSetup {

	private static Logger logger;
	private static final String RESOURCES = "src/main/resources";

	// ******************************
	// Getters
	// ******************************

	/**
	 * Responsavel por criar um objeto do tipo Logger com base de um objeto
	 * recebido, para poder rastrear de qual classe ser� a origem dos logs gerados.
	 * 
	 * @param Object - Qualquer inst�ncia de objeto.
	 * @return Logger - Instancia do tipo Logger apta a registrar os eventos a
	 *         partir do n�vel de Info.
	 */
	public static Logger getLogger(Object object) {
		return getLogger(object.getClass());
	}

	/**
	 * Responsavel por criar um objeto do tipo Logger com base em uma classe
	 * informada rastreando sua origem para gerar determinar como os logs ser�o
	 * gerados.
	 * 
	 * @param Class - Qualquer classe dispon�vel.
	 * @return Logger - Instancia do tipo Logger apta a registrar os eventos a
	 *         partir do n�vel de Info.
	 */
	public static Logger getLogger(Class<? extends Object> classTarget) {
		System.setProperty("fName", Utils.getCurrentDateTime("dd_MM_yyyy[HH-mm]"));
		logger = Logger.getLogger(classTarget);
		String log4jConfigFile = System.getProperty("user.dir") + File.separator + RESOURCES + File.separator
				+ "log4j.properties";
		PropertyConfigurator.configure(log4jConfigFile);

		return logger;
	}
}
