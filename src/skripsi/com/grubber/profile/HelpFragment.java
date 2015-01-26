package skripsi.com.grubber.profile;

import skripsi.com.grubber.BaseActivity;
import skripsi.com.grubber.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class HelpFragment extends Fragment {
  private final static String TAG = HelpFragment.class.getSimpleName();

  // Declare
  private TextView tvReview, tvSearchRest, tvComment, tvSearchUser, tvTrend, tvFB, tvAddRest;
  private TextView tvAnswerRev, tvAnswerSearchRest, tvAnswerComment, tvAnswerSearchUser,
      tvAnswerTrend, tvAnswerFB, tvAnswerAddRest;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    setHasOptionsMenu(true);
    ((BaseActivity) getActivity()).setTitle("Profile");
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // TODO Auto-generated method stub

    View v = inflater.inflate(R.layout.fragment_help, null);

    // initialize answers
    tvAnswerRev = (TextView) v.findViewById(R.id.answerReview);
    tvAnswerSearchRest = (TextView) v.findViewById(R.id.answerRest);
    tvAnswerComment = (TextView) v.findViewById(R.id.answerComment);
    tvAnswerSearchUser = (TextView) v.findViewById(R.id.answerSearch);
    tvAnswerTrend = (TextView) v.findViewById(R.id.answerTrending);
    tvAnswerFB = (TextView) v.findViewById(R.id.answerShare);
    tvAnswerAddRest = (TextView) v.findViewById(R.id.answerAddRest);

    tvAnswerRev.setVisibility(View.GONE);
    tvAnswerSearchRest.setVisibility(View.GONE);
    tvAnswerComment.setVisibility(View.GONE);
    tvAnswerSearchUser.setVisibility(View.GONE);
    tvAnswerTrend.setVisibility(View.GONE);
    tvAnswerFB.setVisibility(View.GONE);
    tvAnswerAddRest.setVisibility(View.GONE);

    // initialize questions
    tvReview = (TextView) v.findViewById(R.id.howReview);
    tvReview.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        tvAnswerRev.setVisibility(View.VISIBLE);
        tvAnswerSearchRest.setVisibility(View.GONE);
        tvAnswerComment.setVisibility(View.GONE);
        tvAnswerSearchUser.setVisibility(View.GONE);
        tvAnswerTrend.setVisibility(View.GONE);
        tvAnswerFB.setVisibility(View.GONE);
        tvAnswerAddRest.setVisibility(View.GONE);
      }
    });
    tvSearchRest = (TextView) v.findViewById(R.id.searchRest);
    tvSearchRest.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        tvAnswerRev.setVisibility(View.GONE);
        tvAnswerSearchRest.setVisibility(View.VISIBLE);
        tvAnswerComment.setVisibility(View.GONE);
        tvAnswerSearchUser.setVisibility(View.GONE);
        tvAnswerTrend.setVisibility(View.GONE);
        tvAnswerFB.setVisibility(View.GONE);
        tvAnswerAddRest.setVisibility(View.GONE);

      }
    });
    tvComment = (TextView) v.findViewById(R.id.addComment);
    tvComment.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        tvAnswerRev.setVisibility(View.GONE);
        tvAnswerSearchRest.setVisibility(View.GONE);
        tvAnswerComment.setVisibility(View.VISIBLE);
        tvAnswerSearchUser.setVisibility(View.GONE);
        tvAnswerTrend.setVisibility(View.GONE);
        tvAnswerFB.setVisibility(View.GONE);
        tvAnswerAddRest.setVisibility(View.GONE);

      }
    });

    tvSearchUser = (TextView) v.findViewById(R.id.searchUser);
    tvSearchUser.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        tvAnswerRev.setVisibility(View.GONE);
        tvAnswerSearchRest.setVisibility(View.GONE);
        tvAnswerComment.setVisibility(View.GONE);
        tvAnswerSearchUser.setVisibility(View.VISIBLE);
        tvAnswerTrend.setVisibility(View.GONE);
        tvAnswerFB.setVisibility(View.GONE);
        tvAnswerAddRest.setVisibility(View.GONE);

      }
    });
    tvTrend = (TextView) v.findViewById(R.id.findTrending);
    tvTrend.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        tvAnswerRev.setVisibility(View.GONE);
        tvAnswerSearchRest.setVisibility(View.GONE);
        tvAnswerComment.setVisibility(View.GONE);
        tvAnswerSearchUser.setVisibility(View.GONE);
        tvAnswerTrend.setVisibility(View.VISIBLE);
        tvAnswerFB.setVisibility(View.GONE);
        tvAnswerAddRest.setVisibility(View.GONE);

      }
    });
    tvFB = (TextView) v.findViewById(R.id.shareFB);
    tvFB.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        tvAnswerRev.setVisibility(View.GONE);
        tvAnswerSearchRest.setVisibility(View.GONE);
        tvAnswerComment.setVisibility(View.GONE);
        tvAnswerSearchUser.setVisibility(View.GONE);
        tvAnswerTrend.setVisibility(View.GONE);
        tvAnswerFB.setVisibility(View.VISIBLE);
        tvAnswerAddRest.setVisibility(View.GONE);

      }
    });
    tvAddRest = (TextView) v.findViewById(R.id.addRest);
    tvAddRest.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        tvAnswerRev.setVisibility(View.GONE);
        tvAnswerSearchRest.setVisibility(View.GONE);
        tvAnswerComment.setVisibility(View.GONE);
        tvAnswerSearchUser.setVisibility(View.GONE);
        tvAnswerTrend.setVisibility(View.GONE);
        tvAnswerFB.setVisibility(View.GONE);
        tvAnswerAddRest.setVisibility(View.VISIBLE);

      }
    });

    return v;
  }

}
