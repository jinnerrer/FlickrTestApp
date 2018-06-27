package dev.ivandyagilev.flickrtestapp.Activity

import android.app.SearchManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView

import butterknife.BindView
import butterknife.ButterKnife
import dev.ivandyagilev.flickrtestapp.Interface.SearchActivityMvpView
import dev.ivandyagilev.flickrtestapp.Presenter.ActivityPresenter
import dev.ivandyagilev.flickrtestapp.R
import android.content.Intent
import android.database.Cursor
import android.os.Handler
import dev.ivandyagilev.flickrtestapp.FlickrSearchManager.MySuggestionProvider
import android.provider.SearchRecentSuggestions
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.widget.Toast
import dev.ivandyagilev.flickrtestapp.Adapter.GridAdapter
import dev.ivandyagilev.flickrtestapp.Inject.DaggerPresenterComponents
import dev.ivandyagilev.flickrtestapp.Model.Photo
import dev.ivandyagilev.flickrtestapp.Inject.LogicModule
import dev.ivandyagilev.flickrtestapp.View.LoadingDialog
import android.view.Window
import android.transition.Explode


class SearchActivity : AppCompatActivity(), SearchActivityMvpView {

    private var gridLayoutManager: GridLayoutManager? = null

    private var queryStr: String = ""
    private var page: Int = 1


    @BindView(R.id.recyclerView)
    lateinit var mRecyclerView: RecyclerView

    @BindView(R.id.searchView)
    lateinit var mSearchView: SearchView

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    private var suggestions: SearchRecentSuggestions? = null
    private var searchManager: SearchManager? =null

    private var adapter: GridAdapter? = null
    private var presenter : ActivityPresenter? = null
    private var photoList: MutableList<Photo> = ArrayList<Photo>()

    private var loadingDialog: LoadingDialog? = null

    private var doubleBackToExitPressedOnce: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setUpWindowAnimation()

        setContentView(R.layout.activity_search)
        ButterKnife.bind(this)


        toolbar.setTitle(getString(R.string.app_title))
        setSupportActionBar(toolbar)


        val components = DaggerPresenterComponents.builder()
                .logicModule(LogicModule(this, photoList))
                .build()
        presenter = components.presenter
        adapter = components.adapter
        loadingDialog = components.loadingDialog

        mRecyclerView.setHasFixedSize(true)
        gridLayoutManager = GridLayoutManager(this,2)
        mRecyclerView.layoutManager = gridLayoutManager

        mRecyclerView.adapter = adapter

        mRecyclerView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (!mRecyclerView.canScrollVertically(1)) {
                page = page + 1;
                presenter!!.loadImages(queryStr, page)
            }
        }
                searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
                suggestions = SearchRecentSuggestions(this, MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)

        if (searchManager != null) {
            mSearchView.setSearchableInfo(searchManager!!.getSearchableInfo(getComponentName()))
        }

        mSearchView.setIconifiedByDefault(false)
        mSearchView.setQueryRefinementEnabled(true)
        mSearchView.requestFocus(1)

        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                if (!query.isEmpty()) {
                    suggestions!!.saveRecentQuery(query, null)
                    searchInit(query)
                }

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        mSearchView.setOnSuggestionListener(
        object  : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return true
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val selectedView = mSearchView.getSuggestionsAdapter()
                val cursor = selectedView.getItem(position) as Cursor
                val index = cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1)
                mSearchView.setQuery(cursor.getString(index), true)
                return true
            }
        })
    }

    private fun setUpWindowAnimation() {
        val explode = Explode()
        window.exitTransition = explode
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
    }

    private fun searchInit(query: String) {
        page = 1
        queryStr = query
        mSearchView.isFocusable = false
        presenter!!.loadImages(query, page)
        mSearchView.queryHint = query
    }

    override fun onStart() {
        super.onStart()

        if (!presenter!!.isViewAttached){
            presenter!!.attachView(this)
        }

    }

    override fun onStop() {
        super.onStop()
        if (presenter!!.isViewAttached){
            presenter!!.detachView()
        }
    }

    override fun startLoading() {
        initDialog(1)
    }

    override fun stopLoading() {
        initDialog(2)
        adapter!!.notifyDataSetChanged()
    }

    override fun noResults() {
        Toast.makeText(this, getString(R.string.no_results_error), Toast.LENGTH_SHORT).show()
    }

    private fun initDialog(state: Int){
        when (state) {
            1 -> {
                if (!loadingDialog!!.isShowing) {
                    loadingDialog!!.show()
                }
                return
            }
            2 -> {
                if (loadingDialog!!.isShowing()) {
                    loadingDialog!!.cancel()
                }
                return
            }
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, R.string.back_to_exit, Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

}
