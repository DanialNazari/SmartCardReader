package com.danial.smartcardreader

import com.orhanobut.hawk.Hawk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CardNumberReaderApplication : android.app.Application() {
	override fun onCreate() {
		super.onCreate()
		Hawk.init(this).build()
		if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
	}
}