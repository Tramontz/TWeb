package pizze;

public class PizzaPrenotazione extends Pizza{
	int quantita;
	
	public PizzaPrenotazione(String n, String imm, String c, double p, String des, int q){
		nome = n;
		COD = c;
		prezzo = p;
		descrizione = des;
		immagine = imm +".jpg";
		quantita = q;
	}
	public int getQuantita(){
		return quantita;
	}
	public void setQuantita(int q){
		quantita += q;
	}
}
