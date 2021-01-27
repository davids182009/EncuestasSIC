package co.gov.sic.service.data.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import co.gov.sic.service.data.business.ImpInterfaceDataService;
import co.gov.sic.service.data.interfaces.InterfaceDataService;

@Path("/DataService")
@Consumes("application/json")
public class DataService {

	InterfaceDataService interfaceDataService = new ImpInterfaceDataService();
	
	@PUT
	@Consumes("application/json")
	@Path("/insertarDataDB")
	public Response insertarDataDB(String clientData){
		return interfaceDataService.insertarDataDB(clientData);
	}
	
	@GET
	@Path("/consultarMarcasPC")
	public Response consultarMarcasPC(){
		return interfaceDataService.consultarMarcasPC();
	}
	
	@POST
	@Consumes("application/json")
	@Path("/consultarEncuestas")
	public Response consultarEncuestas(String clientData){
		return interfaceDataService.consultarEncuestas(clientData);
	}
	
	@DELETE
	@Consumes("application/json")
	@Path("/eliminarEncuestas")
	public Response eliminarEncuestas(String clientData){
		return interfaceDataService.eliminarEncuestas(clientData);
	}
}
