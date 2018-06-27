package dev.ivandyagilev.flickrtestapp.Inject


import dagger.Component
import dev.ivandyagilev.flickrtestapp.Adapter.GridAdapter
import dev.ivandyagilev.flickrtestapp.Presenter.ActivityPresenter
import dev.ivandyagilev.flickrtestapp.View.LoadingDialog


@Component(modules = arrayOf(LogicModule::class))
interface PresenterComponents {
    val presenter: ActivityPresenter
    val adapter: GridAdapter
    val loadingDialog: LoadingDialog
}
