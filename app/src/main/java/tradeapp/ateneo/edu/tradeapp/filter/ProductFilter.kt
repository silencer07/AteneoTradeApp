package tradeapp.ateneo.edu.tradeapp.filter

import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class ProductFilter(val type: String, val keyword: String) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelProductFilter.CREATOR
    }
}