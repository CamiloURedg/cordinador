# Coordinator(BC1)

El componente Cooordinador se desarrollo bajo el lenguage java, con el fin de coordinar a los diferentes componentes del proyecto.

## Contenido

Dentro del desarrollo de componente se crearon diferentes clases y métodos los cuales se especifican mas a detalle en el el repositorio [javadoc_coordinator ](https://pip.pypa.io/en/stable/)

### Clase coordinator

En esta clase se encuentra diferentes servicios:

#### Servicio Paso de Saldo

Este servicio es de tipo post, es consumido por el componente Wallet para realizar una transferencia de saldo de una cuenta origen a una cuenta destino. Se recibe un archivo tipo json con lola información dirección de origen (dir_origen), dirección destino (dir_destino) y la cantidad de dinero a transferir (monto). 

El servicio devuelve como respuesta un dato de tipo "String" con el valor de “true” si la transferencia se realizó de forma exitosa y el valor “false” si se generó algún error durante la transferencia.

Para el consumo del servicio se debe realizar a dirección [http://192.168.0.18:8080/coordinator/RestU/](http://192.168.0.18:8080/coordinator/RestU/)

```bash
    @POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.TEXT_PLAIN})
	@Path ("/pasodesaldo")
	public String pasodesaldo(register r)
	{
		
		String val_r="false";
		
		val_r=clr.client_register(r.getDir_origen(), r.getDir_destino(),r.getMonto());
	    
		if (val_r.equals("true"))
		{
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
```

#### Ejemplo de Consumo

```
Archivo tipo Json
{
 "dir_origen":"#direccionWalletOrigen",
 "dir_destino":"#direccionWalletDestino",
 "monto":"#cantidad"
}
```

#### Respuesta del Consumo
```
{
 "false" (Transacción Rechazada)
 "true" (Transacción exitosa)
}
```
#### Servicio Consulta de saldo

Este servicio es de tipo post, es consumido por el componente Wallet para realizar una consulta de saldo de una cuenta Wallet especificada. Se recibe un archivo tipo json con la información dirección de origen (dir_origen).

El servicio devuelve como respuesta un dato de tipo "String" con el valor del monto o el mensaje enviado por el componente por blockchain.

Para el consumo del servicio se debe realizar a dirección [http://192.168.0.18:8080/coordinator/RestU/](http://192.168.0.18:8080/coordinator/RestU/)



#### Ejemplo de Consumo

```
Archivo tipo Json
{
 "dir_origen":"#direccionWalletOrigen",
}
```

#### Respuesta del Consumo
```
"cantidad de dinero " o "mensaje de error"

```
### Clase register
En la clase register se alamcenan los parametros que se envian desde wallet. En la variable "validaccion", se almacena la respuesta segun las calidaciones realizadas durante el proceso de transacion. 

```
ublic class register {
	
	 String dir_origen;
	 String dir_destino;
	 String monto;
	 String validacion;
	 
	public String getDir_origen() {
		return dir_origen;
	}
	public void setDir_origen(String dir_origen) {
		this.dir_origen = dir_origen;
	}
	public String getDir_destino() {
		return dir_destino;
	}
	public void setDir_destino(String dir_destino) {
		this.dir_destino = dir_destino;
	}
	public String getMonto() {
		return monto;
	}
	public void setMonto(String monto) {
		this.monto = monto;
	}
	public String getValidacion() {
		return validacion;
	}
	public void setValidacion(String validacion) {
		this.validacion = validacion;
	}

```

### Clase blockchain
En la clase register se alamcenan el parametro enviado desde wallet en la variable "dir_origen". En la variable "validaccion", se almacena la respuesta enviada por el componente blockchain. 

```
public class blockchain {
    String dir_origen;
    String validacion;
    
    public String getDir_origen() {
		return dir_origen;
	}
	public void setDir_origen(String dir_origen) {
		this.dir_origen = dir_origen;
	}
	public String getValidacion() {
		return validacion;
	}
	public void setValidacion(String validacion) {
		this.validacion = validacion;
	}
	
}

```
### Clase clientb
En este clase se tienes dos metodos, los cuales consumen el servicio de los componentes register y blockchain.

#### Metodo Consulta Saldo (client_consul)
Se le pasa como parametro un dato String, coorespondiente al dato de la direccion wallet origen enviado por wallet, realiza el consumo del sercivio que publica el componente blockchain y se le envia una archivo json con la direccion Origen, se espera dato de tipo String con la respuesta.

Se le pasa como parametro un dato String, coorespondiente al dato de la direccion wallet origen enviado por wallet, realiza el consumo del sercivio que publica el componente blockchain y se le envia una archivo json con la direccion Origen, se espera dato de tipo String con la respuesta.

#### Metodo Paso Saldo (client_transacion)

Para este metodo se pasa 3 parametros de tipo String, coorespondiente a los datos de direcion de origen, direccion destino, monto provenientes del componente wallet, y este posterior realiza el consumo del servicio publicado por blockchain, dando como respuesta un dato String con el parametro "true " si la transacion se realizo de manera correcta o "false" si la transacion no se pudo completar por alguna razon.

```
public class clientb {
	
	String dirorigen;
	String dirdestino;
	String monto;
	
	public String client_consul(String dir_origen)
	{
		
	    dirorigen=dir_origen;
	    
		Client client=Client.create();
		WebResource webResource= client.resource("http://192.168.0.18:8080/RestU/rest/pc/pbm");
	    String input="{\n\""+"dir_origen"+"\":\""+dirorigen+"\"\n}";   
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, input);
	    String output = response.getEntity(String.class);
		return output;		
	}
	
	public String client_transacion(String dir_origen, String dir_destino, String mont)
	{
		dirorigen=dir_origen;
		dirdestino=dir_destino;
		monto=mont;	
		
		Client client=Client.create();
		WebResource webResource= client.resource("http://192.168.0.18:8080/RestU/rest/pc/pcm");	    
		String input="{\n\""+"dirorigen"+"\":\""+dirorigen+"\",\n"
  			     +"\""+"dirdestino"+"\":\""+dirdestino+"\",\n"
  			     +"\""+"monto"+"\":\""+monto+"\"\n}";		
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, input);
		String output = response.getEntity(String.class);
		return output;
		
	}

}

```
### Clase clientr

### Clase clientr
En este clase se tiene un metodo, el cual consume el servicio publicado por el componente register.

#### Metodo Paso Saldo (client_register)
Para este metodo se pasa 3 parametros de tipo String, coorespondiente a los datos de direcion de origen, direccion destino, monto provenientes del componente wallet, y este posterior realiza el consumo del servicio publicado por register, dando como respuesta un dato String con el parametro "true " si los datos son validos o "false" si los datos no corresponden.

```
public class clientr {
	String dirorigen;
	String dirdestino;
	String monto;
	
	public String client_register(String dir_origen, String dir_destino, String mont)
	{
		dirorigen=dir_origen;
		dirdestino=dir_destino;
		monto=mont;
			
		Client client=Client.create();
		WebResource webResource= client.resource("http://192.168.0.18:8080/RestU/rest/pc/pcm");
	    String input="{\n\""+"dirorigen"+"\":\""+dirorigen+"\",\n"
	   			     +"\""+"dirdestino"+"\":\""+dirdestino+"\",\n"
	   			     +"\""+"monto"+"\":\""+monto+"\"\n}";	    		
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, input);
		String output = response.getEntity(String.class);
		return output;
		
		
		
	}
```



