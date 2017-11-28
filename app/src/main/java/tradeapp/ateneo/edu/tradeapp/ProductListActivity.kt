package tradeapp.ateneo.edu.tradeapp

import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.Sort
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra
import org.androidannotations.annotations.UiThread
import tradeapp.ateneo.edu.tradeapp.adapters.ProductListAdapter
import tradeapp.ateneo.edu.tradeapp.filter.ProductFilter
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
        val realm = Realm.getDefaultInstance()
        var products: OrderedRealmCollection<Product>
        when(filter.type){
            "category" ->  {
                products = realm.where(Product::class.java)
                        .equalTo("category.name", filter.keyword)
                        .equalTo("sold", false)
                        .findAllSorted("dateCreated", Sort.DESCENDING)
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
