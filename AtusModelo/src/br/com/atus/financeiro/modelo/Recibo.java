/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atus.financeiro.modelo;

import br.com.atus.modelo.Advogado;
import br.com.atus.modelo.Usuario;
import br.com.atus.util.FormatadorDeNumeros;
import br.com.atus.util.NumeroPorExtenso;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 * @author ari
 */
@Entity
@Table(name = "recibo", schema = "financeiro")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Recibo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rec_id", nullable = false)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(schema = "financeiro", name = "recibo_parcelas", joinColumns = {
        @JoinColumn(name = "rec_id", referencedColumnName = "rec_id")}, inverseJoinColumns = {
        @JoinColumn(name = "par_id", referencedColumnName = "par_id")})
    private List<ParcelasReceber> listaDeParcelasReceber;

    @ManyToOne
    @JoinColumn(name = "adv_id", referencedColumnName = "adv_id")
    private Advogado advogadoQueRecebeu;
    
   

    @ManyToOne
    @JoinColumn(name = "usr_id", referencedColumnName = "usr_id")
    private Usuario usuarioQueRecebeu;

    @Column(name = "rec_confirmacao_recebimento", columnDefinition = "boolean default false")
    private boolean confirmacaoRecebimento;

    @Column(name = "rec_prestado_conta", columnDefinition = "boolean default false")
    private boolean prestadoConta;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "rec_data_confirmacao")
    private Date dataConfirmacao;

    public Date getDataConfirmacao() {
        return dataConfirmacao;
    }

    public void setDataConfirmacao(Date dataPagamento) {
        this.dataConfirmacao = dataPagamento;
    }
    
    public Advogado getAdvogadoQueRecebeu() {
        return advogadoQueRecebeu;
    }

    public void setAdvogadoQueRecebeu(Advogado advogadoQueRecebeu) {
        this.advogadoQueRecebeu = advogadoQueRecebeu;
    }

    public Usuario getUsuarioQueRecebeu() {
        return usuarioQueRecebeu;
    }

    public void setUsuarioQueRecebeu(Usuario usuarioQueRecebeu) {
        this.usuarioQueRecebeu = usuarioQueRecebeu;
    }

    public boolean isConfirmacaoRecebimento() {
        return confirmacaoRecebimento;
    }

    public void setConfirmacaoRecebimento(boolean confirmacaoRecebimento) {
        this.confirmacaoRecebimento = confirmacaoRecebimento;
    }

    public boolean isPrestadoConta() {
        return prestadoConta;
    }

    public void setPrestadoConta(boolean prestadoConta) {
        this.prestadoConta = prestadoConta;
    }

    public Recibo() {
    }

    public BigDecimal getValorTotal() {
        BigDecimal vlTot = BigDecimal.ZERO;
        for (ParcelasReceber lst : listaDeParcelasReceber) {
            vlTot = vlTot.add(lst.getValorPago());
        }
        return vlTot;
    }

    public String getValorTotalExtenso() {
        NumeroPorExtenso n = new NumeroPorExtenso(true, true, true);
        return n.converteMoeda(getValorTotal());
    }
    
    public BigDecimal getValorDonoDoProcesso() {
        BigDecimal percent = this.listaDeParcelasReceber.get(0).getContaReceber().getCooptacao().getPercentDono().divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.UNNECESSARY);
        return this.getValorTotal().multiply(percent).setScale(2);
    }

    public String getValorTotalFormatado(){
        return FormatadorDeNumeros.converterBigDecimalEmStrng(getValorTotal());
    }

    public String getValorDonoDoProcessoFormatado(){
        return FormatadorDeNumeros.converterBigDecimalEmStrng(getValorDonoDoProcesso());
    }
    public String getValorSocioDoProcessoFormatado(){
        return FormatadorDeNumeros.converterBigDecimalEmStrng(getValorSocioDoProcesso());
    }
    public String getValorDoColaboradorFormatado(){
        return FormatadorDeNumeros.converterBigDecimalEmStrng(getValorDoColaborador());
    }
    
    public BigDecimal getValorSocioDoProcesso() {
        BigDecimal percent = this.listaDeParcelasReceber.get(0).getContaReceber().getCooptacao().getPercentSocio().divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.UNNECESSARY);
        return getValorTotal().multiply(percent).setScale(2);
    }

    public BigDecimal getValorDoColaborador() {
        BigDecimal percent = this.listaDeParcelasReceber.get(0).getContaReceber().getPercentualColaborador().divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.UNNECESSARY);
        return this.getValorTotal().multiply(percent);
    }

    public Long getIdDoProcesso() {
        return listaDeParcelasReceber.get(0).getContaReceber().getProcesso().getId();

    }

    public String getNomeCliente() {
        return listaDeParcelasReceber.get(0).getNomeDoCliente();
    }

    public String getNomeDoAdvogadot(){
        return this.advogadoQueRecebeu.getNome();
    }
    
    public String getNomeDoColaborador(){
       return listaDeParcelasReceber.get(0).getContaReceber().getNomeDoColaborador();
    }
    
    public Date getDataDePagamento() {
        return listaDeParcelasReceber.get(0).getDataPagamento();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addParcela(ParcelasReceber parcelasReceber) {
        listaDeParcelasReceber.add(parcelasReceber);
    }

    public List<ParcelasReceber> getListaDeParcelasReceber() {
        Collections.sort(listaDeParcelasReceber);
        return Collections.unmodifiableList(listaDeParcelasReceber);
    }

    public void setListaDeParcelasReceber(List<ParcelasReceber> listaDeParcelasReceber) {
        this.listaDeParcelasReceber = listaDeParcelasReceber;
    }

   
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Recibo other = (Recibo) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
