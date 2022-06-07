package br.com.repository;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import br.com.entidades.Estados;
import br.com.entidades.Pessoa;

@Named
public class IDaoPessoaImpl implements IDaoPessoa, Serializable {
	

	private static final long serialVersionUID = 1L;
	@Inject
	private EntityManager entityManager;

	@Override
	public Pessoa consultarUsuario(String login, String senha) {
		
		Pessoa pessoa = null;
		
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		try {
			pessoa = (Pessoa) entityManager.createQuery("select p from Pessoa p where p.login = '" + login + "' and p.senha = '" + senha + "'")
					.getSingleResult();
			
		} catch (NoResultException e) { /* Tratamento se não encontrar usuário com login e senha*/
			// handle exception
		}
		
		entityTransaction.commit();
		
		return pessoa;
	}

	@Override
	public List<SelectItem> listaEstados() {
		
		List<SelectItem> selectItems = new ArrayList<SelectItem>();
				
		
		@SuppressWarnings("unchecked")
		List<Estados> estados = entityManager.createQuery("from Estados").getResultList();
		
		for (Estados estado : estados) {
			selectItems.add(new SelectItem(estado, estado.getNome()));
		}
		
		return selectItems;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> consultaLimit10() {
		List<Pessoa> lista = null;
		
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		lista = entityManager.createQuery("from Pessoa order by id desc ").setMaxResults(10).getResultList();
		
		entityTransaction.commit();

		return lista;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> relatorioPessoa(String nome, Date dataIni, Date dataFin) {
		
		List<Pessoa> pessoas = new ArrayList<Pessoa>();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select l from Pessoa l ");
		
		if(dataIni == null && dataFin == null && nome != null && !nome.isEmpty()) {
			sql.append(" where upper(l.nome) like '%").append(nome.trim().toUpperCase()).append("%'");
			
		}else if (nome == null || (nome !=null && nome.isEmpty()) && dataIni != null && dataFin == null) {
			
			String dataIniString = new SimpleDateFormat("yyyy-MM-dd").format(dataIni);
			sql.append(" where l.dataNascimento >= '").append(dataIniString).append("'");
			
		}else if (nome == null || (nome !=null && nome.isEmpty()) && dataIni == null && dataFin != null) {
			
			String dataFinString = new SimpleDateFormat("yyyy-MM-dd").format(dataFin);
			sql.append(" where l.dataNascimento <= '").append(dataFinString).append("'");
			
		}else if(nome == null || (nome !=null && nome.isEmpty()) && dataIni != null && dataFin != null ) {
			String dataIniString = new SimpleDateFormat("yyyy-MM-dd").format(dataIni);
			String dataFinString = new SimpleDateFormat("yyyy-MM-dd").format(dataFin);
			
			sql.append(" where l.dataNascimento >= '").append(dataIniString).append("' ");
			sql.append(" and l.dataNascimento <= '").append(dataFinString).append("' ");
			
		}else if(nome !=null && !nome.isEmpty() && dataIni != null && dataFin != null ) {
			String dataIniString = new SimpleDateFormat("yyyy-MM-dd").format(dataIni);
			String dataFinString = new SimpleDateFormat("yyyy-MM-dd").format(dataFin);
			
			sql.append(" where l.dataNascimento >= '").append(dataIniString).append("' ");
			sql.append(" and l.dataNascimento <= '").append(dataFinString).append("' ");
			sql.append(" and upper(l.nome) like '%").append(nome.trim().toUpperCase()).append("%'");
		}
		
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
	
		pessoas = entityManager.createQuery(sql.toString()).getResultList();
		transaction.commit();
		
		return pessoas;
	}

}
