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
        return dao.consultarTodos("dataEmprestimo", "colaborador.pessoa.nome", valor);
    }

}
