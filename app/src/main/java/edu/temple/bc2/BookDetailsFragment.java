package edu.temple.bc2;

import android.content.Context;
import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import android.widget.ImageView;
import java.util.Objects;
import java.io.File;


import android.Manifest;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

import edu.temple.audiobookplayer.AudiobookService;


/**
 * A simple {@link Fragment} subclass.

 */
public class BookDetailsFragment extends Fragment {

    public static final String BOOK_KEY = "book";
    private OnBookPlay fragmentParent;
    ConstraintLayout bookDetailsView;
    ImageView bookCover;
    TextView bookTitle, bookAuthor, bookPublishedIn, bookPageLength;
    Button download;
    Button delete;
    String baseDownloadURL = "https://kamorris.com/lab/audlib/download.php?id=";
    AudiobookService audiobookService;
    boolean audioBound = false;


    Book book;
    Button playBtn;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param book String.
     * @return A new instance of fragment BookDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookDetailsFragment newInstance(Book book) {
        BookDetailsFragment bookDetailsFragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(BOOK_KEY, book);
        bookDetailsFragment.setArguments(args);
        return bookDetailsFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            book = args.getParcelable(BOOK_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bookDetailsView = (ConstraintLayout) inflater.inflate(R.layout.fragment_book_details, container, false);
        return bookDetailsView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bookCover = Objects.requireNonNull(getView()).findViewById(R.id.bookCover);
        bookTitle = Objects.requireNonNull(getView()).findViewById(R.id.bookTitle);
        bookAuthor = getView().findViewById(R.id.bookAuthor);
        bookPublishedIn = getView().findViewById(R.id.bookPublishedIn);
        bookPageLength = getView().findViewById(R.id.bookPageLength);
        if (book != null) {
            displayBook(book);
            playBtn = getView().findViewById(R.id.playBtn);
            playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentParent.playBook(book);

//                    File audio = new File(Environment.DIRECTORY_DOWNLOADS, book.getTitle() + ".mp3").getAbsoluteFile();
//                    if (!audio.exists()){
//                        ((audioControl) getActivity()).playAudio(book.getId(), progressBar.getProgress());
//                    }
                }
            });
        }

        download = getView().findViewById(R.id.downloadBtn);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t2 = new Thread() {
                    @Override
                    public void run() {
                        File check = new File(getContext().getFilesDir(), book.getTitle() + ".mp3");
                        Log.d("check path", check.getAbsolutePath());
                        Log.d("check exist", String.valueOf(check.exists()));
                        if (!check.exists()) {
                            try {

                                URL bookUrl = new URL(baseDownloadURL + book.getId());

                                URLConnection conn = bookUrl.openConnection();
                                int contentLength = conn.getContentLength();

                                DataInputStream stream = new DataInputStream(bookUrl.openStream());

                                byte[] buffer = new byte[contentLength];
                                stream.readFully(buffer);
                                stream.close();

                                DataOutputStream fos = new DataOutputStream(new FileOutputStream(check));
                                fos.write(buffer);
                                fos.flush();
                                fos.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                t2.start();
            }
        });


        delete = getView().findViewById(R.id.deleteBtn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File check = new File(getContext().getFilesDir(), book.getTitle() + ".mp3");
                if (check.exists()) {
                    check.delete();
                    Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Does not exist, cant delete", Toast.LENGTH_SHORT).show();
                }
            }
        });







    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BookDetailsFragment.OnBookPlay) {
            fragmentParent = (BookDetailsFragment.OnBookPlay) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBookPlay interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentParent = null;
    }


    // Public method for parent Activity to "talk" to BookDetailsFragment
    public void displayBook(Book book) {
        Picasso.get().load(book.getCoverUrl()).into(bookCover);
        bookTitle.setText(book.getTitle());
        bookTitle.setGravity(Gravity.CENTER);

        bookAuthor.setText(book.getAuthor());
        bookAuthor.setGravity(Gravity.CENTER);

        bookPublishedIn.setText(String.format(getResources().getString(R.string.publishedIn), book.getPublished()));
        bookPublishedIn.setGravity(Gravity.CENTER);

        //bookPageLength.setText(String.format(getResources().getString(R.string.pageLength), book.getDuration()));
        bookPageLength.setGravity(Gravity.CENTER);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnBookPlay {
        void playBook(Book book);
    }

}
