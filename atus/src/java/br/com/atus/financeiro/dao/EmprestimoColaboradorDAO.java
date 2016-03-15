/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atus.financeiro.dao;

import br.com.atus.financeiro.modelo.EmprestimoColaborador;
import br.com.atus.util.dao.DAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

/**
 *
 * @author ari
 */
@Stateless
public class EmprestimoColaboradorDAO extends DAO<EmprestimoColaborador, Long> implements Serializable{

    public EmprestimoColaboradorDAO() {
        super(EmprestimoColaborador.class);
    }

    public List<EmprestimoColaborador> consultarAbertos(String nomeColaborador) {
        TypedQuery<EmprestimoColaborador> tq;
        
        tq = getEm().createQuery("SELECT e FROM EmprestimoColaborador e WHERE e.dataPagamento IS NULL and UPPER(e.colaborador.pessoa.nome) like :nome ORDER BY e.colaborador,e.dataEmprestimo", EmprestimoColaborador.class )
                .setParameter("nome","%"+ nomeColaborador.toUpperCase()+"%");
        
        return tq.getResultList().isEmpty() ? new ArrayList<EmprestimoColaborador>() : tq.getResultList();
    }

    public List<EmprestimoColaborador> consultarFechados(String nomeColaborador) {
        TypedQuery<EmprestimoColaborador> tq;
        
        tq = getEm().createQuery("SELECT e FROM EmprestimoColaborador e WHERE e.dataPagamento IS NOT NULL and UPPER(e.colaborador.pessoa.nome) like :nome ORDER BY e.colaborador,e.dataEmprestimo", EmprestimoColaborador.class )
                .setParameter("nome","%"+ nomeColaborador.toUpperCase()+"%");
        
        return tq.getResultList().isEmpty() ? new ArrayList<EmprestimoColaborador>() : tq.getResultList();
    }
    
}
