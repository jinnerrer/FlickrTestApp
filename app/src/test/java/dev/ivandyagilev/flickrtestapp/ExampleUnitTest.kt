package dev.ivandyagilev.flickrtestapp

import android.content.Context
import android.test.InstrumentationTestCase
import android.test.mock.MockContext
import dev.ivandyagilev.flickrtestapp.Inject.DaggerPresenterComponents
import dev.ivandyagilev.flickrtestapp.Inject.LogicModule
import dev.ivandyagilev.flickrtestapp.Inject.PresenterComponents
import dev.ivandyagilev.flickrtestapp.Model.Photo
import dev.ivandyagilev.flickrtestapp.Presenter.ActivityPresenter
import org.junit.Assert
import org.junit.Test

import org.junit.Before
import org.mockito.Mockito.verify


@Suppress("DEPRECATION")
class ExampleUnitTest: InstrumentationTestCase() {

    private var component: PresenterComponents? = null
    lateinit var context: Context
    private var presenter: ActivityPresenter?= null

    @Before
    override fun setUp() {
        val photoList: MutableList<Photo> = ArrayList<Photo>()

        context = MockContext()

        Assert.assertNotNull(context)

        component = DaggerPresenterComponents.builder()
                .logicModule(LogicModule(context = context, photoList = photoList))
                .build()

        presenter = component!!.presenter
    }

    @Test
    fun testPresenter() {

        verify(presenter!!.attachView())
        if (presenter!!.isViewAttached) {
            println("presenter is attached")
        } else {
            println("presenter is not attached")
        }

    }
}
