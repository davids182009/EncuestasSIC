package co.gov.sic.service.data.business;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.ParameterMode;
import javax.persistence.Persistence;
import javax.persistence.StoredProcedureQuery;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import co.gov.sic.service.data.interfaces.InterfaceDataService;

public class ImpInterfaceDataService implements InterfaceDataService {

	String jsonResponse = "";
	public Response consultarMarcasPC() {

		try {
			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory( "SicServices" );
			EntityManager em = entityManagerFactory.createEntityManager();

			StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("consulta_marcas_pc");
			storedProcedure.registerStoredProcedureParameter("resultado", String.class, ParameterMode.OUT);
			storedProcedure.execute();

			String resultado = (String) storedProcedure.getOutputParameterValue("resultado");
			em.close();	

			return Response
					.status(Response.Status.OK)
					.entity(resultado)
					.build();

		} catch (Exception e) {
			jsonResponse = new JSONObject().put("error", "Ocurrio un error al ejecutar la operacion, por favor intente mas tarde").toString();

			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(jsonResponse)
					.build();
		}
	}

	@Override
	public Response insertarDataDB(String clientData) {

		try {
			System.out.println("DATA ENVIADA CLIENTE...");
			System.out.println(clientData);

			JSONObject clientJson= null; 
			clientJson = new JSONObject(clientData);

			int idUsuario = clientJson.getInt("idUsuario");
			System.out.println("ID USUARIO EN JAVA... " + idUsuario);

			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory( "SicServices" );
			EntityManager em = entityManagerFactory.createEntityManager();

			StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("insertar_informacion");
			storedProcedure.registerStoredProcedureParameter("objetoJson", String.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("resultado", String.class, ParameterMode.OUT);
			storedProcedure.setParameter("objetoJson", clientData);
			storedProcedure.execute();

			String resultado = (String) storedProcedure.getOutputParameterValue("resultado");
			JSONObject jsonRespuesta = new JSONObject(resultado);
			String idEncuestaInsertada = jsonRespuesta.getString("id");

			StoredProcedureQuery storedProcedureAsocia = em.createStoredProcedureQuery("asociar_usuario_encuesta");
			storedProcedureAsocia.registerStoredProcedureParameter("idUsuario", Integer.class, ParameterMode.IN);
			storedProcedureAsocia.registerStoredProcedureParameter("idEncuesta", Integer.class, ParameterMode.IN);
			storedProcedureAsocia.registerStoredProcedureParameter("resultado", String.class, ParameterMode.OUT);
			storedProcedureAsocia.setParameter("idUsuario", idUsuario);
			storedProcedureAsocia.setParameter("idEncuesta", Integer.parseInt(idEncuestaInsertada));
			storedProcedureAsocia.execute();

			String resultadoAsocia = (String) storedProcedureAsocia.getOutputParameterValue("resultado");
			em.close();
			JSONObject jsonRespuestaAsocia = new JSONObject(resultadoAsocia);

			if (jsonRespuestaAsocia.get("resultado").equals("OK")) {
				jsonResponse = new JSONObject().put("exito", "Se ha contestado correctamente la encuesta").toString();

				return Response
						.status(Response.Status.OK)
						.entity(jsonResponse)
						.build();
			} else {
				jsonResponse = new JSONObject().put("error", "Ocurrio un error al guardar la encuesta, por favor intente mas tarde").toString();

				return Response
						.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity(jsonResponse)
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

	@Override
	public Response consultarEncuestas(String clientData) {

		try {
			JSONObject clientJson= null; 
			clientJson = new JSONObject(clientData);
			int idUsuarioConsultar = clientJson.getInt("idUsuario");

			System.out.println("ID USUARIO...");
			System.out.println(idUsuarioConsultar);

			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory( "SicServices" );
			EntityManager em = entityManagerFactory.createEntityManager();

			StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("consultar_encuestas");
			storedProcedure.registerStoredProcedureParameter("id_usuario", Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("resultado", String.class, ParameterMode.OUT);
			storedProcedure.setParameter("id_usuario", idUsuarioConsultar);
			storedProcedure.execute();

			String resultado = (String) storedProcedure.getOutputParameterValue("resultado");
			em.close();	

			System.out.println("RESULTADO ENCUESTAS...");
			System.out.println(resultado);

			return Response
					.status(Response.Status.OK)
					.entity(resultado)
					.build();
		} catch (Exception e) {
			jsonResponse = new JSONObject().put("error", "Ocurrio un error al ejecutar la operacion, por favor intente mas tarde").toString();

			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(jsonResponse)
					.build();
		}
	}

	@Override
	public Response eliminarEncuestas(String clientData) {

		try {
			JSONObject clientJson= null; 
			clientJson = new JSONObject(clientData);
			int idEncuestaEliminar = clientJson.getInt("idEncuesta");

			System.out.println("ID ENCUESTA...");
			System.out.println(idEncuestaEliminar);

			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory( "SicServices" );
			EntityManager em = entityManagerFactory.createEntityManager();

			StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("eliminar_encuesta");
			storedProcedure.registerStoredProcedureParameter("idEncuesta", Integer.class, ParameterMode.IN);
			storedProcedure.registerStoredProcedureParameter("resultado", String.class, ParameterMode.OUT);
			storedProcedure.setParameter("idEncuesta", idEncuestaEliminar);
			storedProcedure.execute();

			String resultado = (String) storedProcedure.getOutputParameterValue("resultado");
			em.close();
			JSONObject jsonRespuesta = new JSONObject(resultado);

			if (jsonRespuesta.get("resultado").equals("OK")) {
				jsonResponse = new JSONObject().put("exito", "Encuesta eliminada correctamente").toString();

				return Response
						.status(Response.Status.OK)
						.entity(jsonResponse)
						.build();
			} else {
				jsonResponse = new JSONObject().put("error", "Ocurrio un error al guardar la encuesta, por favor intente mas tarde").toString();

				return Response
						.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity(jsonResponse)
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