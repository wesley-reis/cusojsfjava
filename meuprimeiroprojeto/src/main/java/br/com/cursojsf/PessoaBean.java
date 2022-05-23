package br.com.cursojsf;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.dao.DaoGenerico;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoPessoa;
import br.com.repository.IDaoPessoaImpl;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean {

	private Pessoa pessoa = new Pessoa();
	private DaoGenerico<Pessoa> daoGenerico = new DaoGenerico<Pessoa>();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	
	private IDaoPessoa idaoPessoa = new IDaoPessoaImpl();
	
	public String salvar() {
		pessoa = daoGenerico.merge(pessoa);
		carregarPessoas();
		return "";
	}
	
	public String novo() {
		pessoa = new Pessoa();
		return "";
	}
	
	public String remove() {
		daoGenerico.delete(pessoa);
		carregarPessoas();
		pessoa = new Pessoa();
		return "";
	}
	
	@PostConstruct
	public void carregarPessoas() {
		pessoas = daoGenerico.getListEntity(Pessoa.class);
	}
	
	public List<Pessoa> getPessoas() {
		return pessoas;
	}


	public Pessoa getPessoa() {
		return pessoa;
	}


	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	
	public String logar() {
			
		Pessoa pessoaUser = idaoPessoa.consultarUsuario(pessoa.getLogin(), pessoa.getSenha());
		
		if (pessoaUser != null) {
			//adicionar usuario na sessão usuarioLogado
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			
			HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();
			HttpSession session = req.getSession();
			
			session.setAttribute("usuarioLogado", pessoaUser);
				
			
			return "primeirapagina.jsf";
		}
		
		return "index.jsf";
	}
	
	public boolean permitirAcesso(String acesso) {
			
		//recuperar usuario na sessão usuarioLogado
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		
				
		HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();
		HttpSession session = req.getSession();
		
		Pessoa pessoaUser = (Pessoa) session.getAttribute("usuarioLogado");
			
		
		return pessoaUser.getPerfilUser().equals(acesso);
	}
	

}
