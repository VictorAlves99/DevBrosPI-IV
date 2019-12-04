package br.com.sistema.rotinas.produtos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.hibernate.HibernateException;
import org.primefaces.event.FileUploadEvent;

import br.com.sistema.amazon.AmazonUtil;
import br.com.sistema.enuns.TipoFormatoDeArquivo;
import br.com.sistema.enuns.TipoOrigemArquivoBinario;
import br.com.sistema.enuns.TipoPlataformaProduto;
import br.com.sistema.exceptions.MensagemException;
import br.com.sistema.jsf.Mensagens;
import br.com.sistema.rotinas.arquivos.Arquivo;
import br.com.sistema.rotinas.arquivos.BoArquivo;
import br.com.sistema.util.Caracter;
import br.com.sistema.util.Combos;
import br.com.sistema.util.Constantes;
import br.com.sistema.util.Lib;
import br.com.sistema.util.ParametrosSistema;

@ManagedBean(name = "produtoBean")
@ViewScoped
public class ProdutosBean implements Serializable {

	private static final long serialVersionUID = 3156773698941473780L;

	private Produto objeto;

	private String textoPesquisa;
	private String pergunta;
	private String resposta;
	
	private List<ImagemProduto> imagens;
	private ImagemProduto imagemSelecionada;
	
	private boolean produtoSalvo = false;
	private final boolean usuarioAdm = ParametrosSistema.getUsuarioLogado().getTipoUsuario().ordinal() == 1;

	private List<Produto> produtosAAdicionar = new ArrayList<>();
	private List<Produto> produtos = new ArrayList<>();
	private List<TipoPlataformaProduto> plataformasEscolhidas;

	public ProdutosBean() {
		
	}
	
	public void adicionarProdutos(){

		try(BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\priscyla.poliveira\\Desktop\\DevBrosPI-IV\\src\\main\\webapp\\resources\\outros\\in.txt"))){
			
			String line = br.readLine();
			while(line != null) {
				String[] linha = line.split(",");
				Produto produto = new Produto(linha[0],linha[1],linha[2],Double.parseDouble(linha[3]),Integer.parseInt(linha[4]),verificaEnum(linha[5]));
				produtosAAdicionar.add(produto);
				line = br.readLine();
			}
			
			produtosAAdicionar.forEach(Produto::adicionarProduto);
			
			Mensagens.gerarMensagemGenerica("Produtos salvos com sucesso!");
			
		}catch(IOException e) {
			Mensagens.gerarMensagemException(e);
		}catch(Exception e) {
			Mensagens.gerarMensagemException(e);
		}
	}
	
	private TipoPlataformaProduto verificaEnum(String string) {
		if(string.equalsIgnoreCase("Playstation 4")||string.equalsIgnoreCase("PS4")) {
			return TipoPlataformaProduto.PS4;
		}else if(string.equalsIgnoreCase("Playstation 3")||string.equalsIgnoreCase("PS3")) {
			return TipoPlataformaProduto.PS3;
		}else if(string.equalsIgnoreCase("XBOX ONE")) {
			return TipoPlataformaProduto.XBOXONE;
		}else if(string.equalsIgnoreCase("XBOX 360")) {
			return TipoPlataformaProduto.XBOX360;
		}else if(string.equalsIgnoreCase("Switch")||string.equalsIgnoreCase("Nintendo Switch")) {
			return TipoPlataformaProduto.SWITCH;
		}else if(string.equalsIgnoreCase("Console")) {
			return TipoPlataformaProduto.CONSOLE;
		}
		return TipoPlataformaProduto.CONSOLE;
	}

	public void subirArquivo(FileUploadEvent event) {
		try {
			if (this.objeto == null || this.objeto.getId() <= 0)
				throw new MensagemException(
						Mensagens.getMensagem("É necessário salvar o cadastro antes de incluir as imagens."));

			String nomeDoArquivo = Caracter.getRandomString(30) +
					Caracter.getExtensaoDoArquivo(event.getFile().getFileName());
			
			TipoFormatoDeArquivo formato = TipoFormatoDeArquivo
					.getFormatoDeArquivoPelaExtensao(event.getFile().getFileName());

			long tamanhoDoArquivoOriginal = event.getFile().getSize();

			InputStream arq = Lib.getInputStreamFromByteArray(event.getFile().getContents());
			
			Arquivo arquivo = new Arquivo();
			
			arquivo.setNome(nomeDoArquivo);
			arquivo.setDataUpload(ParametrosSistema.getDataHoraSistema());
			arquivo.setFormato(formato);
			arquivo.setTamanho(tamanhoDoArquivoOriginal);
			arquivo.setStream(arq);
			arquivo.setOrigem(TipoOrigemArquivoBinario.Produto);
			arquivo.setIdExterno(this.objeto.getId());
				
			new BoArquivo(this.objeto.getId(), Constantes.BUCKET_S3_DEVBROS)
					.salvarArquivo(arquivo);

			Mensagens.gerarMensagemGenerica("Upload efetuado com sucesso.");

		} catch (MensagemException e) {
			Mensagens.gerarMensagemGenerica(e.getMessage());
		} catch (Exception e) {
			Mensagens.gerarMensagemException(e);
		}
	}
	
