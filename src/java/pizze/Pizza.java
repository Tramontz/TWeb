package pizze;

public class Pizza {
	String nome;
	String descrizione;
	String COD;
	double prezzo;
	String immagine;
	
	public Pizza(){
		
	}
	public Pizza(String n, String d, String c, double p, String imm){
		nome = n;
		descrizione = d;
		COD=c;
		prezzo = p;
		immagine = imm;
	}
	
	public void setNome(String nome){
		this.nome = nome;
	}
	public String getNome(){
		return nome;
	}
	
	public void setCOD(String cod){
		this.COD = cod;
	}
	public String getCOD(){
		return COD;
	}
	public void setDescrizione(String des){
		descrizione = des;
	}
	public String getDescrizione(){
		return descrizione;
	}
	public void setPrezzo(double pr){
		prezzo = pr;
	}
	public double getPrezzo(){
		return prezzo;
	}
	public void setImmagine(String img){
		immagine = img;
	}
	public String getImmagine(){
		return immagine;
	}
}
