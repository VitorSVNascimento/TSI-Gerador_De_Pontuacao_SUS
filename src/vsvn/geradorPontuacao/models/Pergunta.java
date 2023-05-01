package vsvn.geradorPontuacao.models;

import java.util.ArrayList;
import java.util.List;

public class Pergunta {

	private byte idPergunta;
	private List<Integer> listaDeRespostas;
	
	public Pergunta() {
		listaDeRespostas = new ArrayList<Integer>();
	}
	
	public Pergunta(byte idPergunta) {
		this();
		this.idPergunta = idPergunta;
	}
	
	public boolean addResposta(Integer resposta) {
		return listaDeRespostas.add(resposta);
		
	}
	
	public float somaRespostasSUS() {
		if(idPergunta%2 == 0)
			return somaRespostasPerguntaParSUS()/listaDeRespostas.size();
		return somaRespostasPerguntaImparSUS()/listaDeRespostas.size();
	}
	private float somaRespostasPerguntaImparSUS() {
		float soma = 0f;
		for(Integer resposta : listaDeRespostas)
			soma += resposta - 1.0f;
		
		return soma;
	}
	private float somaRespostasPerguntaParSUS() {
		float soma = 0f;
		for(Integer resposta : listaDeRespostas)
			soma += 5.0f - resposta ;
		
		return soma;
	}

	@Override
	public String toString() {
		StringBuilder perguntaString = new StringBuilder();
		perguntaString.append(String.format("ID:%d - ", idPergunta));
		for(Integer resposta : listaDeRespostas)
			perguntaString.append(String.format("%d, ", resposta));
		return perguntaString.toString();
	}
	
	
	
}
