/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atus.financeiro.controller;

import br.com.atus.financeiro.dao.EmprestimoColaboradorDAO;
import br.com.atus.financeiro.modelo.EmprestimoColaborador;
import br.com.atus.interfaces.Controller;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author ari
 */
@Stateless
public class EmprestimoColaboradorController extends Controller<EmprestimoColaborador, Long> implements Serializable {

    @Inject
    private EmprestimoColaboradorDAO dao;

    @PostConstruct
    @Override
    protected void inicializaDAO() {
        setDAO(dao);
    }

    public List<EmprestimoColaborador> consultarEmprestimos(String valor) throws Exception {
        return consultarTodos("dataEmprestimo", "colaborador.pessoa.nome", valor);
    }

    public void efetuarPagamento(EmprestimoColaborador ec, BigDecimal valor, Date dataDePagamento) throws Exception {
        EmprestimoColaborador ecl = new EmprestimoColaborador();
        if (ec.getValor().compareTo(valor) == 0) {
            ec.setDataPagamento(dataDePagamento);
           
        }else if(ec.getValor().compareTo(valor) > 0){
            ecl.setAtivo(true);
            ecl.setColaborador(ec.getColaborador());
            ecl.setDataEmprestimo(ec.getDataEmprestimo());
            ecl.setObservacao("Restante do emprestimo de id: ".concat(ec.getId().toString()));
            ecl.setValor(ec.getValor().subtract(valor));
            
            ec.setValor(valor);
            ec.setDataPagamento(dataDePagamento);
        
            salvar(ecl);
        }else{
            throw new Exception("Valor maior que o devido!");
        }
        
        atualizar(ec);
    }

}
