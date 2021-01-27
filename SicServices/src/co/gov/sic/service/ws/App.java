package co.gov.sic.service.ws;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("SicWS")
public class App extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> resources = new HashSet<>();
		addRestResourcesClasses(resources);
		return resources;
	}

	private void addRestResourcesClasses(Set<Class<?>> resources) {
	}
}