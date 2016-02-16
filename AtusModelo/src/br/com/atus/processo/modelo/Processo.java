/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atus.processo.modelo;

import br.com.atus.cadastro.modelo.Usuario;
import br.com.atus.cadastro.modelo.TipoContrato;
import br.com.atus.cadastro.modelo.Materia;
import br.com.atus.cadastro.modelo.JuizoTribunal;
import br.com.atus.cadastro.modelo.Colaborador;
import br.com.atus.cadastro.modelo.Cliente;
import br.com.atus.cadastro.modelo.Advogado;
import br.com.atus.util.peca.PecaColetor;
import br.com.atus.util.peca.TipoMascara;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author gilmario
 */
@Entity
@Table(name = "processo", schema = "processo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Processo implements Serializable {

    //Adicionr cliente
    //filho conjuge falecido
    //nb deferido string
    @Id
    @Column(name = "pro_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @PecaColetor
    private Long id;

    @ManyToOne
    @JoinColumn(name = "end_id", referencedColumnName = "end_id")
    @PecaColetor(isEntidade = true)
    private Enderecamento enderecamento;

    @ManyToOne
    @JoinColumn(name = "col_id", referencedColumnName = "col_id")
    @PecaColetor(isEntidade = true)
    private Colaborador colaborador;

    @ManyToOne
    @JoinColumn(name = "cli_id", referencedColumnName = "cli_id")
    @PecaColetor(isEntidade = true)
    private Cliente cliente;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @PecaColetor(isLista = true)
    private List<Adversario> adversarios;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @PecaColetor(isLista = true)
    private List<ParteInteressada> parteInteressadas;

    @Column(name = "pro_numero", length = 80)
    @Length(max = 80)
    @PecaColetor
    private String numero;

    @ManyToOne
    @JoinColumn(name = "tpc_id", referencedColumnName = "tpc_id")
    @PecaColetor(isEntidade = true)
    private TipoContrato tipoContrato;

    @ManyToOne
    @JoinColumn(name = "fas_id", referencedColumnName = "fas_id")
    @PecaColetor(isEntidade = true)
    private Fase fase;

    @Column(name = "pro_data_cadastro")
    @Temporal(TemporalType.DATE)
    @PecaColetor(tipo = TipoMascara.DATA)
    private Date dataCadastro;

    @ManyToOne
    @JoinColumn(name = "usr_id", referencedColumnName = "usr_id")
    @PecaColetor(isEntidade = true)
    private Usuario usuarioCadastro;

    @ManyToOne
    @JoinColumn(name = "mat_id", referencedColumnName = "mat_id")
    @PecaColetor(isEntidade = true)
    private Materia materia;

    @PecaColetor
    @Column(name = "pro_objeto", length = 1024)
    private String objetoProcesso;

    @Column(name = "pro_motivo_fase", length = 1024)
    private String motivoFase;

    @ManyToOne
    @JoinColumn(name = "jut_id", referencedColumnName = "jut_id")
    @PecaColetor(isEntidade = true)
    private JuizoTribunal juizoTribunal;

    @Column(name = "pro_valor")
    @PecaColetor(tipo = TipoMascara.DINHEIRO)
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "adv_id", referencedColumnName = "adv_id")
    @PecaColetor(isEntidade = true)
    private Advogado advogado;

    @Length(max = 100000)
    @Column(name = "pro_observacoes", length = 100000)
    @PecaColetor
    private String observacoes;

    @Length(max = 100000)
    @Column(name = "pro_fatos", length = 100000)
    @PecaColetor
    private String fatos;

    @Length(max = 100000)
    @Column(name = "pro_provas", length = 100000)
    @PecaColetor
    private String provas;

    @Length(max = 100000)
    @Column(name = "pro_informacao_reservada", length = 100000)
    @PecaColetor
    private String informacaoReservada;

    @Column(name = "pro_nb_indeferido")
    @PecaColetor
    private String nbIndeferido;

    @Column(name = "pro_nb_deferido")
    @PecaColetor
    private String nbDeferido;

    @Column(name = "pro_der")
    @Temporal(TemporalType.DATE)
    @PecaColetor(tipo = TipoMascara.DATA)
    private Date der;

    @Column(name = "pro_dcb")
    @Temporal(TemporalType.DATE)
    @PecaColetor(tipo = TipoMascara.DATA)
    private Date dcb;

    @Column(name = "pro_dib")
    @Temporal(TemporalType.DATE)
    @PecaColetor(tipo = TipoMascara.DATA)
    private Date dib;

    @Column(name = "pro_dip")
    @Temporal(TemporalType.DATE)
    @PecaColetor(tipo = TipoMascara.DATA)
    private Date dip;

    @Length(max = 100000)
    @Column(name = "pro_motivo_indeferimento", length = 100000)
    @PecaColetor
    private String motivoIndeferimento;

    @Length(max = 100000)
    @Column(name = "pro_incapacidade", length = 100000)
    @PecaColetor
    private String incapacidade;

    @Length(max = 100000)
    @Column(name = "pro_dependente", length = 100000)
    @PecaColetor
    private String dependente;

    public String getTodosAdversarios() {
        String adv = "";
        for (Adversario a : adversarios) {
            adv = adv.concat(a.getCliente().getPessoa().getNome()).concat(", ");

        }
        return adv;
    }

    public String getFatos() {
        return fatos;
    }

    public void setFatos(String fatos) {
        this.fatos = fatos;
    }

    public String getProvas() {
        return provas;
    }

    public void setProvas(String provas) {
        this.provas = provas;
    }

    public String getInformacaoReservada() {
        return informacaoReservada;
    }

    public void setInformacaoReservada(String informacaoReservada) {
        this.informacaoReservada = informacaoReservada;
    }

    public String getNbIndeferido() {
        return nbIndeferido;
    }

    public void setNbIndeferido(String nbIndeferido) {
        this.nbIndeferido = nbIndeferido;
    }

    public String getNbDeferido() {
        return nbDeferido;
    }

    public void setNbDeferido(String nbDeferido) {
        this.nbDeferido = nbDeferido;
    }

    public Date getDer() {
        return der;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setDer(Date der) {
        this.der = der;
    }

    public Date getDcb() {
        return dcb;
    }

    public void setDcb(Date dcb) {
        this.dcb = dcb;
    }

    public Date getDib() {
        return dib;
    }

    public void setDib(Date dib) {
        this.dib = dib;
    }

    public Date getDip() {
        return dip;
    }

    public void setDip(Date dip) {
        this.dip = dip;
    }

    public String getMotivoIndeferimento() {
        return motivoIndeferimento;
    }

    public void setMotivoIndeferimento(String motivoIndeferimento) {
        this.motivoIndeferimento = motivoIndeferimento;
    }

    public String getIncapacidade() {
        return incapacidade;
    }

    public void setIncapacidade(String incapacidade) {
        this.incapacidade = incapacidade;
    }

    public String getDependente() {
        return dependente;
    }

    public void setDependente(String dependente) {
        this.dependente = dependente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Enderecamento getEnderecamento() {
        return enderecamento;
    }

    public void setEnderecamento(Enderecamento enderecamento) {
        this.enderecamento = enderecamento;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Usuario getUsuarioCadastro() {
        return usuarioCadastro;
    }

    public void setUsuarioCadastro(Usuario usuarioCadastro) {
        this.usuarioCadastro = usuarioCadastro;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public JuizoTribunal getJuizoTribunal() {
        return juizoTribunal;
    }

    public void setJuizoTribunal(JuizoTribunal juizoTribunal) {
        this.juizoTribunal = juizoTribunal;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Advogado getAdvogado() {
        return advogado;
    }

    public void setAdvogado(Advogado advogado) {
        this.advogado = advogado;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public List<Adversario> getAdversarios() {
        return adversarios;
    }

    public void setAdversarios(List<Adversario> adversarios) {
        this.adversarios = adversarios;
    }

    public List<ParteInteressada> getParteInteressadas() {
        return parteInteressadas;
    }

    public void setParteInteressadas(List<ParteInteressada> parteInteressadas) {
        this.parteInteressadas = parteInteressadas;
    }

    public TipoContrato getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(TipoContrato tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public String getObjetoProcesso() {
        return objetoProcesso;
    }

    public void setObjetoProcesso(String objetoProcesso) {
        this.objetoProcesso = objetoProcesso;
    }

    public Fase getFase() {
        return fase;
    }

    public void setFase(Fase fase) {
        this.fase = fase;
    }

    public String getMotivoFase() {
        return motivoFase;
    }

    public void setMotivoFase(String motivoFase) {
        this.motivoFase = motivoFase;
    }

    public String getNomeDoCliente() {
        return this.cliente.getNome();
    }

    public String getNomeDoColaborador() {
        if (this.colaborador != null) {
            return this.colaborador.getNome();
        } else {
            return "";
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final Processo other = (Processo) obj;
        return Objects.equals(this.id, other.id);
    }

}
