/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atus.dao;

import br.com.atus.modelo.EspecieEvento;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

/**
 *
 * @author Ari
 */
@Stateless
public class EspecieEventoDAO extends DAO<EspecieEvento, Integer> implements Serializable {

    public EspecieEventoDAO() {
        super(EspecieEvento.class);
    }

    public List<EspecieEvento> consultarTiposParaRelatorio() {
        TypedQuery<EspecieEvento> tq;
        tq = getEm().createQuery("SELECT e FROM EspecieEvento e WHERE e.id = 11 or e.id = 7 or e.id = 8", EspecieEvento.class);

        return tq.getResultList();
    }

}
