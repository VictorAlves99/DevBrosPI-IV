package br.com.sistema.rotinas.produtos;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import br.com.sistema.amazon.AmazonUtil;
import br.com.sistema.enuns.TipoFormaPagamento;
import br.com.sistema.enuns.TipoOrigemArquivoBinario;
import br.com.sistema.exceptions.MensagemException;
import br.com.sistema.hibernate.HibernateUtil;
import br.com.sistema.jsf.FacesUtil;
import br.com.sistema.jsf.Mensagens;
import br.com.sistema.rotinas.arquivos.Arquivo;
import br.com.sistema.rotinas.arquivos.BoArquivo;
import br.com.sistema.rotinas.usuario.Usuario;
import br.com.sistema.rotinas.usuario.UsuarioDAO;
import br.com.sistema.rotinas.usuario.UsuarioEndereco;
import br.com.sistema.rotinas.vendas.Venda;
import br.com.sistema.util.Combos;
import br.com.sistema.util.Constantes;
import br.com.sistema.util.ParametrosSistema;

@ManagedBean(name = "cartBean")
@ViewScoped
public class CarrinhoBean implements Serializable {

	private static final long serialVersionUID = -6826121377569634764L;

	private Carrinho carrinho;

	private Venda venda;

	private double precoParcial;

	private double precoFrete;

	private Usuario usuario = ParametrosSistema.getUsuarioLogado();

	private List<String> enderecos = new ArrayList<>();
	private String enderecoEscolhido;
	private UsuarioEndereco usuarioEnderecoEscolhido;

	private TipoFormaPagamento formaDePagamento;

	public CarrinhoBean() {
		enderecos = carregarEnderecos();
		enderecoEscolhido = enderecos.get(0);
		usuarioEnderecoEscolhido = new UsuarioDAO().getEnderecoPorString(enderecoEscolhido, usuario);
		
		carrinho = carregaCarrinho();
		
		if (carrinho.getItens() != null && carrinho.getItens().size() > 0) {
			for (CarrinhoItem i : carrinho.getItens()) {
				carregarImagemProduto(i.getProduto());
			}
		} else {
			carrinho.setItens(new ArrayList<CarrinhoItem>());
		}
		
		venda = new Venda();
	}

	private List<String> carregarEnderecos() {
		Session session = HibernateUtil.getSession();

		try {

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<UsuarioEndereco> query = builder.createQuery(UsuarioEndereco.class);
			Root<UsuarioEndereco> root = query.from(UsuarioEndereco.class);

			query.select(root).where(builder.equal(root.get("usuario"), usuario));

			Query<UsuarioEndereco> q = session.createQuery(query);

			List<String> lista = new ArrayList<>();

			for (UsuarioEndereco end : q.getResultList()) {
				lista.add(end.getEndereco());
			}

			return lista;

		} catch (HibernateException ex) {
			throw ex;
		} catch (Exception ex) {
			throw ex;
		} finally {
			session.close();
		}
	}

