package br.com.sistema.hibernate;

import java.util.EnumSet;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import br.com.sistema.rotinas.arquivos.Arquivo;
import br.com.sistema.rotinas.produtos.Carrinho;
import br.com.sistema.rotinas.produtos.CarrinhoItem;
import br.com.sistema.rotinas.produtos.PerguntasERespostas;
import br.com.sistema.rotinas.produtos.Produto;
import br.com.sistema.rotinas.usuario.Usuario;
import br.com.sistema.rotinas.usuario.UsuarioEndereco;
import br.com.sistema.rotinas.vendas.Venda;

public class SchemaUtil {

	public static void main(String[] args) {
		try {
			criarSchema("db_loja_noobies");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void criarSchema(String schema) {

		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();

		builder.applySetting(Environment.DEFAULT_SCHEMA, schema);

		MetadataSources metadata = new MetadataSources(builder.build());

		metadata.addAnnotatedClass(Usuario.class);
		metadata.addAnnotatedClass(Produto.class);
		metadata.addAnnotatedClass(Carrinho.class);
		metadata.addAnnotatedClass(CarrinhoItem.class);
		metadata.addAnnotatedClass(Arquivo.class);
		metadata.addAnnotatedClass(PerguntasERespostas.class);
		metadata.addAnnotatedClass(Venda.class);
		metadata.addAnnotatedClass(UsuarioEndereco.class);

		new SchemaExport().create(EnumSet.of(TargetType.DATABASE), metadata.buildMetadata());
	}
}
