package com.a30corner.combomaster.padherder;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.functions.Func1;

import com.a30corner.combomaster.padherder.vo.PadHerder;


public class PadHerderLoader {
	private static final String URL_PAD_HERDER = "https://www.padherder.com/";
	
	public static Observable<PadHerder> load(String userName, final ErrorHandler handler) {
		return Observable.just(userName)
				.observeOn(Schedulers.io())
				.map(new Func1<String, PadHerder>() {

					@Override
					public PadHerder call(String name) {
						RestAdapter adapter = new RestAdapter.Builder().setEndpoint(URL_PAD_HERDER)
								.setErrorHandler(new ErrorHandler() {
									
									@Override
									public Throwable handleError(RetrofitError error) {
										return handler.handleError(error);
									}
								}).build();
						return adapter.create(IPadHerder.class).getUserData(name);
					}
				});
	}
}
