package br.com.sistema.rotinas.home;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import br.com.sistema.amazon.AmazonUtil;
import br.com.sistema.enuns.TipoOrigemArquivoBinario;
import br.com.sistema.exceptions.MensagemException;
import br.com.sistema.hibernate.HibernateUtil;
import br.com.sistema.jsf.FacesUtil;
import br.com.sistema.jsf.Mensagens;
import br.com.sistema.rotinas.arquivos.Arquivo;
import br.com.sistema.rotinas.arquivos.BoArquivo;
import br.com.sistema.rotinas.produtos.Carrinho;
import br.com.sistema.rotinas.produtos.CarrinhoItem;
import br.com.sistema.rotinas.produtos.ImagemProduto;
import br.com.sistema.rotinas.produtos.Produto;
import br.com.sistema.rotinas.produtos.ProdutosDAO;
import br.com.sistema.rotinas.usuario.Usuario;
import br.com.sistema.util.Constantes;
import br.com.sistema.util.ParametrosSistema;

@ViewScoped
@ManagedBean(name = "homeBean")
public class HomeBean implements Serializable {

	private static final long serialVersionUID = 3156773698941473780L;

	private String textoPesquisa;

	private Carrinho carrinho;

	private Produto produtoSelecionado;

	private Usuario usuario = ParametrosSistema.getUsuarioLogado();

	private List<Produto> produtos = new ArrayList<>();
	private List<ImagemProduto> imagens = new ArrayList<ImagemProduto>();

	private int quantidadeDesejada;
	private double precoTotal;

	public HomeBean() {

		if (usuario == null) {
			usuario = new Usuario();
		}

		if (carrinho == null) {
			carrinho = new Carrinho();
		}

		List<Produto> produtosSemImagem = new ArrayList<>();

		produtosSemImagem = new ProdutosDAO().getListaParaPesquisaHome("");
		carregarImagens();
		for (Produto p : produtosSemImagem) {
			carregarImagemProduto(p);
			produtos.add(p);
		}

	}

	public void getListaPorNome() {
		this.produtos = new ProdutosDAO().getListaParaPesquisaHome(this.textoPesquisa);
		for (Produto p : produtos) {
			carregarImagemProduto(p);
		}
	}

	public void carregarImagens() {

		imagens = new ArrayList<ImagemProduto>();

		String urlImg1 = "../resources/images/home/play-quatro.jpg";
		String urlImg2 = "../resources/images/home/fifa-vinte.png";
		String urlImg3 = "../resources/images/home/xbox-one.jpg";
		String urlImg4 = "../resources/images/home/god-of-war-4.jpg";
		String urlImg5 = "../resources/images/home/switch.jpg";

		ImagemProduto img1 = new ImagemProduto();
		ImagemProduto img2 = new ImagemProduto();
		ImagemProduto img3 = new ImagemProduto();
		ImagemProduto img4 = new ImagemProduto();
		ImagemProduto img5 = new ImagemProduto();

		img1.setUrlGrande(urlImg1);
		img2.setUrlGrande(urlImg2);
		img3.setUrlGrande(urlImg3);
		img4.setUrlGrande(urlImg4);
		img5.setUrlGrande(urlImg5);

		this.imagens.add(img1);
		this.imagens.add(img2);
		this.imagens.add(img3);
		this.imagens.add(img4);
		this.imagens.add(img5);

	}

