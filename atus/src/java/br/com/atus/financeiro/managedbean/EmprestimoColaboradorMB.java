/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atus.financeiro.managedbean;

import br.com.atus.cadastro.modelo.Colaborador;
import br.com.atus.cadastro.modelo.Pessoa;
import br.com.atus.financeiro.controller.EmprestimoColaboradorController;
import br.com.atus.financeiro.modelo.EmprestimoColaborador;
import br.com.atus.util.MenssagemUtil;
import br.com.atus.util.managedbean.BeanGenerico;
import br.com.atus.util.managedbean.NavegacaoMB;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author ari
 */
@ManagedBean
@ViewScoped
public class EmprestimoColaboradorMB extends BeanGenerico<EmprestimoColaborador> implements Serializable {

    @Inject
    private NavegacaoMB navegacaoMB;
    @EJB
    private EmprestimoColaboradorController emprestimoColaboradorController;

    private EmprestimoColaborador emprestimoColaborador;
    private List<EmprestimoColaborador> listaEmprestimoColaborador;

    public EmprestimoColaboradorMB() {
        super(EmprestimoColaborador.class);
    }

    @PostConstruct
    public void init() {
        try {
            emprestimoColaborador = (EmprestimoColaborador) navegacaoMB.getRegistroMapa("emprestimo_colaborador", new EmprestimoColaborador());

            if (emprestimoColaborador == null) {
                emprestimoColaborador.setColaborador(new Colaborador());
                emprestimoColaborador.getColaborador().setPessoa(new Pessoa());
                emprestimoColaborador.setValor(BigDecimal.ZERO);
                listaEmprestimoColaborador = emprestimoColaboradorController.consultarTodos("dataEmprestimo", "colaborador.pessoa.nome", emprestimoColaborador.getColaborador().getPessoa().getNome());
            } else {
                emprestimoColaborador = new EmprestimoColaborador();
                emprestimoColaborador.setAtivo(true);
                emprestimoColaborador.setDataEmprestimo(new Date());

            }
        } catch (Exception ex) {
            Logger.getLogger(EmprestimoColaboradorMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void salvar() {
        try {
            emprestimoColaboradorController.salvarouAtualizar(emprestimoColaborador);
            init();
            MenssagemUtil.addMessageInfo(NavegacaoMB.getMsg("salvar", MenssagemUtil.MENSAGENS));
        } catch (Exception ex) {
            MenssagemUtil.addMessageErro(NavegacaoMB.getMsg("falha", MenssagemUtil.MENSAGENS).concat(" " + ex.getMessage()), ex, "Emprestimo");
            Logger.getLogger(EmprestimoColaboradorMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void excluir(EmprestimoColaborador ec) {
        try {
            ec = emprestimoColaboradorController.gerenciar(ec.getId());
            emprestimoColaboradorController.excluir(ec);
            listaEmprestimoColaborador.remove(ec);
            MenssagemUtil.addMessageInfo(NavegacaoMB.getMsg("excluir", MenssagemUtil.MENSAGENS));
        } catch (Exception ex) {
            MenssagemUtil.addMessageErro(NavegacaoMB.getMsg("excluir.falha", MenssagemUtil.MENSAGENS));
            Logger.getLogger(EmprestimoColaboradorMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void finalizarEmprestimo(EmprestimoColaborador ec) {
        try {
            ec.setDataPagamento(new Date());
            emprestimoColaboradorController.atualizar(ec);
            MenssagemUtil.addMessageInfo(NavegacaoMB.getMsg("salvar", MenssagemUtil.MENSAGENS));

        } catch (Exception ex) {
            MenssagemUtil.addMessageErro(NavegacaoMB.getMsg("falha", MenssagemUtil.MENSAGENS).concat(" " + ex.getMessage()), ex, "Emprestimo");
            Logger.getLogger(EmprestimoColaboradorMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void consultar() {
        try {
            listaEmprestimoColaborador = emprestimoColaboradorController.consultarEmprestimos(emprestimoColaborador.getColaborador().getPessoa().getNome());
        } catch (Exception ex) {
            Logger.getLogger(EmprestimoColaboradorMB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<EmprestimoColaborador> getListaEmprestimoColaborador() {
        return listaEmprestimoColaborador;
    }

    public EmprestimoColaborador getEmprestimoColaborador() {
        return emprestimoColaborador;
    }

    public void setEmprestimoColaborador(EmprestimoColaborador emprestimoColaborador) {
        this.emprestimoColaborador = emprestimoColaborador;
    }

}
