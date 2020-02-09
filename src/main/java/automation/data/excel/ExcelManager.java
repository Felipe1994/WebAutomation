package automation.data.excel;

/*
 * @author Luiz Felipe Alves de Sousa
 * @version 1.00
 * @since 09/01/2020
 * 
 * Classe Respons�vel por Gerenciar arquivos de planilhas Excel
*/

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import automation.core.utils.Utils;
import automation.logging.log4j.Log4JSetup;

public class ExcelManager {

	private Logger log = Log4JSetup.getLogger(ExcelManager.class);

	private Workbook workbook;
	private FileInputStream inputstream;

	private int index;
	private String fileName;

	// ******************************
	// Construtores
	// ******************************

	/**
	 * Construtor com nome do arquivo alter�vel.
	 * 
	 * @param String - Nome do arquivo excel desejado com a extens�o correta XLS ou
	 *               XLSX.
	 */
	public ExcelManager(String fileName) {
		setFileName(fileName);
		defaultIndex();
	}

	/**
	 * Construtor com o n�mero do ind�ce da aba da planilha selecionada alter�vel.
	 * 
	 * @param int - N�mero da Aba da Planilha deseja. Inicia por padr�o come�a no 0.
	 */
	public ExcelManager(int indexSheet) {
		defaultFileName();
		setIndex(indexSheet);
	}

	/**
	 * Construtor com nome do arquivo e ind�ce da aba da planilha selecionada
	 * alter�veis.
	 * 
	 * @param String - Nome do arquivo excel desejado com a extens�o correta XLS ou
	 *               XLSX.
	 * @param int    - N�mero da Aba da Planilha deseja. Inicia por padr�o come�a no
	 *               0.
	 */
	public ExcelManager(String fileName, int indexSheet) {
		setFileName(fileName);
		setIndex(indexSheet);
	}

	/**
	 * Construtor Padr�o com nome e aba de planilha j� configurados e n�o alter�veis
	 */
	public ExcelManager() {
		defaultFileName();
		defaultIndex();
	}

	// ******************************
	// Configura��es Padr�es
	// ******************************

	/**
	 * Configura o nome do arquivo excel com base no que est� configurado no arquivo
	 * setup.properties.
	 *
	 */
	private void defaultFileName() {
		setFileName(Utils.getProp("file.excel"));
	}

	/**
	 * Configura o arquivo excel para selecionar a primeira aba do arquivo
	 * carregado.
	 *
	 */
	private void defaultIndex() {
		setIndex(0);
	}

	private void setFileName(String newName) {
		fileName = newName;
	}

	private void setIndex(int indexSheet) {
		index = indexSheet;
	}

	// ******************************
	// Configura��o de Carregamento
	// ******************************

	/**
	 * M�todo respons�vel por carregar o arquivo excel na mem�ria. Deve ser iniciado
	 * sempre que for carregar um arquivo.
	 */
	public void setup() {

		if (!fileName.toLowerCase().contains(".xls")) {
			log.error("O nome do Arquivo est� escrito incorretamente, verifique a extens�o 'XLS' ou 'XLSX");
			assertTrue(fileName.toLowerCase().contains(".xls"));
		}

		String excelPath = getExcelPath();

		try {
			inputstream = new FileInputStream(new File(excelPath));
			workbook = new XSSFWorkbook(inputstream);
			log.info("Arquivo Excel foi carregado.");
		} catch (IOException e) {
			e.printStackTrace();
			log.fatal("N�o foi poss�vel localizar o arquivo Excel, verifique a extens�o 'XLS' ou 'XLSX !");
		}

	}

	/**
	 * M�todo respons�vel por encerrar a leitura do arquivo excel em mem�ria,
	 * encerrando todos os componentes abertos.
	 */
	public void exit() {
		try {
			workbook.close();
			inputstream.close();
			log.info("Arquivo Excel finalizado corretamente.");
		} catch (IOException e) {
			log.info("N�o foi poss�vel fechar o Workbook ou o arquivo Excel!");
			e.printStackTrace();
		}
	}

	/**
	 * M�todo respons�vel por obter o caminho do diret�rio onde est�o localizados as
	 * planilhas Excel.
	 * 
	 * @return String - Caminho do diret�rio onde est�o localizados as planilhas
	 *         Excel.
	 */
	private String getExcelPath() {
		String pathExcel = Utils.getProp("file.excel.path") + fileName;
		return pathExcel;
	}

	// ******************************
	// Gerenciar Celulas e Colunas
	// ******************************

	/**
	 * M�todo privado que retona uma lista de Linhas da planilha Excel que foi
	 * carregada na mem�ria.
	 * 
	 * @return Iterator<Row> - Lista de Linhas da planilha Excel.
	 */
	private Iterator<Row> getRows() {
		Sheet currentSheet = workbook.getSheetAt(index);
		Iterator<Row> rows = currentSheet.iterator();
		return rows;
	}

	/**
	 * M�todo publico que retona uma lista de c�lulas e seus valores da planilha
	 * Excel que foi carregada na mem�ria.
	 * 
	 * @return List<Cell> - Lista de todas as c�lulas da planilha Excel.
	 */
	public List<Cell> getCells() {
		setup();
		Iterator<Row> rows = getRows();
		List<Cell> cells = new ArrayList<Cell>();

		while (rows.hasNext()) {
			Row currentRow = rows.next();
			Iterator<Cell> cellIterator = currentRow.cellIterator();

			while (cellIterator.hasNext()) {
				cells.add(cellIterator.next());
			}
		}
		exit();

		return cells;
	}

	// ******************************
	// Demonstra��o - Padr�o
	// ******************************
	public static void main(String[] args) {

		// Instancia do Objeto Manager
		ExcelManager em = new ExcelManager(1);

		// Obtem todas as C�lulas
		List<Cell> cells = em.getCells();

		// Realiza Itera��o para printar os valores ou passar como paramentro para um
		// objeto do tipo ExcelObject
		for (Cell cell : cells) {

			System.out.printf("LINHA ATUAL: %s", cell.getRowIndex());
			switch (cell.getCellType()) {
			case STRING:
				System.out.printf(" - CAMPO: %s", cell.getStringCellValue());
				break;

			case BOOLEAN:
				System.out.printf(" - CAMPO: %s", cell.getBooleanCellValue());
				break;

			case NUMERIC:
				System.out.printf(" - CAMPO: %s", cell.getNumericCellValue());
				break;

			default:
				break;
			}
			System.out.println();
		}
	}
}
