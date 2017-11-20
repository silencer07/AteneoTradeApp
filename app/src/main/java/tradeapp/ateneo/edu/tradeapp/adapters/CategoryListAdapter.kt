package tradeapp.ateneo.edu.tradeapp.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListAdapter
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter
import tradeapp.ateneo.edu.tradeapp.R
import tradeapp.ateneo.edu.tradeapp.model.Category

/**
 * Created by aldrin on 11/20/17.
 */

class CategoryListAdapter(context: Context, categories: OrderedRealmCollection<Category>): RealmBaseAdapter<Category>(categories)
        , ListAdapter {

    private class ViewHolder {
        internal var categoryButton: ImageButton? = null
        internal var categoryName: TextView? = null
    }

    override fun getView(position: Int, _convertView: View?, parent: ViewGroup?): View {
        var viewHolder: ViewHolder
        var convertView = _convertView;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent!!.context)
                    .inflate(R.layout.category_card, parent, false)
            viewHolder = ViewHolder()
            viewHolder.categoryButton = convertView.findViewById(R.id.categoryButton) as ImageButton
            viewHolder.categoryName = convertView.findViewById(R.id.categoryName) as TextView
            convertView.setTag(viewHolder)
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        if (adapterData != null) {
            val category = adapterData!![position]
            val resources = parent!!.context.resources
            val packageName = parent.context.packageName
            val image = resources.getDrawable(resources.getIdentifier(category.photo, "drawable", packageName), null)
            viewHolder.categoryButton!!.setImageDrawable(image)
            viewHolder.categoryButton!!.contentDescription = category.name
            viewHolder.categoryName!!.text = category.name

            /*
            if (inDeletionMode) {
                viewHolder.deleteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        countersToDelete.add(item.getCount());
                    }
                });
            } else {
                viewHolder.deleteCheckBox.setOnCheckedChangeListener(null);
            }
            viewHolder.deleteCheckBox.setChecked(countersToDelete.contains(item.getCount()));
            viewHolder.deleteCheckBox.setVisibility(inDeletionMode ? View.VISIBLE : View.GONE);
            */
        }
        return convertView!!
    }

}
