package acuario;

import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Sensores {

	private static final AtomicInteger COUNTER = new AtomicInteger();
	private final int id;
	private String nombre;
	private String valor;
	private Integer respuesta;

	@JsonCreator
	public Sensores(@JsonProperty("nombre") String nombre,
			@JsonProperty("valor") String valor,
			@JsonProperty("respuesta") Integer respuesta) {
		this.id = COUNTER.getAndIncrement();
		this.nombre = nombre;
		this.valor = valor;
		this.respuesta = respuesta;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Integer getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(Integer respuesta) {
		this.respuesta = respuesta;
	}

	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sensores other = (Sensores) obj;
		if (id != other.id)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

}