	public void removeItem(CarrinhoItem item) {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			session.delete(item);

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

	public void encerrarCompra() throws Exception {
		Session session = HibernateUtil.getSession();
		try {
			usuarioEnderecoEscolhido = new UsuarioDAO().getEnderecoPorString(enderecoEscolhido, usuario);

			venda.setPrecoTotal(this.getPrecoTotal());
			venda.setCliente(this.usuario);
			venda.setFormaPagamento(formaDePagamento);
			venda.setEndereco(usuarioEnderecoEscolhido);

			if (venda.getId() > 0) {
				session.update(venda);
			} else {
				session.save(venda);
			}

			List<CarrinhoItem> lista = new ProdutosDAO().getItensDoCarrinho(carrinho);

			for (CarrinhoItem c : lista) {

				Produto p = c.getProduto();
				p.setQuantidade(p.getQuantidade() - c.getQuantidade());
				if (p.getQuantidade() >= 0) {
					session.update(p);
				} else {
					throw new MensagemException(Mensagens
							.getMensagem("Não temos o produto " + p.getNome() + " nesta quantidade em estoque!"));
				}

				c.setCarrinho(null);
				c.setVenda(venda);
				session.update(c);
			}

			FacesUtil.addCallbackParam("showDialog", true);

		} catch (HibernateException ex) {
			Mensagens.gerarMensagemGenerica(ex.getMessage());
		} catch (Exception ex) {
			Mensagens.gerarMensagemGenerica(ex.getMessage());
		} finally {
			session.close();
		}
	}

	public Carrinho carregaCarrinho() {

		Session session = HibernateUtil.getSession();

		try {

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Carrinho> query = builder.createQuery(Carrinho.class);
			Root<Carrinho> root = query.from(Carrinho.class);

			query.select(root).where(builder.equal(root.get("usuario"), usuario.getId()));

			Query<Carrinho> q = session.createQuery(query);

			if (q.getResultList().size() > 0) {
				for (Carrinho car : q.getResultList()) {
					for (CarrinhoItem item : car.getItens()) {
						Hibernate.initialize(item);
						Hibernate.initialize(item.getProduto());
					}
				}

				return q.getResultList().get(0);
			} else {
				return new Carrinho();
			}

		} catch (HibernateException ex) {
			throw ex;
		} catch (Exception ex) {
			throw ex;
		} finally {
			session.close();
		}

	}

	public void voltarParaOMenu() {

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("Home" + Constantes.EXTENSAO_JSF);
		} catch (IOException e) {
			e.printStackTrace();
		}

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

	public List<SelectItem> getComboTipoFormaPagamento() {
		return Combos.getComboTipoFormaPagamento();
	}

	public List<SelectItem> getComboParcelas() {
		return Combos.getComboTipoCondicaoPagamento();
	}

	public Carrinho getCarrinho() {
		return carrinho;
	}

	public void setCarrinho(Carrinho carrinho) {
		this.carrinho = carrinho;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public double getPrecoTotal() throws MensagemException {
		double precoTotal = 0.0;
		usuarioEnderecoEscolhido = new UsuarioDAO().getEnderecoPorString(enderecoEscolhido, usuario);

		if (carrinho.getItens() != null && carrinho.getItens().size() > 0) {
			for (CarrinhoItem item : carrinho.getItens()) {
				precoTotal += item.getQuantidade() * item.getProduto().getPreco();
			}
		}

		if (usuarioEnderecoEscolhido != null) {
			precoTotal += usuarioEnderecoEscolhido.getPrecoFrete();
		} else {
			throw new MensagemException(Mensagens.getMensagem("Escolha um endereço!"));
		}

		return precoTotal;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public double getPrecoParcial(CarrinhoItem item) {
		precoParcial = item.getQuantidade() * item.getProduto().getPreco();
		return precoParcial;
	}

	public void setPrecoParcial(double precoParcial) {
		this.precoParcial = precoParcial;
	}

	public List<String> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<String> enderecos) {
		this.enderecos = enderecos;
	}

	public String getEnderecoEscolhido() {
		return enderecoEscolhido;
	}

	public void setEnderecoEscolhido(String enderecoEscolhido) {
		this.enderecoEscolhido = enderecoEscolhido;
	}

	public TipoFormaPagamento getFormaDePagamento() {
		return formaDePagamento;
	}

	public void setFormaDePagamento(TipoFormaPagamento formaDePagamento) {
		this.formaDePagamento = formaDePagamento;
	}

	public boolean isCredito() {
		return formaDePagamento == TipoFormaPagamento.Credito;
	}

	public UsuarioEndereco getUsuarioEnderecoEscolhido() {
		return usuarioEnderecoEscolhido;
	}

	public void setUsuarioEnderecoEscolhido(UsuarioEndereco usuarioEnderecoEscolhido) {
		this.usuarioEnderecoEscolhido = usuarioEnderecoEscolhido;
	}

	public double getPrecoFrete() {
		precoFrete = new UsuarioDAO().getEnderecoPorString(enderecoEscolhido, usuario).getPrecoFrete();
		return precoFrete;
	}

	public void setPrecoFrete(double precoFrete) {
		this.precoFrete = precoFrete;
	}

}
