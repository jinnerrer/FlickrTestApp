package dev.ivandyagilev.flickrtestapp.FlickrSearchManager

import android.content.SearchRecentSuggestionsProvider

class MySuggestionProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {

        val AUTHORITY = "dev.ivandyagilev.flickrtestapp"
        val MODE = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES
    }

}
