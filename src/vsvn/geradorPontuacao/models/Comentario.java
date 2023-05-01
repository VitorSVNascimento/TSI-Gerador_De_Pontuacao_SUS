package vsvn.geradorPontuacao.models;

public class Comentario {

	private String id;
	private String comentario;
	
	public Comentario(String id,String comentario) {
		this.id = id;
		this.comentario = comentario;
	}

	public String getId() {
		return id;
	}

	public String getComentario() {
		return comentario;
	}

	@Override
	public String toString() {
		return "Comentario [id=" + id + ", comentario=" + comentario + "]";
	}
	
	
	
}
