import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class XmlTeste {

	private static XmlTeste instance = null;
	private BufferedWriter escritor = null;

	// --------------------------------------------------------------------------------------------------------//

	private XmlTeste() {

	}

	public static XmlTeste getInstance() {
		if (instance == null) {
			instance = new XmlTeste();
		}
		return instance;
	}

	// --------------------------------------------------------------------------------------------------------//

	public void InicioXML(String letra) throws IOException {

		/**
		 * Estrutura Inicial XML
		 */

		escritor.append("<?xml version=\"1.0\"?> \r\n");
		escritor.append("<dic> \r\n");
		escritor.append("<head>" + letra + "</head>\r\n");
		escritor.append("\r\n");
		escritor.append("\r\n");
		escritor.flush();

	}

	public void FinalXML() throws IOException {

		/**
		 * Tag de encerramento XML
		 */

		escritor.append("\r\n");
		escritor.append("\r\n");
		escritor.append("</dic> \r\n");
		escritor.flush();
	}

	public void PalavraXML(String textPASS) throws IOException {

		/**
		 * Estrutura Inserção da PALAVRA no XML
		 */

		escritor.append("<entry id=\"" + textPASS + "\">\r\n");
		escritor.append("<form> \r\n");
		escritor.append("<orth>" + textPASS + "</orth>\r\n");
		escritor.append("</form>\r\n");
		escritor.flush();

	}

	public void ClassificacaoXML(String classPASS) throws IOException {

		/**
		 * Estrutura Inserção da classificação PALAVRA no XML
		 */

		escritor.append("<sense>\r\n");
		escritor.append("<gramGrp>" + classPASS + "</gramGrp>\r\n");
		escritor.flush();

	}

	// --------------------------------------------------------------------------------------------------------//

	private static void replaceAll(StringBuffer builder, String from, String to) {

		/**
		 * Dada uma string (builder), uma segunda string (form) será buscada
		 * dentro da anterior, e substituida por outra string (to)
		 */

		int index = builder.indexOf(from);
		while (index != -1) {
			builder.replace(index, index + from.length(), to);
			index += to.length();
			index = builder.indexOf(from, index);
		}
	}

	// --------------------------------------------------------------------------------------------------------//

	public void StartXML(String letra) throws IOException {

		BufferedReader leitor = null;
		String linha = "";
		String textPASS = "";
		String ClassPASS = "";

		System.out.println("START");

		try {
			if (new File("src/dicxml/" + letra + ".xml").exists() == false) {

				new File("src/dicxml/" + letra + ".xml").createNewFile();

				escritor = new BufferedWriter(new FileWriter("src/dicxml/" + letra + ".xml"));

				System.out.println("XML criado");

				InicioXML(letra);

				System.out.println("Estrutura inicial do arquivo inserida");

			}

			leitor = new BufferedReader(new FileReader("src/dictxt/" + letra + ".txt"));

			int key = -1; // key inicia com -1 (não encontrou espaço em branco)

			while ((linha = leitor.readLine()) != null) {

				if (key == 3) {

					if (linha.length() > 1) { // mais de uma linha no campo de
												// definição
						escritor.append(linha + "\r\n");
						escritor.flush();
					} else {
						key = -1; // fechar bloco de significado da palavra e
									// resetar a key
						escritor.append("</def> \r\n");
						escritor.append("</sense>\r\n");
						escritor.append("</entry>\r\n");
						escritor.append("\r\n");
						escritor.flush();
						System.out.println("*");
					}

				}

				if (key == 2) {

					escritor.append("<def> \r\n");
					escritor.append(linha + "\r\n");
					escritor.flush();

					key = 3;

				}

				if (key == 1) {

					StringBuffer aux1 = new StringBuffer();
					aux1.append(linha);
					replaceAll(aux1, "_", "");
					ClassPASS = aux1.toString();
					ClassificacaoXML(ClassPASS);

					key = 2;
				}

				if (key == 0) {

					if (linha.length() > 1 && linha.contains("*")) {

						StringBuffer aux = new StringBuffer();
						aux.append(linha);
						replaceAll(aux, "*", "");
						replaceAll(aux, "+", "");
						replaceAll(aux, ",", "");
						replaceAll(aux, "^1", "");
						replaceAll(aux, "^2", "");
						replaceAll(aux, "^3", "");
						replaceAll(aux, "{", "");
						replaceAll(aux, "}", "");
						replaceAll(aux, "; com zêlo.", "");

						textPASS = aux.toString();

						PalavraXML(textPASS); // inserção da palavra nas
												// tags
												// XML

						key = 1; // key passa a ser 1 (limpou palavra e a
									// escreve) - passa para a etapa de
									// classificação da palavra

					}

				}

				if (linha.length() == 0) {
					key = 0; // key passa a ser 0 (encontrou espaço em branco)
				}

			}
			
			System.out.println("colocar fim");
			
			escritor.append("\r\n");
			escritor.append("\r\n"); // fechar XML
			escritor.append("</dic>\r\n");
			escritor.flush();

			System.out.println("END");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// --------------------------------------------------------------------------------------------------------//

}
