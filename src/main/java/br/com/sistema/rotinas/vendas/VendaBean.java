package br.com.sistema.rotinas.vendas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.sistema.enuns.TipoCondicaoPagamento;
import br.com.sistema.exceptions.MensagemException;
import br.com.sistema.jsf.FacesUtil;
import br.com.sistema.jsf.Mensagens;
import br.com.sistema.rotinas.usuario.Usuario;
import br.com.sistema.util.ParametrosSistema;

@ManagedBean(name = "vendaBean")
@ViewScoped
public class VendaBean implements Serializable {

	private static final long serialVersionUID = 3156773698941473780L;

	private Venda objeto;
	private List<Venda> vendas = new ArrayList<>();
	private Date dataInicio;
	private Date dataFim;
	private final TipoCondicaoPagamento AVista = TipoCondicaoPagamento.AVista;
	private Usuario usuario = ParametrosSistema.getUsuarioLogado();

	public VendaBean() {
		
	}
	
	public void pesquisarVendas(){
		try {
			this.vendas = new VendaDAO().getListaPorData(dataInicio, dataFim, usuario);
		} catch (MensagemException e) {
			Mensagens.gerarMensagemGenerica(e.getMessage());
		}
	}
	
	public void abrirVenda(Venda venda) {
		
		this.objeto = new VendaDAO().loadVenda(venda.getId(), venda.getNumeroDaVenda());

		FacesUtil.addCallbackParam("showDialog", true);
		
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
	
}
