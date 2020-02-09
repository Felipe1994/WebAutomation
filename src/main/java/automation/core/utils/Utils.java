package automation.core.utils;

/*
 * @author Luiz Felipe Alves de Sousa
 * @version 1.00
 * @since 09/01/2020
 * 
 * Classe Est�tica que possui a responsabilidade de acessar as properties
 * tratar repositorios e seus endere�os, manipular e ler arquivos
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import automation.logging.log4j.Log4JSetup;

public class Utils {

	private static Properties props;
	private static Logger log = Log4JSetup.getLogger(Utils.class);

	// ******************************
	// Properties
	// ******************************

	/**
	 * M�todo p�blico para retorno das properties do arquivo setup.properties
	 * 
	 * @param String - nome da propriedade desejada.
	 * @return String - Valor da Property
	 */
	public static String getProp(String prop) {
		props = getProp();

		return props.getProperty(prop);
	}

	/**
	 * M�todo privado que obt�m um objeto do tipo Property v�lido.
	 * 
	 * @return Properties - Inst�ncia de um Objeto Property
	 */
	private static Properties getProp() {
		if (props == null) {
			props = new Properties();
			try {
				props.load(new FileInputStream(".\\src\\main\\resources\\setup.properties"));
			} catch (IOException e) {
				log.error("Falha ao obter as Properties de Configura��o, verifique se o arquivo existe.");
				e.printStackTrace();
			}
		}
		return props;
	}

	// ******************************
	// Reposit�rios e Endere�os
	// ******************************

	/**
	 * M�todo p�blico que retorna o endere�o da pasta raiz dos drivers web de acordo
	 * com o S.O.
	 * 
	 * @return String - Endere�o da Pasta de raiz dos drivers de acordo com o S.O.
	 */
	public static String binOSPath() {

		String binOSPath = Utils.getProp("browser.path.root");

		CharSequence oldChar = "OS_SYSTEM";
		CharSequence newChar = "windows";

		if (System.getProperty("os.name").toLowerCase().contains("linux")) {
			newChar = "linux";
		} else {
			props.setProperty("browser.mode.extension", ".exe");
		}

		return binOSPath.replace(oldChar, newChar);
	}

	/**
	 * M�todo p�blico que retorna o endere�o da pasta onde os downloads, feitos
	 * durante a automa��o web, ser�o armazenados.
	 * 
	 * @return String - Endere�o da Pasta de Downloads de acordo com o S.O.
	 */
	public static String binDownloadFolderPath() {

		String binOSPath = Utils.getProp("browser.path.root");

		CharSequence oldChar = "OS_SYSTEM";
		CharSequence newChar = "windows";

		if (System.getProperty("os.name").toLowerCase().contains("linux")) {
			newChar = "linux";
		}

		return binOSPath.replace(oldChar, newChar) + "downloads";
	}

	/**
	 * M�todo p�blico que retorna o endere�o de localiza��o de um driver web.
	 * 
	 * @param String - Nome do Driver Web que se quer obter o endere�o de
	 *               localiza��o.
	 * @return String - Endere�o do driver web de acordo com o S.O.
	 */
	public static String binDriverPath(String browser) {
		String root = Utils.binOSPath();
		return root + Utils.getProp("browser.path." + browser) + Utils.getProp("browser.mode.extension");
	}

	// ******************************
	// Arquivos
	// ******************************

	/**
	 * M�todo p�blico que l� um arquivo txt, que cont�m queries espec�ficas de banco
	 * de dados para executar numa rotina espec�fica.
	 * 
	 * @return BufferedReader - Buffer contendo todas as queries escritas no arquivo
	 *         txt, para executar no bando de dados.
	 */
	public static BufferedReader getDataBaseQueriesFromTextFile() {
		try {
			String dataBaseTXT = getProp("file.db.text.path") + getProp("file.db.text");
			File file = new File(dataBaseTXT);
			FileReader fr = new FileReader(file);
			BufferedReader queries = new BufferedReader(fr);
			return queries;
		} catch (FileNotFoundException e) {
			log.error("N�o foi poss�vel ler o arquivo do banco de dados.");
			e.printStackTrace();
			return null;
		}

	}

	/*
	 * ***********************************
	 * 
	 * 
	 * 
	 * ***** Tratamento de Strings *******
	 * 
	 * 
	 * 
	 ***********************************/
	/**
	 * Respons�vel se um determinado texto � valido de acordo com o padr�o RegEx
	 * informado.
	 * 
	 * @param content
	 * @param regEx
	 * @return boolean - resultado da valida��o
	 */
	public static Boolean FindInStrRegEx(String content, String regEx) {
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(content);
		boolean result = matcher.find();
		return result;
	}

	/**
	 * Respons�vel por retornar o a data atual no padr�o definido
	 * 
	 * @param pattern - Padr�o dd_MM_yyyy
	 * @return String - com a data formatada.
	 */
	public static String getCurrentDateTime(String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		formatter.setTimeZone(TimeZone.getTimeZone("GMT-3:00"));
		Date date = new Date();
		return formatter.format(date);
	}

	/**
	 * Respons�vel por formatar a data atual num padr�o amig�vel.
	 * 
	 * @return Retorna a data no formato: dd-MM-yyyy HH_mm_ss
	 */
	public static String getDate() {
		return getCurrentDateTime("dd-MM-yyyy HH_mm_ss");
	}
}
