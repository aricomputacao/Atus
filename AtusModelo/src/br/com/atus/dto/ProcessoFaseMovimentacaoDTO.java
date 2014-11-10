/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atus.dto;

import br.com.atus.modelo.Fase;
import br.com.atus.modelo.Movimentacao;
import br.com.atus.modelo.Processo;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Depende da criação das seguintes view: 1 -- CREATE OR REPLACE VIEW
 * processo.fase_processo ( pro_id, fas_id) AS SELECT pr.pro_id, fa.fas_id FROM
 * processo.processo pr JOIN processo.fase fa ON pr.fas_id = fa.fas_id GROUP BY
 * fa.fas_id, pr.pro_id ORDER BY fa.fas_id; 2 -- CREATE OR REPLACE VIEW
 * processo.movimentacao_processo ( fas_id, pro_id, ult_mov) AS SELECT
 * fp.fas_id, fp.pro_id, max(mv.mov_id) AS ult_mov FROM processo.fase_processo
 * fp JOIN processo.movimentacao mv ON mv.pro_id = fp.pro_id GROUP BY fp.pro_id,
 * fp.fas_id;
 *
 * @author ari
 */
@Entity
@Table(name = "movimentacao_processo", schema = "processo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProcessoFaseMovimentacaoDTO implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "pro_id", referencedColumnName = "pro_id")
    private Processo processo;

    @ManyToOne
    @JoinColumn(name = "fas_id", referencedColumnName = "fas_id")
    private Fase fase;

    @ManyToOne
    @JoinColumn(name = "mov_id", referencedColumnName = "mov_id")
    private Movimentacao movimentacao;

    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    public Fase getFase() {
        return fase;
    }

    public void setFase(Fase fase) {
        this.fase = fase;
    }

    public Movimentacao getMovimentacao() {
        return movimentacao;
    }

    public void setMovimentacao(Movimentacao movimentacao) {
        this.movimentacao = movimentacao;
    }

   

}
