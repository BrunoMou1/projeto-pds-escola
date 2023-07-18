package com.fitTracker.fitTracker.Controllers;

import com.fitTracker.fitTracker.Models.Avaliacao;

import com.fitTracker.fitTracker.Service.AtividadeService;
import com.fitTracker.fitTracker.Service.RecompensaService;
import com.fitTracker.fitTracker.Service.UsuarioService;
import com.fitTracker.fitTracker.Strategy.concrets.EstrategiaAvaliacao;
import com.fitTracker.fitTracker.Strategy.concrets.EstrategiaRecompensaEscola;
import com.fitTracker.fitTracker.Util.ElementoExisteException;
import com.fitTracker.fitTracker.Util.ElementoNaoEncontradoException;
import com.fitTracker.fitTracker.Util.RegraNegocioException;
import com.fitTracker.fitTracker.Util.RepositoryNullException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/avaliacao")
public class AvaliacaoController {

    @Autowired
    private RecompensaService recompensaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    AtividadeService atividadeService;

    @PostMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity createAvaliacao(@RequestBody Avaliacao avaliacao) {
        try {
            Avaliacao avaliacaoSalva = (Avaliacao) atividadeService.createAtividade(avaliacao, new EstrategiaAvaliacao());
            avaliacao.getUsuarios().forEach(usuario -> {
                try {
                    usuarioService.addAvaliacaoUsuario(usuario.getId(), avaliacaoSalva.getId());
                } catch (ElementoNaoEncontradoException | ElementoExisteException | RepositoryNullException e) {
                    e.printStackTrace();
                }
            });
            return new ResponseEntity(avaliacaoSalva, HttpStatus.CREATED);
        } catch (ElementoNaoEncontradoException | RegraNegocioException | RepositoryNullException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping(value = "/{id}", produces = "application/json;charset=UTF-8")
    public ResponseEntity updateAvaliacao(@PathVariable("id") Long usuarioId, @RequestBody Avaliacao avaliacao){
        try{
            Avaliacao avaliacaoAtualizada = (Avaliacao) atividadeService.updateAtividade(avaliacao, new EstrategiaAvaliacao());
            recompensaService.gerarPontuacao(usuarioId, new EstrategiaRecompensaEscola());
            return ResponseEntity.ok(avaliacaoAtualizada);
        }catch (ElementoNaoEncontradoException | RegraNegocioException | RepositoryNullException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAvaliacao(@PathVariable("id") Long avaliacaoId){
        try{
            atividadeService.deleteAtividade(avaliacaoId);
            return ResponseEntity.ok().build();
        }catch (ElementoNaoEncontradoException | RegraNegocioException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
