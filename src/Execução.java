import java.io.IOException;
import java.util.Scanner;

public class Execução {
	
	
	public static void main(String[] args) throws IOException{
		
		Scanner teclado = new Scanner(System.in);

		System.out.println("Digite uma letra do alfabeto (caracter maiusculo): ");

		String texto = teclado.nextLine();
		
		XmlTeste.getInstance().StartXML(texto);
		
	}
	 
}
