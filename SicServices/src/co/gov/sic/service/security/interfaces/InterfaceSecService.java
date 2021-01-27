package co.gov.sic.service.security.interfaces;

import javax.ws.rs.core.Response;

public interface InterfaceSecService {
	public Response getToken (String clientData);
}
