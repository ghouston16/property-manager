package org.wit.property_manager.models

import timber.log.Timber.i


class UserMemStore: UserStore {
    val users = ArrayList<UserModel>()

    override fun findAll(): List<UserModel> {
        return users
    }

    override fun create(user: UserModel) {
        users.add(user)
        logAll()
    }
    fun logAll() {
        users.forEach{ i("${it}") }
    }
}