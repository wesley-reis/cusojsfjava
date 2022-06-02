package br.com.cursojsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.dao.DaoGenerico;
import br.com.entidades.Lancamento;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoLancamento;

@ViewScoped
@Named(value = "lancamentoBean")
public class LancamentoBean implements Serializable {
	

	private static final long serialVersionUID = 1L;
	private Lancamento lancamento = new Lancamento();	
	private List<Lancamento> lancamentos = new ArrayList<Lancamento>();
	
	@Inject
	private DaoGenerico<Lancamento> daoGenerico;
	
	@Inject
	private IDaoLancamento daoLancamento;
	
	
	
	public String salvar() {
		
		//recuperar usuario na sessão usuarioLogado
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		
				
		HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();
		HttpSession session = req.getSession();
		
		Pessoa pessoaUser = (Pessoa) session.getAttribute("usuarioLogado");
		
		lancamento.setUsuario(pessoaUser);
		lancamento = daoGenerico.merge(lancamento);
		
		carregarLancamentos();
		
		mostrarMsg("Cadastrado com sucesso!");
		
		return "";
	}
	
	
	private void mostrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);

	}
	
	@PostConstruct
	private void carregarLancamentos(){
		//recuperar usuario na sessão usuarioLogado
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		
		HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();
		HttpSession session = req.getSession();
		
		Pessoa pessoaUser = (Pessoa) session.getAttribute("usuarioLogado");
		
		lancamentos = daoLancamento.consultar(pessoaUser.getId());
	}
	
	public String novo() {
		lancamento = new Lancamento();
		
		return "";
	}
	
	public String limpar() {
		lancamento = new Lancamento();
		
		return "";
	}
	
	public String remove() {
		daoGenerico.delete(lancamento);
		lancamento = new Lancamento();
		carregarLancamentos();
		
		mostrarMsg("removido com sucesso!");
		return "";
	}
	
	
	public Lancamento getLancamento() {
		return lancamento;
	}
	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}
	public DaoGenerico<Lancamento> getDaoGenerico() {
		return daoGenerico;
	}
	public void setDaoGenerico(DaoGenerico<Lancamento> daoGenerico) {
		this.daoGenerico = daoGenerico;
	}
	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}
	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}
	
	

}
