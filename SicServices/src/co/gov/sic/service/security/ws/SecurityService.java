package co.gov.sic.service.security.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import co.gov.sic.service.security.business.ImpInterfaceSecService;
import co.gov.sic.service.security.interfaces.InterfaceSecService;

@Path("/SecurityService")
@Consumes("application/json")
public class SecurityService {
	
	InterfaceSecService interfaceToken = new ImpInterfaceSecService();
	
	@POST
	@Consumes("application/json")
	@Path("/getToken")
	public Response getToken(String clientData){
		return interfaceToken.getToken(clientData);
	}
}
