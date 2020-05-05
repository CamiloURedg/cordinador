package com.uniminuto.ws.rest.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


import com.uniminuto.ws.rest.vo.blockchain;
import com.uniminuto.ws.rest.vo.clientb;
import com.uniminuto.ws.rest.vo.clientr;
import com.uniminuto.ws.rest.vo.register;

@Path ("/coordinator")
public class coordinator {	
	//Se instancia el metodo para llamar al metodo que realizara el consumo del web service a register.
	clientr clr=new clientr();
	//Se instancia el metodo para llamar al metodo que realizara el consumo del web service a blockchain.
	clientb clb=new clientb();
	
	/*
	 * Metodo de tipo pos que se utliza para solicitar los siguientes datos 
	 * Direccion de Wallet de origen el nombre de la varibale es (dir_origen)
	 * Direccion de Wallet de destino el nombre de la varibale es (dir_destino)
	 * Monto a transacion con el nombre de la variable (monto)
	 * El servicio retorna una respuesta de tipo String donde se informa el estado de la
	 * transacion, la cual puede tomar dos valores true o false	 
	 */		
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.TEXT_PLAIN})
	@Path ("/pasodesaldo")
	public String pasodesaldo(register r)
	{
		/*
		 * Variable que cumple la funcion de tomar los estados de la respuesta dada por register 
		 * teniendo el estado de validacion de datos, se inicializa el false.
		 */
		String val_r="false";
		
		val_r=clr.client_register(r.getDir_origen(), r.getDir_destino(),r.getMonto());
	    
		if (val_r.equals("true"))
		{
			/*
			 * Variable que cumple la funcion de tomar los estados de la respuesta dada por blockchain 
			 * teniendo el estado de la transacion se inicializa el false.
			 */
			String val_b="false";
			
			val_b=clb.client_transacion(r.getDir_origen(), r.getDir_destino(),r.getMonto());
			
			if(val_b.equals("true"))
			{
				r.setValidacion("true");
			}
			else
			{
				r.setValidacion("false");
			}
		}
		else
		{
			r.setValidacion("false");
		}
			
	   return r.getValidacion();
	}	
	
	
	
	/*
	 * Metodo de tipo pos que se utliza para solicitar los siguientes datos 
	 * Direccion de Wallet de origen el nombre de la varibale es (dir_origen)
	 * El servicio retorna una respuesta de tipo String donde se informa el estado de la
	 * transacion, la cual puede tomar dos valores true o false	 
	 */		
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.TEXT_PLAIN})
	@Path ("/consultarsaldo")
	public String consultarsaldo(blockchain b)
	{
		/*
		 * Variable que cumple la funcion de tomar la respuesta dada por blockchain esta respuesta se
		 * recibe en una variable tipo String.
		 */
	   String val_cb;
	   val_cb=clb.client_consul(b.getDir_origen());
	   
		   b.setValidacion(val_cb);
	       return b.getValidacion();
	}
	
}
