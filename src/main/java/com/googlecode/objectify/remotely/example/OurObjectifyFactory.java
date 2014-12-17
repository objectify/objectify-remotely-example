package com.googlecode.objectify.remotely.example;

import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceConfig;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.remotely.RemoteAsyncDatastoreService;

/**
 */
public class OurObjectifyFactory extends ObjectifyFactory {

	public OurObjectifyFactory() {
		register(Thing.class);
	}

	@Override
	protected AsyncDatastoreService createRawAsyncDatastoreService(DatastoreServiceConfig cfg) {
		return RemoteAsyncDatastoreService.create(super.createRawAsyncDatastoreService(cfg));
	}
}
