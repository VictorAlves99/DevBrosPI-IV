import br.com.sistema.seguranca.TripleDES;
import br.com.sistema.util.Constantes;

public class Teste1 {

	public static void main(String[] args) throws Exception {
		System.out.println(new TripleDES(Constantes.KEY_TRIPLE_DES).encrypt("1234"));
	}
}