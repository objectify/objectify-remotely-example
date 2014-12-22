package com.googlecode.objectify.remotely.example;

import com.google.appengine.tools.remoteapi.RemoteApiOptions;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.remotely.Remotely;
import com.googlecode.objectify.remotely.VoidCallable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static com.googlecode.objectify.ObjectifyService.ofy;
import static com.googlecode.objectify.ObjectifyService.run;

/**
 */
public class Run extends HttpServlet {

	@Override
	public void init() throws ServletException {
		ObjectifyService.setFactory(new OurObjectifyFactory());

		RemoteApiOptions options = new RemoteApiOptions()
				.server("voodoodyne1.appspot.com", 443)
//				.server("localhost", 8080)
				.credentials("asdf", "asdf");
		Remotely.setOptions(options);
	}

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {

		// It's like the movie Inception, down the rabbithole we go...
		run(new VoidWork() {
			@Override
			public void vrun() {
				Remotely.execute(new VoidCallable() {
					@Override
					public void run() {
						Thing thing = new Thing();
						thing.setName("foo");
						ofy().save().entity(thing).now();

						ofy().clear();

						Thing fetched = ofy().load().entity(thing).now();

						write(resp, "Thing before roundtrip: " + thing);
						write(resp, "Thing after roundtrip: " + fetched);
					}
				});
			}
		});

		run(new VoidWork() {
			@Override
			public void vrun() {
				Remotely.execute(new VoidCallable() {
					@Override
					public void run() {
						write(resp, "List of things:");

						for (Thing th : ofy().load().type(Thing.class)) {
							write(resp, th.toString());
						}
					}
				});

			}
		});
	}

	private void write(HttpServletResponse resp, String someText) {
		try {
			resp.getWriter().println(someText);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
