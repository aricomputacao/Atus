/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atus.financeiro.managedbean;

import br.com.atus.cadastro.modelo.Colaborador;
import br.com.atus.financeiro.controller.EmprestimoColaboradorController;
import br.com.atus.financeiro.modelo.EmprestimoColaborador;
import br.com.atus.util.MenssagemUtil;
import br.com.atus.util.managedbean.BeanGenerico;
import br.com.atus.util.managedbean.NavegacaoMB;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    private Colaborador colaborador;
    private EmprestimoColaborador emprestimoColaborador;
    private List<EmprestimoColaborador> listaEmprestimoColaborador;
    private List<EmprestimoColaborador> listaEmprestimoColaboradorFechados;
    private Date dataDePagamento;
    private BigDecimal valor;

    public EmprestimoColaboradorMB() {
        super(EmprestimoColaborador.class);
    }

    @PostConstruct
    public void init() {
        try {
            dataDePagamento = new Date();
            valor = BigDecimal.ZERO;
            emprestimoColaborador = (EmprestimoColaborador) navegacaoMB.getRegistroMapa("emprestimo_colaborador", new EmprestimoColaborador());
            if (emprestimoColaborador.getId() == null) {
                colaborador = new Colaborador();
                emprestimoColaborador.setValor(BigDecimal.ZERO);
                emprestimoColaborador = new EmprestimoColaborador();
                emprestimoColaborador.setAtivo(true);
                emprestimoColaborador.setDataEmprestimo(new Date());

            } else {
                colaborador = emprestimoColaborador.getColaborador();
            }
            listaEmprestimoColaborador = new ArrayList<>();
        } catch (Exception ex) {
            Logger.getLogger(EmprestimoColaboradorMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void salvar() {
        try {
            emprestimoColaborador.setColaborador(colaborador);
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
            consultar();
            MenssagemUtil.addMessageInfo(NavegacaoMB.getMsg("excluir", MenssagemUtil.MENSAGENS));
        } catch (Exception ex) {
            MenssagemUtil.addMessageErro(NavegacaoMB.getMsg("excluir.falha", MenssagemUtil.MENSAGENS));
            Logger.getLogger(EmprestimoColaboradorMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setarEmprestimoColaborador(EmprestimoColaborador ec){
        emprestimoColaborador = ec;
        consultar();
    }
    
    public void finalizarEmprestimo() {
        try {
            emprestimoColaboradorController.efetuarPagamento(emprestimoColaborador,valor,dataDePagamento);
            consultar();
            MenssagemUtil.addMessageInfo(NavegacaoMB.getMsg("salvar", MenssagemUtil.MENSAGENS));

        } catch (Exception ex) {
            MenssagemUtil.addMessageErro(NavegacaoMB.getMsg("falha", MenssagemUtil.MENSAGENS).concat(" " + ex.getMessage()), ex, "Emprestimo");
            Logger.getLogger(EmprestimoColaboradorMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void consultar() {
        try {
            listaEmprestimoColaborador = emprestimoColaboradorController.consultarEmprestimosAbertos(colaborador.getNome());
            listaEmprestimoColaboradorFechados = emprestimoColaboradorController.consultarEmprestimosFechados(colaborador.getNome());
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

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    public Date getDataDePagamento() {
        return dataDePagamento;
    }

    public void setDataDePagamento(Date dataDePagamento) {
        this.dataDePagamento = dataDePagamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public List<EmprestimoColaborador> getListaEmprestimoColaboradorFechados() {
        return listaEmprestimoColaboradorFechados;
    }

    
    
}
