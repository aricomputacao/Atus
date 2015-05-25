/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atus.financeiro.managedbean;

import br.com.atus.cadastro.controller.AdvogadoController;
import br.com.atus.cadastro.controller.ColaboradorController;
import br.com.atus.financeiro.controller.ContaReceberController;
import br.com.atus.financeiro.controller.CooptacaoController;
import br.com.atus.financeiro.controller.ParcelaReceberController;
import br.com.atus.financeiro.dto.ContaReceberParcelasDTO;
import br.com.atus.financeiro.modelo.ContaReceber;
import br.com.atus.financeiro.modelo.Cooptacao;
import br.com.atus.financeiro.modelo.ParcelasReceber;
import br.com.atus.financeiro.modelo.Recibo;
import br.com.atus.modelo.Advogado;
import br.com.atus.modelo.Colaborador;
import br.com.atus.modelo.Processo;
import br.com.atus.util.AssistentedeRelatorio;
import br.com.atus.util.MenssagemUtil;
import br.com.atus.util.RelatorioSession;
import br.com.atus.util.managedbean.BeanGenerico;
import br.com.atus.util.managedbean.NavegacaoMB;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

/**
 *
 * @author ari
 */
@ManagedBean
@ViewScoped
public class ContaReceberMB extends BeanGenerico<ContaReceber> implements Serializable {

    @Inject
    private NavegacaoMB navegacaoMB;
    @EJB
    private ContaReceberController controller;
    @EJB
    private AdvogadoController advogadoController;
    @EJB
    private CooptacaoController cooptacaoController;
    @EJB
    private ColaboradorController colaboradorController;
    @EJB
    private ParcelaReceberController parcelaReceberController;

    private List<ContaReceber> listaContaReceber;
    private ContaReceber contaReceber;
    private ParcelasReceber parcelasReceber;
    private Recibo recibo;
    private ContaReceberParcelasDTO contaReceberParcelaDTO;
    private List<Advogado> listaDeAdvogados;
    private List<Processo> listaDeProcessos;
    private List<Colaborador> listaDeColaboradores;
    private List<Cooptacao> listaDeCooptacao;
    private List<ContaReceberParcelasDTO> listaContaReceberParcelasDTOs;
    private BigDecimal valorPagamento;

