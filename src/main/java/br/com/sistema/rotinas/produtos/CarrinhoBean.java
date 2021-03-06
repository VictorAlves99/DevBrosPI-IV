package br.com.sistema.rotinas.produtos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
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
import br.com.sistema.enuns.TipoCondicaoPagamento;
import br.com.sistema.enuns.TipoFormaPagamento;
import br.com.sistema.enuns.TipoOrigemArquivoBinario;
import br.com.sistema.enuns.TipoStatusVenda;
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
import br.com.sistema.rotinas.vendas.VendaDAO;
import br.com.sistema.util.Caracter;
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

	private CarrinhoItem carrinhoItem;
	private String cep;

	private List<CarrinhoItem> itens = new ArrayList<>();

	private String logradouro;

	private String bairro;

	private String cidade;

	private String uf;

	private String numero;

	private String enderecoCompleto;

	public CarrinhoBean() {
		enderecos = carregarEnderecos();
		enderecoEscolhido = enderecos.get(0);
		usuarioEnderecoEscolhido = new UsuarioDAO().getEnderecoPorString(enderecoEscolhido, usuario);
		
		carrinho = carregaCarrinho();
		itens = carrinho.getItens();
		
		if (itens != null && itens.size() > 0) {
			for (CarrinhoItem i : itens) {
				carregarImagemProduto(i.getProduto());
			}
		} else {
			itens = new ArrayList<CarrinhoItem>();
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

	public void removeItem(CarrinhoItem item) throws Exception {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			this.carrinhoItem = new ProdutosDAO().getCarrinhoItem(item);
			
			session.delete(this.carrinhoItem);
			
			itens.remove(this.carrinhoItem);
			
			tx.commit();
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("Carrinho" + Constantes.EXTENSAO_JSF);

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
		Transaction tx;
		try {
			tx = session.beginTransaction();
			if(formaDePagamento == TipoFormaPagamento.NaoDefinida) 
				throw new MensagemException(Mensagens.getMensagem("Selecione uma forma de pagamento!"));
			
			if(carrinho.getItens().size() <= 0) 
				throw new MensagemException(Mensagens.getMensagem("Não foi possível finalizar a compra. Carrinho não contém itens!"));
				
			usuarioEnderecoEscolhido = new UsuarioDAO().getEnderecoPorString(enderecoEscolhido, usuario);

			venda.setPrecoTotal(this.getPrecoTotal());
			venda.setCliente(this.usuario);
			venda.setFormaPagamento(formaDePagamento);
			
			if(formaDePagamento != TipoFormaPagamento.Credito)
				venda.setParcelas(TipoCondicaoPagamento.AVista);
			
			venda.setEndereco(usuarioEnderecoEscolhido);
			venda.setStatus(TipoStatusVenda.AguardandoPagamento);

			if (venda.getId() > 0) {
				new VendaDAO().update(venda);
				session.flush();
			} else {
				new VendaDAO().save(venda);
				session.flush();
			}

			List<CarrinhoItem> lista = new ProdutosDAO().getItensDoCarrinho(carrinho);
			
			for (CarrinhoItem c : lista) {
				c = new ProdutosDAO().getCarrinhoItem(c);
				Produto p = new ProdutosDAO().load(c.getProduto().getId());
				int quantidadeAposRemover = p.getQuantidade() - c.getQuantidade();
				p.setQuantidade(quantidadeAposRemover);
				if (p.getQuantidade() >= 0) {
					session.update(p);
					session.flush();
				} else {
					throw new MensagemException(Mensagens
							.getMensagem("Não temos o produto " + p.getNome() + " nesta quantidade em estoque!"));
				}

				c.setCarrinho(null);
				c.setVenda(venda);
				session.update(c);
				session.flush();
			}
			
			tx.commit();
			
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

	public void cadastrarEndereco() throws Exception {

		Session session = HibernateUtil.getSession();

		Transaction tx = null;

		try {
			UsuarioEndereco endereco = new UsuarioEndereco();

			String precoDols1 = Caracter.getRandomIntRes(1);
			String precoDols2 = Caracter.getRandomInt(1);
			String precoCents = Caracter.getRandomInt(2);
			String preco = precoDols1 + precoDols2 + "." + precoCents;
			
			if(this.cep != null) {
				if(this.numero == null)
					this.numero = "sem nº";
				
				enderecoCompleto = buscarCep(this.cep);
			}else {
				throw new MensagemException(Mensagens.getMensagem("Digite o CEP corretamente"));
			}
			
			endereco.setPrecoFrete(Double.parseDouble(preco));
			endereco.setUsuario(this.usuario);
			endereco.setEndereco(enderecoCompleto);
			
			tx = session.beginTransaction();

			session.save(endereco);

			session.flush();

			tx.commit();

			FacesContext.getCurrentInstance().getExternalContext().redirect("Carrinho" + Constantes.EXTENSAO_JSF);
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
	
	public void buscarCEP() throws MensagemException {
		if(this.cep != null) {
			if(this.numero == null)
				this.numero = "sem nº";
				
			enderecoCompleto = buscarCep(this.cep);
		}else {
			throw new MensagemException(Mensagens.getMensagem("Digite o CEP corretamente."));
		}
	}
	
	public String buscarCep(String cep) {
        String json;        

        try {
            URL url = new URL("http://viacep.com.br/ws/"+ cep +"/json");
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder jsonSb = new StringBuilder();

            br.lines().forEach(l -> jsonSb.append(l.trim()));
            json = jsonSb.toString();
            
            json = json.replaceAll("[{},:]", "");
            json = json.replaceAll("\"", "\n");                       
            String array[] = new String[30];
            array = json.split("\n");
                             
            logradouro = array[7];            
            bairro = array[15];
            cidade = array[19]; 
            uf = array[23];
            
            logradouro = new String(logradouro.getBytes(), "UTF-8");
            bairro = new String(bairro.getBytes(), "UTF-8");
            cidade = new String(cidade.getBytes(), "UTF-8");
            uf = new String(uf.getBytes(), "UTF-8");
            
            return logradouro + "," + numero + " - " + bairro + "," + cidade + "," + uf;
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

	public CarrinhoItem getCarrinhoItem() {
		return carrinhoItem;
	}

	public void setCarrinhoItem(CarrinhoItem carrinhoItem) {
		this.carrinhoItem = carrinhoItem;
	}

	public List<CarrinhoItem> getItens() {
		return itens;
	}

	public void setItens(List<CarrinhoItem> itens) {
		this.itens = itens;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getEnderecoCompleto() {
		return enderecoCompleto;
	}

	public void setEnderecoCompleto(String enderecoCompleto) {
		this.enderecoCompleto = enderecoCompleto;
	}

}
