package br.com.cursojsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.dao.DaoGenerico;
import br.com.entidades.Lancamento;
import br.com.repository.IDaoLancamento;

@ViewScoped
@Named(value = "relLancamento")
public class RelLancamento implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date dataIni;
	private Date dataFin;

	private String numNota;

	@Inject
	private IDaoLancamento daoLancamento;


	private List<Lancamento> lancamentos = new ArrayList<Lancamento>();

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

	public String getNumNota() {
		return numNota;
	}

	public void setNumNota(String numNota) {
		this.numNota = numNota;
	}

	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void buscarLancamento() {
		if(dataIni == null && dataFin == null && (numNota == null || numNota == "" )) {
			
			lancamentos = daoLancamento.consultarAll();
			
		}else {
			lancamentos = daoLancamento.relatorioLancamento(numNota, dataIni, dataFin);
		}
		
	}
}