	public void carregarImagens() {
		try {
			
			BoArquivo bo = new BoArquivo(this.objeto.getId(), Constantes.BUCKET_S3_DEVBROS);

			List<Arquivo> docs = bo.getListaDeArquivos(TipoOrigemArquivoBinario.Produto, this.objeto.getId(),
					false);

			if (docs.size() <= 0) {
				carregarImagensEmBranco();
			} else {

				imagens = new ArrayList<ImagemProduto>();

				for (Arquivo d : docs) {

					String urlGrande = new AmazonUtil(this.objeto.getId(),
							Constantes.BUCKET_S3_DEVBROS).gerarURL(d.getNome()).toString();

					ImagemProduto doc = new ImagemProduto();
					doc.setIdImagem(d.getId());
					doc.setUrlGrande(urlGrande);
					this.imagens.add(doc);
					
				}
			}
			
		} catch (IOException e) {
			Mensagens.gerarMensagemException(e);
		} catch (HibernateException e) {
			Mensagens.gerarMensagemException(e);
		} catch (MensagemException e) {
			Mensagens.gerarMensagemException(e);
		} catch (Exception e) {
			Mensagens.gerarMensagemException(e);
		}
	}

	private void carregarImagensEmBranco() {

		imagens = new ArrayList<ImagemProduto>();

		String urlDocumentos = "../resources/images/produtos/semFoto.jpg";

		ImagemProduto doc1 = new ImagemProduto();

		doc1.setUrlGrande(urlDocumentos);

		this.imagens.add(doc1);
		
	}
	
	public void escolheImagem(ImagemProduto doc) {
		this.imagemSelecionada = doc;
	}

	public void carregarTela(Produto produto) {
		this.produtoSalvo = true;
		this.objeto = produto;
		carregarImagens();
	}
	
	public void carregarEspecificacao(Produto produto) {
		this.objeto = produto;
	}
	
	public void pesquisar() {
		this.produtos = new ProdutosDAO().getListaParaPesquisa(this.textoPesquisa);
	}

	public void salvarCadastro() {
		try {
			if (this.objeto.getId() > 0) {
				new ProdutosDAO().update(this.objeto);
			} else {
				new ProdutosDAO().save(this.objeto);
			}
			
			this.objeto = null;
			
			Mensagens.gerarMensagemGenerica("Produto salvo com sucesso");
		} catch (MensagemException e) {
			Mensagens.gerarMensagemGenerica(e.getMessage());
		} catch (Exception e) {
			Mensagens.gerarMensagemException(e);
		}
	}

	public void adicionarPerguntasERespostas() {
		try {
			PerguntasERespostas pergEResp = new PerguntasERespostas();
			
			pergEResp.setPergunta(this.pergunta);
			pergEResp.setResposta(this.resposta);
			pergEResp.setProduto(this.objeto);
			
			new PerguntasERespostasDAO().save(pergEResp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void excluirCadastro(Produto p) {
		try {
			this.objeto = p;
			
			if (this.objeto.getId() <= 0)
				throw new MensagemException("Não é possível excluir um cadastro que ainda não foi salvo");

			new ProdutosDAO().delete(this.objeto);
			
			Mensagens.gerarMensagemGenerica("Produtos excluído com sucesso");
			this.objeto = null;
		} catch (MensagemException e) {
			Mensagens.gerarMensagemGenerica(e.getMessage());
		} catch (Exception e) {
			Mensagens.gerarMensagemException(e);
		}
	}
	
	public void incluir() {
		this.objeto = new Produto();
		this.plataformasEscolhidas = new ArrayList<>();
	}

	public void zerarCampos() {

	}

	public List<SelectItem> getComboTipoPlataformaProduto() {
		return Combos.getComboTipoPlataformaProduto();
	}
	
	public Produto getObjeto() {
		return objeto;
	}

	public void setObjeto(Produto objeto) {
		if(objeto == null) {
			this.produtoSalvo = false;
			this.objeto = objeto;
		}else {
			this.objeto = objeto;
		}
	}

	public String getTextoPesquisa() {
		return textoPesquisa;
	}

	public void setTextoPesquisa(String textoPesquisa) {
		this.textoPesquisa = textoPesquisa;
	}

	public String getPergunta() {
		return pergunta;
	}

	public void setPergunta(String pergunta) {
		this.pergunta = pergunta;
	}

	public String getResposta() {
		return resposta;
	}

	public void setResposta(String resposta) {
		this.resposta = resposta;
	}

	public List<ImagemProduto> getImagens() {
		return imagens;
	}

	public void setImagens(List<ImagemProduto> imagens) {
		this.imagens = imagens;
	}

	public ImagemProduto getImagemSelecionada() {
		return imagemSelecionada;
	}

	public void setImagemSelecionada(ImagemProduto imagemSelecionada) {
		this.imagemSelecionada = imagemSelecionada;
	}

	public boolean isProdutoSalvo() {
		return produtoSalvo;
	}

	public void setProdutoSalvo(boolean produtoSalvo) {
		this.produtoSalvo = produtoSalvo;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public List<TipoPlataformaProduto> getPlataformasEscolhidas() {
		return plataformasEscolhidas;
	}

	public void setPlataformasEscolhidas(List<TipoPlataformaProduto> plataformasEscolhidas) {
		this.plataformasEscolhidas = plataformasEscolhidas;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isUsuarioAdm() {
		return usuarioAdm;
	}
	
}
