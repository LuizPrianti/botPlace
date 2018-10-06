
public class Instituicao {
	
	private String name;
	private String coordenadas;
	
	public Instituicao(String name, String coordenadas) {
		this.name = name;
		this.coordenadas = coordenadas;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getCoordenadas() {
		return coordenadas;
	}

	public void setCoordenadas(String coordenadas) {
		this.coordenadas = coordenadas;
	}

}