	public void carregarImagemProduto(Produto produto) {
		try {

			BoArquivo bo = new BoArquivo(produto.getId(), Constantes.BUCKET_S3_DEVBROS);

			List<Arquivo> imgs = bo.getListaDeArquivos(TipoOrigemArquivoBinario.Produto, produto.getId(), false);

			if (imgs.size() <= 0) {
				carregarImagensEmBranco(produto);
			} else if (imgs.size() > 0) {

				String urlGrande = new AmazonUtil(produto.getId(), Constantes.BUCKET_S3_DEVBROS)
						.gerarURL(imgs.get(0).getNome()).toString();

				ImagemProduto img = new ImagemProduto();
				img.setIdImagem(imgs.get(0).getId());
				img.setUrlGrande(urlGrande);
				produto.setImagemPrincipal(img);
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

	private void carregarImagensEmBranco(Produto produto) {

		String urlImagem = "../resources/images/produtos/semFoto.jpg";

		ImagemProduto img1 = new ImagemProduto();

		img1.setUrlGrande(urlImagem);

		produto.setImagemPrincipal(img1);

	}

	public void selecionarProduto(Produto p) {

		this.produtoSelecionado = p;

		carregarImagemProduto(produtoSelecionado);

		FacesUtil.addCallbackParam("showDialog", true);

	}

	public void adicionarAoCarrinho() throws Exception {

		Session session = HibernateUtil.getSession();

		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			CarrinhoItem item = new CarrinhoItem();

			this.carrinho = carregaCarrinho(usuario);

			item.setQuantidade(quantidadeDesejada);
			item.setPrecoItemTotal(this.getPrecoTotal());
			item.setProduto(produtoSelecionado);
			item.setCarrinho(carrinho);

			if (carrinho.getId() > 0) {
				session.update(carrinho);
			} else {
				session.save(carrinho);
			}

			if (produtoSelecionado.getQuantidade() < quantidadeDesejada) {
				throw new MensagemException(Mensagens.getMensagem("Esse produto não tem essa quantidade em estoque!"));
			}

			boolean estaNoCarrinho = false;

			if (carrinho.getItens() != null) {
				for (CarrinhoItem i : carrinho.getItens()) {
					if (!estaNoCarrinho) {
						if (i.getProduto().getId() == item.getProduto().getId()) {
							i.setQuantidade(i.getQuantidade() + item.getQuantidade());

							session.flush();
							session.update(i);

							estaNoCarrinho = true;
						}
					}
				}
			}

			if (!estaNoCarrinho) {
				session.flush();
				session.save(item);
			}

			this.produtoSelecionado = null;
			this.quantidadeDesejada = 0;

			tx.commit();

		} catch (HibernateException ex) {
			tx.rollback();
			throw ex;
		} catch (Exception ex) {
			tx.rollback();
			throw ex;
		} finally {
			session.close();
		}

	}

	public Carrinho carregaCarrinho(Usuario usuario) {

		Session session = HibernateUtil.getSession();

		try {

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Carrinho> query = builder.createQuery(Carrinho.class);
			Root<Carrinho> root = query.from(Carrinho.class);

			query.select(root).where(builder.equal(root.get("usuario"), usuario.getId()));

			Query<Carrinho> q = session.createQuery(query);

			if (q.getResultList().size() > 0) {
				for (Carrinho car : q.getResultList()) {
					if (car.getItens().size() > 0) {
						for (CarrinhoItem item : car.getItens()) {
							Hibernate.initialize(item);
							Hibernate.initialize(item.getProduto());
						}
					}
				}

				return q.getResultList().get(0);
			} else {
				Carrinho carrinho = new Carrinho();
				carrinho.setUsuario(usuario);
				return carrinho;
			}

		} catch (HibernateException ex) {
			throw ex;
		} catch (Exception ex) {
			throw ex;
		} finally {
			session.close();
		}

	}

	public void abrirCarrinho() {

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("Carrinho" + Constantes.EXTENSAO_JSF);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void abrirVendas() {

		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("RelatorioVendas" + Constantes.EXTENSAO_JSF);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getTextoPesquisa() {
		return textoPesquisa;
	}

	public void setTextoPesquisa(String textoPesquisa) {
		this.textoPesquisa = textoPesquisa;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public List<ImagemProduto> getImagens() {
		return imagens;
	}

	public void setImagens(List<ImagemProduto> imagens) {
		this.imagens = imagens;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public int getQuantidadeDesejada() {
		return quantidadeDesejada;
	}

	public void setQuantidadeDesejada(int quantidadeDesejada) {
		this.quantidadeDesejada = quantidadeDesejada;
	}

	public void setPrecoTotal(double precoTotal) {
		this.precoTotal = precoTotal;
	}

	public double getPrecoTotal() {
		if (produtoSelecionado != null) {
			this.precoTotal = quantidadeDesejada * produtoSelecionado.getPreco();
		}
		return precoTotal;
	}

	public Carrinho getCarrinho() {
		return carrinho;
	}

	public void setCarrinho(Carrinho carrinho) {
		this.carrinho = carrinho;
	}

}