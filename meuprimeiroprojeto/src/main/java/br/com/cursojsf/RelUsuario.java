package br.com.cursojsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.dao.DaoGenerico;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoPessoa;

@ViewScoped
@Named(value = "relUsuario")
public class RelUsuario implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Date dataIni;
	private Date dataFin;
	
	private String nome;
	
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	
	@Inject
	private IDaoPessoa daoPessoa;
	
	@Inject
	private DaoGenerico<Pessoa> daoGenerico;
	
	
	public void buscarUsuarios() {
		if(dataIni == null && dataFin == null && (nome == null || nome == "" )) {
			
			pessoas = daoGenerico.getListEntity(Pessoa.class);
			
		}else {
			pessoas = daoPessoa.relatorioPessoa(nome, dataIni, dataFin);
		}
	}
		

	public Date getDataIni() {
		return dataIni;
	}


	public void setDataIni(Date dataIni) {
		this.dataIni = dataIni;
	}



	public Date getDataFin() {
		return dataFin;
	}



	public void setDataFin(Date dataFin) {
		this.dataFin = dataFin;
	}



	public String getNome() {
		return nome;
	}



	public void setNome(String nome) {
		this.nome = nome;
	}



	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}

	
	
}
