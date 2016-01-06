/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atus.cadastro.modelo;

import br.com.atus.cadastro.modelo.Advogado;
import br.com.atus.enumerated.Sexo;
import br.com.atus.util.peca.PecaColetor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 * @author Ari
 */
@Entity
@Table(name = "colaborador", schema = "cadastro")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Colaborador implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "col_id", nullable = false)
    @PecaColetor
    private Long id;
    @Embedded
    @PecaColetor(isEntidade = true)
    private Pessoa pessoa;

    @Enumerated(EnumType.STRING)
    @Column(name = "col_sexo")
    @PecaColetor
    private Sexo sexo;

    @NotNull
    @Column(name = "col_participacao", nullable = false)
    @PecaColetor(mascara = "#,##0.00")
    private BigDecimal participacao;

    @ManyToOne
    @JoinColumn(name = "adv_id", referencedColumnName = "adv_id")
    @PecaColetor(isEntidade = true)
    private Advogado advogado;

    @Column(name = "col_dados_bancarios", length = 1024)
    private String dadosBancarios;
    @Column(name = "col_observacao", length = 1024)
    @PecaColetor
    private String observacao;
    
    public String getNome(){
        return this.pessoa != null ? this.pessoa.getNome() : "";
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public BigDecimal getParticipacao() {
        return participacao;
    }

    public void setParticipacao(BigDecimal participacao) {
        this.participacao = participacao;
    }

    public Advogado getAdvogado() {
        return advogado;
    }

    public void setAdvogado(Advogado advogado) {
        this.advogado = advogado;
    }

    public String getDadosBancarios() {
        return dadosBancarios;
    }

    public void setDadosBancarios(String dadosBancarios) {
        this.dadosBancarios = dadosBancarios;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final Colaborador other = (Colaborador) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
