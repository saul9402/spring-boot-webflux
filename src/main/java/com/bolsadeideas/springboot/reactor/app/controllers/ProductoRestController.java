package com.bolsadeideas.springboot.reactor.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.reactor.app.models.dao.ProductoDao;
import com.bolsadeideas.springboot.reactor.app.models.documents.Producto;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping(value = "/api/productos")
public class ProductoRestController {

	@Autowired
	private ProductoDao productoDao;

	@GetMapping
	public Flux<Producto> index() {
		Flux<Producto> productos = productoDao.findAll().map(producto -> {
			producto.setNombre(producto.getNombre().toUpperCase());
			return producto;
		}).doOnNext(producto -> log.info("NOMBRE DEL PRODUCTO: {}", producto.getNombre()));

		return productos;

	}

	@GetMapping(value = "/{id}")
	public Mono<Producto> show(@PathVariable String id) {

		// Mono<Producto> producto = productoDao.findById(id);
		Flux<Producto> productos = productoDao.findAll();

		Mono<Producto> producto = productos.filter(prod -> prod.getId().equals(id)).next()
				.doOnNext(prod -> log.info("NOMBRE DEL PRODUCTO: {}", prod.getNombre()));

		return producto;
	}
}
