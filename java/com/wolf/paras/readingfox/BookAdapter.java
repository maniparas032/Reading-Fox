package com.wolf.paras.readingfox;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter( Context context, List<Book> bookList) {
        super(context,0, bookList);
    }

    public static int mPosition;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_home, parent, false);
        }

        Book book = getItem(position);
        mPosition = position;

        TextView title = (TextView) listItemView.findViewById(R.id.titleOfBook);
        title.setText(book.getTitle());

        TextView authors = (TextView) listItemView.findViewById((R.id.authorofBook));
        authors.setText(book.getAuthorName());

        TextView category = (TextView) listItemView.findViewById(R.id.categotyOfBook);
        category.setText(book.getCategoryList());

        TextView languahe = (TextView) listItemView.findViewById(R.id.languageOfBook);
        languahe.setText(book.getLanguage());


        ImageView imageView = (ImageView) listItemView.findViewById(R.id.thumbnail);
        imageView.setImageBitmap(book.getSmallThumbnail());

        TextView rating = (TextView) listItemView.findViewById(R.id.rating);
        rating.setText(book.getmAverageRating());

        TextView ratingcount = (TextView) listItemView.findViewById(R.id.rating_count);
        ratingcount.setText(book.getRatingCount());

        TextView maturityRating = (TextView) listItemView.findViewById(R.id.maturity_rating);
        maturityRating.setText(book.getMaturityRating());

        TextView description = (TextView) listItemView.findViewById(R.id.description);
        description.setText(book.getDescription());

        TextView price = (TextView) listItemView.findViewById(R.id.price);
        price.setText(book.getPrice());



        return listItemView;
    }

    public  static int getPos() {
        return mPosition;
    }

}
