/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atus.dao;

import br.com.atus.modelo.Peca;
import java.io.Serializable;
import javax.ejb.Stateless;

/**
 *
 * @author gilmario
 */
@Stateless
public class PecaDAO extends DAO<Peca, Long> implements Serializable {

    public PecaDAO() {
        super(Peca.class);
    }

}
