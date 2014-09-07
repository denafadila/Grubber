package skripsi.com.grubber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.util.Log;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that contain this fragment
 * must implement the {@link TNCDialogFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link TNCDialogFragment#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class TNCDialogFragment extends DialogFragment {
  private static final String TAG = TNCDialogFragment.class.getSimpleName();
  public final static int TYPE_TERMS_OF_SERVICE = 100;
  public final static int TYPE_PRIVACY_POLICY = 200;

  private int mType;

  public static TNCDialogFragment newInstance(int type) {
    TNCDialogFragment f = new TNCDialogFragment();
    f.setType(type);
    return f;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    String title = null;
    String rName = null;
    switch (mType) {
    case TYPE_TERMS_OF_SERVICE:
      title = getString(R.string.grubber_registration_agree_tncc_title_tos);
      rName = "tos.html";
      break;
    case TYPE_PRIVACY_POLICY:
      title = getString(R.string.grubber_registration_agree_tncc_title_pp);
      rName = "pp.html";
      break;
    }
    builder.setTitle(title);
    BufferedReader br = null;
    StringBuilder message = new StringBuilder();
    try {
      br = new BufferedReader(new InputStreamReader(getResources().getAssets().open(rName)));
      String temp = null;
      while ((temp = br.readLine()) != null) {
        message.append(temp);
      }
    } catch (IOException e) {
      message.append(getResources().getString(R.string.grubber_registration_agree_tncc_error));
      Log.w(TAG, "Problem reading Terms of Service or Privacy Policy", e);
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          // ignore this exception
        }
      }
    }
    builder.setMessage(Html.fromHtml(message.toString()));
    builder.setPositiveButton(R.string.grubber_registration_agree_tncc_ok, null);
    return builder.create();
  }

  private void setType(int type) {
    this.mType = type;
  }
}
