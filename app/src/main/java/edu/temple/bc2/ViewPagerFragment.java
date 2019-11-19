package edu.temple.bc2;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import android.content.res.Resources;



/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPagerFragment extends Fragment {

    ViewPager mPager;
    PagerAdapter pagerAdapter;
    ArrayList<Book> books;
    ArrayList<BookDetailsFragment> bookDetailsFragments;
    public final static String BOOKS_KEY = "books";

    public ViewPagerFragment() {
        // Required empty public constructor

    }

    public static ViewPagerFragment newInstance(ArrayList<Book> books) {
        ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BOOKS_KEY, books);
        viewPagerFragment.setArguments(args);
        return viewPagerFragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        bookDetailsFragments = new ArrayList<>();
        if (args != null) {
            books = args.getParcelableArrayList(BOOKS_KEY);
            // For each book in the books ArrayList<Book>, create a BookDetailsFragment
            if (books != null) {
                for (int i = 0; i < books.size(); i++) {
                    bookDetailsFragments.add(BookDetailsFragment.newInstance(books.get(i)));
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        mPager = view.findViewById(R.id.viewPagerFragment);
        // Instantiate a ViewPager and a PagerAdapter.
        pagerAdapter = new BookDetailsPagerAdapter(getChildFragmentManager(), bookDetailsFragments);
        mPager.setAdapter(pagerAdapter);
        return view;
    }




    private class BookDetailsPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<BookDetailsFragment> bookDetailsFragments; // hold reference to ViewPagers fragments

        BookDetailsPagerAdapter(FragmentManager fm, ArrayList<BookDetailsFragment> bookDetailsFragments) {
            super(fm);
            this.bookDetailsFragments = bookDetailsFragments;
        }

        @Override
        public Fragment getItem(int position) {
            return bookDetailsFragments.get(position);
        }




        @Override
        public int getCount() {
            return bookDetailsFragments.size();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }
    public ArrayList<BookDetailsFragment> getBooks() {
        return this.bookDetailsFragments;
    }



}
