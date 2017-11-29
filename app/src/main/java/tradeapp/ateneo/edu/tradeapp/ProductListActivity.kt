package tradeapp.ateneo.edu.tradeapp

import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmList
import io.realm.Sort
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra
import org.androidannotations.annotations.UiThread
import tradeapp.ateneo.edu.tradeapp.adapters.ProductListAdapter
import tradeapp.ateneo.edu.tradeapp.filter.ProductFilter
import tradeapp.ateneo.edu.tradeapp.model.Bookmark
import tradeapp.ateneo.edu.tradeapp.model.Product
import tradeapp.ateneo.edu.tradeapp.service.UserService
import java.util.*


@EActivity(R.layout.activity_main)
open class ProductListActivity : AbstractMainActivity() {

    @Extra("filter")
    lateinit var filter: ProductFilter

    override fun showData() {
        updateView()
    }

    @UiThread
    protected open fun updateView(){
        val user = userService.getLoggedInUser()
        val realm = Realm.getDefaultInstance()
        var products: OrderedRealmCollection<Product>
        when(filter.type){
            "category" ->  {
                products = realm.where(Product::class.java)
                        .equalTo("category.name", filter.keyword)
                        .equalTo("sold", false)
                        .findAllSorted("dateCreated", Sort.DESCENDING)
            }
            "bought" -> {
                products = realm.where(Product::class.java)
                        .equalTo("sold", true)
                        .equalTo("reservedTo.username", userService.getLoggedInUser()!!.username)
                        .findAllSorted("dateCreated", Sort.DESCENDING)
            }
            "sold" -> {
                products = realm.where(Product::class.java)
                        .equalTo("sold", true)
                        .equalTo("user.username", userService.getLoggedInUser()!!.username)
                        .findAllSorted("dateCreated", Sort.DESCENDING)
            }
            "bookmark" -> {
                val unmanagedProducts = realm.where(Bookmark::class.java)
                        .equalTo("user.username", userService.getLoggedInUser()!!.username)
                        .findAllSorted("dateCreated", Sort.DESCENDING)
                        .map { b -> b.product }
                products = realm.where(Product::class.java).`in`("uuid", unmanagedProducts.map { p -> p!!.uuid }.toTypedArray()).findAll()
            }
            else -> throw UnsupportedOperationException("${filter.type} not yet supported")
        }


        listView.adapter = ProductListAdapter(userService.getLoggedInUser(), this.applicationContext, products)
    }


    override fun onResume() {
        super.onResume()
        updateView()
    }

}
