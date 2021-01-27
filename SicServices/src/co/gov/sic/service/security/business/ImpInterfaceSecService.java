package co.gov.sic.service.security.business;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.ParameterMode;
import javax.persistence.Persistence;
import javax.persistence.StoredProcedureQuery;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import co.gov.sic.service.security.interfaces.InterfaceSecService;

public class ImpInterfaceSecService implements InterfaceSecService {

	String jsonResponse = "";
	public Response getToken(String clientData) {
		
		try {
			System.out.println("DATA ENVIADA...");
			System.out.println(clientData);
			
			JSONObject clientJson= null; 
			clientJson = new JSONObject(clientData);
			String usuarioEnviado = clientJson.getString("usuario");
			String contraEnviado = clientJson.getString("contrasena");
			System.out.println(usuarioEnviado);
			System.out.println(contraEnviado);
			
			
			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory( "SicServices" );
			EntityManager em = entityManagerFactory.createEntityManager();

			StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("consultar_usuario");
			storedProcedure.registerStoredProcedureParameter("usuario", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("pass", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("resultado", String.class, ParameterMode.OUT);
			storedProcedure.setParameter("usuario", usuarioEnviado);
			storedProcedure.setParameter("pass", contraEnviado);
			storedProcedure.execute();
			
			String resultado = (String) storedProcedure.getOutputParameterValue("resultado");
			
			if (resultado != null) {
				return Response
						.status(Response.Status.OK)
						.entity(resultado)
						.build();
			} else {
				
				StoredProcedureQuery storedProcedureExiste = em.createStoredProcedureQuery("usuario_existe");
				storedProcedureExiste.registerStoredProcedureParameter("usuario", String.class, ParameterMode.IN);
				storedProcedureExiste.registerStoredProcedureParameter("resultado", String.class, ParameterMode.OUT);
				storedProcedureExiste.setParameter("usuario", usuarioEnviado);
				storedProcedureExiste.execute();
				
				String resultadoExiste = (String) storedProcedureExiste.getOutputParameterValue("resultado");
				int conteoFallido = 0;
				String mensaje = "";
				
				if (resultadoExiste != null) {
					
					StoredProcedureQuery storedProcedureLoginFallido = em.createStoredProcedureQuery("login_fallido");
					storedProcedureLoginFallido.registerStoredProcedureParameter("nombre", String.class, ParameterMode.IN);
					storedProcedureLoginFallido.registerStoredProcedureParameter("resultado", String.class, ParameterMode.OUT);
					storedProcedureLoginFallido.setParameter("nombre", usuarioEnviado);
					storedProcedureLoginFallido.execute();
					
					String resultadoLoginFallido = (String) storedProcedureLoginFallido.getOutputParameterValue("resultado");
					em.close();
					JSONObject jsonRespuesta = new JSONObject(resultadoLoginFallido);
					conteoFallido = jsonRespuesta.getInt("login_fallido");
					
					
					if (conteoFallido >= 3) {
						mensaje = "Cuenta deshabilitada contacte al administrador";
					} else {
						mensaje = "Usuario o contraseña invalidos";
					}
				}
				return Response
						.status(Response.Status.UNAUTHORIZED)
						.entity(mensaje)
						.build();
			}
		} catch (Exception e) {
			jsonResponse = new JSONObject().put("error", "Ocurrio un error al ejecutar la operacion, por favor intente mas tarde").toString();

			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(jsonResponse)
					.build();
		}
	}
}
