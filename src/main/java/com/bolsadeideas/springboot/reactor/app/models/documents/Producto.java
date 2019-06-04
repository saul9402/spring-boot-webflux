package com.bolsadeideas.springboot.reactor.app.models.documents;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Document(collection = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

	@Id
	private String id;

	private String nombre;

	private Double precio;

	private Date createAt;

	public Producto(String nombre, Double precio) {
		this.nombre = nombre;
		this.precio = precio;
	}

}
