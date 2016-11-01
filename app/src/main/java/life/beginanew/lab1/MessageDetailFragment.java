package life.beginanew.lab1;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import life.beginanew.lab1.dummy.DummyContent;

/**
 * A fragment representing a single Message detail screen.
 * This fragment is either contained in a {@link MessageListActivity}
 * in two-pane mode (on tablets) or a {@link MessageDetailActivity}
 * on handsets.
 */
public class MessageDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    // Lab 7-11: Change private DummyContent.DummyItem mItem; to: private String mItem;
    // private DummyContent.DummyItem mItem;
    private String mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MessageDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            // we are directly sending the string, instead of item ID, change
            // mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));.
            //   to: mItem = getArguments().getString(ARG_ITEM_ID);
            mItem = getArguments().getString(ARG_ITEM_ID);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                // Lab 7-12: Change the appBarLayout.setTitle( ) so that “Detailed message” is the new parameter.
                appBarLayout.setTitle(getText(R.string.detailed_message));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.message_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            // Lab 7-13: In the onCreateView() function, change the mItem.details to just mItem
            ((TextView) rootView.findViewById(R.id.message_detail)).setText(mItem);
        }

        return rootView;
    }
}
