package br.com.sistema.rotinas.vendas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.hibernate.HibernateException;

import br.com.sistema.enuns.TipoCondicaoPagamento;
import br.com.sistema.enuns.TipoStatusVenda;
import br.com.sistema.exceptions.MensagemException;
import br.com.sistema.jsf.FacesUtil;
import br.com.sistema.jsf.Mensagens;
import br.com.sistema.rotinas.usuario.Usuario;
import br.com.sistema.util.Combos;
import br.com.sistema.util.ParametrosSistema;

@ManagedBean(name = "vendaBean")
@ViewScoped
public class VendaBean implements Serializable {

	private static final long serialVersionUID = 3156773698941473780L;

	private Venda objeto;
	private List<Venda> vendas = new ArrayList<>();
	private Date dataInicio;
	private Date dataFim;
	private String textoPesquisa;
	private String status;
	private TipoStatusVenda statusNovo;
	private final TipoCondicaoPagamento AVista = TipoCondicaoPagamento.AVista;
	private Usuario usuario = ParametrosSistema.getUsuarioLogado();

	public VendaBean() {

	}

	public void pesquisarVendas() {
		try {
			if (dataInicio != null && dataFim != null)
				this.vendas = new VendaDAO().getListaPorData(dataInicio, dataFim, usuario, null);
			else
				throw new MensagemException(Mensagens.getMensagem("Preencha as datas corretamente"));
		} catch (MensagemException e) {
			Mensagens.gerarMensagemGenerica(e.getMessage());
		}
	}

	public void pesquisarVendasGerais() {
		try {
			this.vendas = new VendaDAO().getListaPorData(null, null, null,textoPesquisa);
		} catch (MensagemException e) {
			Mensagens.gerarMensagemGenerica(e.getMessage());
		}
	}

	public void abrirVenda(Venda venda) {

		this.objeto = new VendaDAO().loadVenda(venda.getId(), venda.getNumeroDaVenda());

		FacesUtil.addCallbackParam("showDialog", true);

	}

	public void alteraStatus() {

		try {
			statusNovo = TipoStatusVenda.valueOf(status);
			
			this.objeto.setStatus(statusNovo);
			new VendaDAO().update(this.objeto);
			
			Mensagens.gerarMensagemGenerica("Status atualizado com sucesso");
		} catch (HibernateException e) {
			Mensagens.gerarMensagemException(e);
		} catch (MensagemException e) {
			Mensagens.gerarMensagemException(e);
		} catch (Exception e) {
			Mensagens.gerarMensagemException(e);
		}

	}

	public List<SelectItem> getComboStatus() {
		return Combos.getComboStatus();
	}

	public Venda getObjeto() {
		return objeto;
	}

	public void setObjeto(Venda objeto) {
		this.objeto = objeto;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Venda> getVendas() {
		return vendas;
	}

	public void setVendas(List<Venda> vendas) {
		this.vendas = vendas;
	}

	public TipoCondicaoPagamento getAVista() {
		return AVista;
	}

	public TipoStatusVenda getStatusNovo() {
		return statusNovo;
	}

	public void setStatusNovo(TipoStatusVenda statusNovo) {
		this.statusNovo = statusNovo;
	}

	/**
	 * @return the textoPesquisa
	 */
	public String getTextoPesquisa() {
		return textoPesquisa;
	}

	/**
	 * @param textoPesquisa the textoPesquisa to set
	 */
	public void setTextoPesquisa(String textoPesquisa) {
		this.textoPesquisa = textoPesquisa;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
