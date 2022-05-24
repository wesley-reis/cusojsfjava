package br.com.cursojsf;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

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
	
	
	public void pesquisaCep(AjaxBehaviorEvent event) {
				
		try {
			
			URL url = new URL("https://viacep.com.br/ws/"+pessoa.getCep()+"/json/");
			
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			
			String cep = "";
			StringBuilder jsonCep = new StringBuilder();
			
			while ((cep = br.readLine()) != null) {
				
				jsonCep.append(cep);
				
			}
						
			Pessoa gsonAux = new Gson().fromJson(jsonCep.toString(), Pessoa.class);
			
			pessoa.setCep(gsonAux.getCep());
			pessoa.setLogradouro(gsonAux.getLogradouro());
			pessoa.setComplemento(gsonAux.getComplemento());
			pessoa.setBairro(gsonAux.getBairro());
			pessoa.setLocalidade(gsonAux.getLocalidade());
			pessoa.setUf(gsonAux.getUf());		
			
			
		} catch (Exception e) {
			e.printStackTrace();
			mostrarMsg("Erro ao consultar o cep");
		}
		
	}
	
	public String salvar() {
		pessoa = daoGenerico.merge(pessoa);
		carregarPessoas();
		mostrarMsg("Cadastrado com sucesso!");
		return "";
	}
	
	private void mostrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
		
	}

	public String novo() {
		pessoa = new Pessoa();
		return "";
	}
	
	public String limpar() {
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
	
	public Pessoa userLogado() {
		//recuperar usuario na sessão usuarioLogado
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		
		HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();
		HttpSession session = req.getSession();
		
		Pessoa pessoaUser = (Pessoa) session.getAttribute("usuarioLogado");
		
		return pessoaUser;
	}
	

}
