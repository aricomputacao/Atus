/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atus.financeiro.dao;

import br.com.atus.financeiro.modelo.EmprestimoColaborador;
import br.com.atus.util.dao.DAO;
import java.io.Serializable;
import javax.ejb.Stateless;

/**
 *
 * @author ari
 */
@Stateless
public class EmprestimoColaboradorDAO extends DAO<EmprestimoColaborador, Long> implements Serializable{

    public EmprestimoColaboradorDAO() {
        super(EmprestimoColaborador.class);
    }
    
}
