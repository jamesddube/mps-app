package com.intersect.app.mps;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.intersect.app.mps.api.requests.ApiCalls;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by james on 5/3/16.
 */
public class App extends Application{
	private static App Instance;
	private static RealmConfiguration realmConfig;
	public static String Tag = "REALM";
	private static User User;
	private static ApiCalls apiCalls;

	@Override
	public void onCreate()
	{
		super.onCreate();
		realmConfig = new RealmConfiguration.Builder(this)
				//.deleteRealmIfMigrationNeeded() //rebuild schema on each launch, for dev purposes
				.build();
		Instance = this;
		apiCalls = new ApiCalls();
		//realm browser

	}

	public static App getInstance()
	{
		if(Instance == null)
		{
			Log.d(Tag,"app init");
			Instance = new App();
		}
		return Instance;
	}

	public static Context getAppContext()
	{
		return Instance.getApplicationContext();
	}


	public static Realm Realm(){

		Realm.setDefaultConfiguration(realmConfig);

		// Get a Realm instance for this thread
		return Realm.getDefaultInstance();
	}

	public static ApiCalls Api()
	{
		return apiCalls;
	}

	public static User currentUser()
	{
		User = new User();
		User.setId(1);
		User.setName("James");
		User.setEmail("info@mps.com");
		User.setRoute(App.Realm().where(Route.class).findFirst());
		User.setSurname("Dube");

		return User;

	}
}
