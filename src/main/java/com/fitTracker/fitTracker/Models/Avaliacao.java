package com.fitTracker.fitTracker.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
public class Avaliacao extends Atividade{

    @Column
    private int pontuacao;

    @Column
    @ElementCollection(targetClass=String.class)
    private List<String> questoes;

    public Avaliacao() {
    }

    public Avaliacao(String nome, String descricao, int pontuacao) {
        super(nome,descricao);
        this.pontuacao = pontuacao;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public List<String> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<String> questoes) {
        this.questoes = questoes;
    }
}
