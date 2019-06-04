package com.bolsadeideas.springboot.reactor.app.controllers;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import com.bolsadeideas.springboot.reactor.app.models.dao.ProductoDao;
import com.bolsadeideas.springboot.reactor.app.models.documents.Producto;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Controller
@Slf4j
public class ProductoController {

	@Autowired
	private ProductoDao productoDao;

	@GetMapping(value = { "/listar", "", "/" })
	public String listar(Model model) {

		Flux<Producto> productos = productoDao.findAll().map(producto -> {
			producto.setNombre(producto.getNombre().toUpperCase());
			return producto;
		});

		productos.subscribe(producto -> log.info("NOMBRE DEL PRODUCTO: {}", producto.getNombre()));

		model.addAttribute("titulo", "Listado de productos");
		model.addAttribute("productos", productos);
		return "listar";
	}

	@GetMapping(value = { "/listar-data-driver" })
	public String listarDataDriver(Model model) {

		Flux<Producto> productos = productoDao.findAll().map(producto -> {
			producto.setNombre(producto.getNombre().toUpperCase());
			return producto;
		})
				/**
				 * Con este método agregas un delay a la emisión de cada elemento que llega por
				 * el flux
				 */
				.delayElements(Duration.ofSeconds(1L));

		productos.subscribe(producto -> log.info("NOMBRE DEL PRODUCTO: {}", producto.getNombre()));

		model.addAttribute("titulo", "Listado de productos");
		/**
		 * DataDriver es un modo para trabajar con contra-presion, es decir cuando hay
		 * demasiado elementos llegando desde el flux y se desean procesar conforme van
		 * llegando o bien cuando llegan todos... depende de como se quiera manejar la
		 * contra-presión
		 * 
		 * Con esto controlas el delay que generaste arriba para evitar que el usuario
		 * esté esperando a que se carguen todos los elementos metes tu Flux en el
		 * constructor del objeto ReactiveDataDriverContextVariable y le indicas cada
		 * cuando elementos deberá mostrar en la vista, en este caso se puso 1, es decir
		 * cada que se emita un nuevo elemento se va a mostar en la vista, esto pasa
		 * porque Thymeleaf por debajo hace un subscribe a los elementos que llegan.
		 */
		model.addAttribute("productos", new ReactiveDataDriverContextVariable(productos, 2));
		return "listar";
	}

	/**
	 * Aqui se usa como DataDriver el modo Full, es decir no se configura nada y se
	 * procesa el "bonche" de datos cuando están todos los elementos listos.
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping(value = { "/listar-full" })
	public String listarFull(Model model) {

		Flux<Producto> productos = productoDao.findAll().map(producto -> {
			producto.setNombre(producto.getNombre().toUpperCase());
			return producto;
		}).repeat(5000);

		model.addAttribute("titulo", "Listado de productos");
		model.addAttribute("productos", productos);
		return "listar";
	}

	/**
	 * Aqui se usa el modo chunked que basicamente hace lo mismo que el
	 * ContextVariable pero en vez de especificar el numero de elementos especificas
	 * el tamaño del buffer en el .yml asi como también especificas las vistas a las
	 * que va a aplicar dicho modo
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping(value = { "/listar-chunked" })
	public String listarChunked(Model model) {

		Flux<Producto> productos = productoDao.findAll().map(producto -> {
			producto.setNombre(producto.getNombre().toUpperCase());
			return producto;
		}).repeat(5000);

		model.addAttribute("titulo", "Listado de productos");
		model.addAttribute("productos", productos);
		return "listar-chunked";
	}

}
