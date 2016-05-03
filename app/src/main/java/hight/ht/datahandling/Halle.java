package hight.ht.datahandling;

public class Halle {

	private int hallenNr;
	private String name;
	private String strasse;
	private int hausnummer;
	private String plz;
	private String ort;

	public int getHallenNr() {
		return hallenNr;
	}

	public void setHallenNr(int hallenNr) {
		this.hallenNr = hallenNr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public int getHausnummer() {
		return hausnummer;
	}

	public void setHausnummer(int hausnummer) {
		this.hausnummer = hausnummer;
	}

	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public String kompletteAdresse() {
		String adresse;
		if (this.getHausnummer() == 0) {
			adresse = this.getStrasse() + ", " + this.getPlz() + " " + this.getOrt();
		} else {
			adresse = this.getStrasse() + " " + this.getHausnummer() + ", " + this.getPlz() + " " + this.getOrt();
		}
		return adresse;
	}

	@Override
	public String toString() {
		return "Name: " + this.name;
	}

}
