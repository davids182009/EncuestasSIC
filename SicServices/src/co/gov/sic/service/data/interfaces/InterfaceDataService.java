package co.gov.sic.service.data.interfaces;

import javax.ws.rs.core.Response;

public interface InterfaceDataService {

	public Response insertarDataDB (String clientData);
	public Response consultarMarcasPC ();
	public Response consultarEncuestas (String clientData);
	public Response eliminarEncuestas (String clientData);
}