    @PostConstruct
    public void init() {
        try {
            contaReceber = (ContaReceber) navegacaoMB.getRegistroMapa("conta_receber", new ContaReceber());
            contaReceberParcelaDTO = new ContaReceberParcelasDTO();
            parcelasReceber = new ParcelasReceber();
            recibo = new Recibo();
            listaContaReceber = new ArrayList<>();
            listaDeAdvogados = advogadoController.consultarTodos("nome");
            listaDeCooptacao = cooptacaoController.consultarTodos("nome");
            listaDeProcessos = new ArrayList<>();
            listaDeColaboradores = colaboradorController.consultarTodos("nome");
            listaContaReceberParcelasDTOs = new ArrayList<>();
            valorPagamento = BigDecimal.ZERO;
            if (contaReceber.getId() == null) {
                contaReceber.setDataCadastro(new Date());
                contaReceber.setQuantidadeParcelas(1);
            }
        } catch (Exception ex) {
            Logger.getLogger(ContaReceberMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void salvar() {
        try {
            controller.addContasReceber(contaReceber);
            init();
            MenssagemUtil.addMessageInfo(NavegacaoMB.getMsg("salvar", MenssagemUtil.MENSAGENS));
        } catch (Exception ex) {
            MenssagemUtil.addMessageErro(NavegacaoMB.getMsg("falha", MenssagemUtil.MENSAGENS));
            Logger.getLogger(ContaReceberMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fazerPagamento() {
        try {
            recibo = parcelaReceberController.fazerPagamento(contaReceberParcelaDTO, parcelasReceber, valorPagamento);
            imprimirRecibo();
            consultarTodasContasReceberDoCliente();
            MenssagemUtil.addMessageInfo(NavegacaoMB.getMsg("pagamento_sucesso", MenssagemUtil.MENSAGENS));
            valorPagamento = BigDecimal.ZERO;
            parcelasReceber = new ParcelasReceber();
        } catch (Exception ex) {
            consultarTodasContasReceberDoCliente();
            valorPagamento = BigDecimal.ZERO;
            MenssagemUtil.addMessageErro(NavegacaoMB.getMsg("pagamento_falha", MenssagemUtil.MENSAGENS));
            Logger.getLogger(ContaReceberMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void estornarPagamento(ParcelasReceber pr) {
        try {
            parcelasReceber = pr;
            parcelaReceberController.estornarPagamento(pr);
            MenssagemUtil.addMessageInfo(NavegacaoMB.getMsg("pagamento_estorno_sucesso", MenssagemUtil.MENSAGENS));

        } catch (Exception ex) {
            MenssagemUtil.addMessageErro(NavegacaoMB.getMsg("pagamento_estorno_falha", MenssagemUtil.MENSAGENS));
            Logger.getLogger(ContaReceberMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setarContaParcelaDTO(ContaReceberParcelasDTO dto, ParcelasReceber pr) {
        this.contaReceberParcelaDTO = dto;
        this.parcelasReceber = pr;
        this.valorPagamento = pr.getValorParcela();
    }

    public boolean renderBtnPagar(ParcelasReceber pr) {
        return pr.getDataPagamento() == null;
    }

    public void consultarTodasContasReceberDoCliente() {
        listaContaReceberParcelasDTOs = controller.consultarTodasContasReceberAbertasDo(getValorBusca());

    }

    public void imprimirRecibo() {
        if (!recibo.getListaDeParcelasReceber().isEmpty()) {
            Map<String, Object> m = new HashMap<>();

            m.put("nome_cliente", recibo.getNomeCliente());
            m.put("adv_rec",recibo.getAdivogadoQueRecebeu());
            m.put("data_pagamento",recibo.getDataDePAgamento());
            m.put("valor_extenso", recibo.getValorTotalExtenso());
            m.put("valor_total", recibo.getValorTotal());
            m.put("id_processo", recibo.getIdDoProcesso());
            m.put("id_recibo", recibo.getId());
            
            byte[] rel = new AssistentedeRelatorio().relatorioemByte(recibo.getListaDeParcelasReceber(), m, "WEB-INF/relatorios/recibo.jasper", "Recibo de Pagamento");
            RelatorioSession.setBytesRelatorioInSession(rel);
        }

    }

    public void validarValorDoPagamento(FacesContext context, UIComponent component,
            Object value) throws ValidatorException {
        BigDecimal vl = (BigDecimal) value;
        if (vl.compareTo(contaReceberParcelaDTO.getTotalAberto()) > 0) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso: ",
                    NavegacaoMB.getMsg("pagamento_invalido", MenssagemUtil.MENSAGENS)));
        }
        if (vl.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso: ",
                    NavegacaoMB.getMsg("pagamento_igual_zero", MenssagemUtil.MENSAGENS)));
        }
    }

    public ContaReceberMB() {
        super(ContaReceber.class);
    }

    public List<ContaReceber> getListaContaReceber() {
        return listaContaReceber;
    }

    public void setListaContaReceber(List<ContaReceber> listaContaReceber) {
        this.listaContaReceber = listaContaReceber;
    }

    public List<Advogado> getListaDeAdvogados() {
        return listaDeAdvogados;
    }

    public void setListaDeAdvogados(List<Advogado> listaDeAdvogados) {
        this.listaDeAdvogados = listaDeAdvogados;
    }

    public List<Processo> getListaDeProcessos() {
        return listaDeProcessos;
    }

    public void setListaDeProcessos(List<Processo> listaDeProcessos) {
        this.listaDeProcessos = listaDeProcessos;
    }

    public List<Colaborador> getListaDeColaboradores() {
        return listaDeColaboradores;
    }

    public void setListaDeColaboradores(List<Colaborador> listaDeColaboradores) {
        this.listaDeColaboradores = listaDeColaboradores;
    }

    public ContaReceber getContaReceber() {
        return contaReceber;
    }

    public void setContaReceber(ContaReceber contaReceber) {
        this.contaReceber = contaReceber;
    }

    public List<Cooptacao> getListaDeCooptacao() {
        return listaDeCooptacao;
    }

    public List<ContaReceberParcelasDTO> getListaContaReceberParcelasDTOs() {
        return listaContaReceberParcelasDTOs;
    }

    public ContaReceberParcelasDTO getContaReceberParcelaDTO() {
        return contaReceberParcelaDTO;
    }

    public void setContaReceberParcelaDTO(ContaReceberParcelasDTO contaReceberParcelaDTO) {
        this.contaReceberParcelaDTO = contaReceberParcelaDTO;
    }

    public ParcelasReceber getParcelasReceber() {
        return parcelasReceber;
    }

    public void setParcelasReceber(ParcelasReceber parcelasReceber) {
        this.parcelasReceber = parcelasReceber;
    }

    public BigDecimal getValorPagamento() {
        return valorPagamento;
    }

    public void setValorPagamento(BigDecimal valorPagamento) {
        this.valorPagamento = valorPagamento;
    }

}
