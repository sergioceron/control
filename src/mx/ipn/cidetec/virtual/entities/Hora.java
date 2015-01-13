package mx.ipn.cidetec.virtual.entities;

import javax.persistence.*;

/**
 * Created by sergio on 12/06/2014.
 */
@Entity
public class Hora implements Comparable{
	private final String[] dias = new String[]{ "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado" };
	private Long id;

	private int diaSemana = 0;
	private String horaInicio = "";
	private String horaFin = "";

	public Hora() {
	}

	public Hora( int diaSemana ) {
		this.diaSemana = diaSemana;
	}

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public int getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana( int diaSemana ) {
		this.diaSemana = diaSemana;
	}

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio( String horaInicio ) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFin() {
		return horaFin;
	}

	public void setHoraFin( String horaFin ) {
		this.horaFin = horaFin;
	}

	@Transient
	public String getDia(){
		return dias[diaSemana];
	}

	@Override
	public String toString() {
		return getDia() + " - " + horaInicio + " a " + horaFin;
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;

		Hora hora = (Hora) o;

		if( diaSemana != hora.diaSemana ) return false;
		if( !hora.horaFin.equals( horaFin ) ) return false;
		if( !hora.horaInicio.equals( horaInicio ) ) return false;

		return true;
	}

	@Override
	public int compareTo( Object o ) {
		if( o instanceof Hora ){
			return this.getDiaSemana() - ( (Hora) o ).getDiaSemana();
		}
		return -10000;
	}
}
