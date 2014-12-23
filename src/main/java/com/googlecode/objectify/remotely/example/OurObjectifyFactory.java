package com.googlecode.objectify.remotely.example;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceConfig;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.remotely.RemoteCheck;
import com.googlecode.objectify.remotely.Remotely;

/**
 */
public class OurObjectifyFactory extends ObjectifyFactory {

	private final Remotely remotely;

	public OurObjectifyFactory() {
		register(Thing.class);

		RemoteApiOptions options = new RemoteApiOptions()
				.server("voodoodyne1.appspot.com", 443)
//				.server("localhost", 8080)
				.credentials("asdf", "asdf");

		remotely = new Remotely(options, new RemoteCheck() {
			@Override
			public boolean isRemote(String namespace) {
				return true;
			}
		});
	}

	@Override
	protected AsyncDatastoreService createRawAsyncDatastoreService(DatastoreServiceConfig cfg) {
		return remotely.intercept(super.createRawAsyncDatastoreService(cfg));
	}
}
