package com.example.a3edhomework.horses.presentation.domain.cache

class BadgeCache {
    private var hasActiveFilters: Boolean = false

    fun setHasActiveFilters(hasFilters: Boolean) {
        this.hasActiveFilters = hasFilters
    }

    fun getHasActiveFilters(): Boolean {
        return hasActiveFilters
    }

    fun clear() {
        hasActiveFilters = false
    }
}