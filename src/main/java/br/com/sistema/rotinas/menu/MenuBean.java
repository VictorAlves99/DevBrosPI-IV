package br.com.sistema.rotinas.menu;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

@ManagedBean(name = "menuBean")
@SessionScoped
public class MenuBean implements Serializable {

	private static final long serialVersionUID = -7172536349866782379L;

	private MenuModel model;

	public MenuBean() {
		montarMenu();
	}

	protected void montarMenu() {

		model = new DefaultMenuModel();

		this.criaMenuItem(model, "Dashboard", "/Sistema/Dashboard.htm", "om_dashboard", "fa fa-fw fa-home");
		this.criaMenuItem(model, "Produtos", "/Sistema/Produtos.htm", "om_filmes", "fa fa-fw fa-list-alt");
		this.criaMenuItem(model, "Usuário", "/Sistema/Usuario.htm", "om_usuario", "fa fa-fw fa-user-o");
		
	}

	public void criaMenuItem(MenuModel model, String texto, String endereco, String id, String icone) {

		DefaultMenuItem item = new DefaultMenuItem();
		item.setValue(texto);
		item.setId(id);
		item.setUrl(endereco);
		item.setIcon(icone);

		model.addElement(item);
	}

	public MenuModel getModel() {
		return model;
	}

	public void setModel(MenuModel model) {
		this.model = model;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
