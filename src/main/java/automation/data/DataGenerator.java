package automation.data;

/*
 * @author Luiz Felipe Alves de Sousa
 * @version 1.00
 * @since 09/01/2020
 * 
 * Classe Est�tica que possui a responsabilidade de gerar n�meros de CPF e CNPJ v�lidos
*/

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {

	// ******************************
	// CPF
	// ******************************

	/**
	 * M�todo retorna o tamanho de um n�mero de CPF (11).
	 * 
	 * @return int - 11
	 */
	private static int cpfSize() {
		return 11;
	}

	/**
	 * M�todo gera um CPF v�lido com um n�mero final espec�fico.
	 * 
	 * @param int - digito final desejado de 0 a 9 para gerar o CPF.
	 * @return String - Numera��o do CPF com o digito desejado AAA AAA AAA A(0-9)
	 */
	public static String gerarCPFcomFinal(int digito) {

		List<Integer> cpf = gerarCPF();
		String string_cpf = "";

		while (cpf.get(cpf.size() - 1) != digito) {
			cpf = gerarCPF();
		}

		for (int i = 0; i < cpf.size(); i++) {
			string_cpf = string_cpf + cpf.get(i);
		}

		return string_cpf;
	}

	/**
	 * M�todo gera um CPF aleat�rio v�lido.
	 * 
	 * @return List<Integer> - Lista de Inteiros contendo todos os 11 n�meros do CPF
	 *         gerado.
	 */
	public static List<Integer> gerarCPF() {
		List<Integer> numbers = new ArrayList<Integer>();

		for (int i = 0; i < 9; i++) {
			numbers.add(new Random().nextInt(9));
		}

		gerarDigito(numbers);
		gerarDigito(numbers);

		return numbers;
	}

	/**
	 * M�todo gera os dois digitos validadores de um CPF.
	 * 
	 * @param List<Integer> - Uma lista de n�meros de CPF com o tamanho de 9
	 *                      digitos.
	 */
	private static void gerarDigito(List<Integer> numbers) {
		int contador = numbers.size() + 1;
		int result = 0;

		for (Integer integer : numbers) {
			result += integer * contador;
			contador--;
		}

		if (result % cpfSize() < 2) {
			numbers.add(0);
		} else {
			int sub = cpfSize() - (result % cpfSize());
			numbers.add(sub);
		}

	}

	// ******************************
	// CNPJ
	// ******************************

	/**
	 * M�todo gera um CNPJ v�lido com um n�mero final espec�fico.
	 * 
	 * @param int - digito final desejado de 0 a 9 para gerar o CNPJ.
	 * @return String - Numera��o do CNPJ com o digito desejado
	 *         AAA.AAA.AAA.AAAA.A(0-9)
	 */
	public static String gerarCNPJcomFinal(int digito) {

		List<Integer> cnpj = gerarCNPJ();
		String string_cnpj = "";

		while (cnpj.get(cnpj.size() - 1) != digito) {
			cnpj = gerarCNPJ();
		}

		for (int i = 0; i < cnpj.size(); i++) {
			string_cnpj = string_cnpj + cnpj.get(i);
		}

		return string_cnpj.toString();
	}

	/**
	 * M�todo gera um CNPJ aleat�rio v�lido.
	 * 
	 * @return List<Integer> - Lista de Inteiros contendo todos os 15 n�meros do
	 *         CNPJ gerado.
	 */
	private static List<Integer> gerarCNPJ() {
		List<Integer> numbers = new ArrayList<Integer>();

		for (int i = 0; i < 12; i++) {
			if (i == 11) {
				numbers.add(1);
			} else if (i > 7) {
				numbers.add(0);
			} else {
				numbers.add(new Random().nextInt(9));
			}
		}

		geradorDigitoCNPJ(numbers);
		geradorDigitoCNPJ(numbers);

		return numbers;
	}

	/**
	 * M�todo gera os dois digitos validadores de um CNPJ.
	 * 
	 * @param List<Integer> - Uma lista de n�meros de CPF com o tamanho de 13
	 *                      digitos.
	 */
	private static void geradorDigitoCNPJ(List<Integer> baseCNPJ) {
		int calc = 0;
		int size;
		if (baseCNPJ.size() == 12) {
			size = 5;
		} else {
			size = 6;
		}

		for (int i = 0; i < baseCNPJ.size(); i++) {
			if (baseCNPJ.size() - i == 8) {
				size = 9;
			}
			calc += baseCNPJ.get(i) * size;
			size--;
		}

		if (calc % cpfSize() < 2) {
			baseCNPJ.add(0);
		} else {
			int sub = cpfSize() - (calc % cpfSize());
			baseCNPJ.add(sub);
		}
	}
}
