/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atus.controller;

import br.com.atus.dao.PermissaoDAO;
import br.com.atus.modelo.Modulo;
import br.com.atus.modelo.Permissao;
import br.com.atus.modelo.Tarefa;
import br.com.atus.modelo.Usuario;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author gilmario
 */
@Stateless
public class PermissaoController extends Controller<Permissao, Long> implements Serializable {

    @EJB
    private PermissaoDAO dao;

    @Override
    @PostConstruct
    protected void inicializaDAO() {
        setDAO(dao);
    }

    public List<Permissao> listar(Usuario usuario, Modulo modulo, Tarefa tarefa) {
        return getDAO().listar(usuario, modulo, tarefa);
    }

    public List<Permissao> listar(Usuario usuario) {
        return dao.listar(usuario);
    }

}
